package ru.tykvin.view;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import ru.tykvin.loader.Config;
import ru.tykvin.loader.Constants;
import ru.tykvin.model.data.DataBean;
import ru.tykvin.model.data.Source;

public class Report {

    Map<String, Object> parameters = new HashMap<String, Object>();
    private ArrayList<DataBean> dataList = new ArrayList<DataBean>();

    public static void main(String[] args) throws JRException {
        (new Report()).show();
    }

    public void show() throws JRException {
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
        parameters.put("INFO", "Hello");

        JasperReport report = (JasperReport) JRLoader.loadObject(new File("../templates/template.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanColDataSource);

        JFrame frame = new JFrame("Report");
        JRViewer view = new JRViewer(jasperPrint);
        view.setFitWidthZoomRatio();
        frame.getContentPane().add(view);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public void show(Config config, List<Source> source) throws JRException, ParseException {
        fillParameters(config, source);
        fillDataSet(config, source);
        show();
    }

    private void fillParameters(Config config, List<Source> source) {
        SimpleDateFormat format = new SimpleDateFormat("dd, MMMM yyyy");
        parameters.put("COMPANY", config.getCompany());
        parameters.put("CODE", config.getCode());
        parameters.put("COEFFICIENT", config.getCoefficient());
        parameters.put("CONTRACT", config.getContract());
        parameters.put("TYPE", config.getType());
        parameters.put("DATE", format.format(source.get(0).getDate()));
        parameters.put("MAKER", config.getMaker());
        for (int i = 0; i < 4; i++) {
            parameters.put("COUNTER" + (i + 1), source.size() <= i ? "" : source.get(i).getNumber());
            parameters.put("SUM" + (i + 1), source.size() <= i ? null : source.get(i).getSum() / 10000 * config.getCorrection());
            parameters.put("SUMC" + (i + 1), source.size() <= i ? null : source.get(i).getSum() / 10000 * config.getCoefficient() * config.getCorrection());
        }
    }

    private void fillDataSet(Config config, List<Source> sourceList) throws ParseException {
        SimpleDateFormat dstFormat = new SimpleDateFormat(Constants.timesFormat);
        Integer k = config.getCoefficient();
        Double correct = config.getCorrection();
        DataBean bean;
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Source source;
        int i = 0;
        for (Date t : config.getTimes()) {
            String time = dstFormat.format(t);
            bean = new DataBean();
            if (i++ == 0 && "24-00".equals(time)) {
                bean.setTime("0-00");
                for (int j = 0; j < sourceList.size(); j++) {
                    source = sourceList.get(j);
                    bean.getVal()[j] = source.getBeginConsuming();
                    bean.getValC()[j] = source.getBeginConsuming() * k;
                }
                dataList.add(bean);
            } else {
                for (int j = 0; j < sourceList.size(); j++) {
                    source = sourceList.get(j);
                    c.setTime(source.getDate());
                    c1.setTime(t);
                    c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), 0);
                    if ("24-00".equals(time)) {
                        c.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    Double val = source.getConsumings().get(c.getTime());
                    System.out.println(c.getTime() + " >> " + (val == null ? 0 : val));
                    bean.getVal()[j] = val / 10000 * correct + source.getBeginConsuming();
                    val += (source.getBeginConsuming() * 10000);
                    val *= 40;
                    val *= correct;
                    val /= 10000;
                    bean.getValC()[j] = (val + source.getBeginConsuming() * 10000) * 40 / 10000;
                    bean.setTime(dstFormat.format(c1.getTime()));
                }
            }
            dataList.add(bean);
        }
    }

}
