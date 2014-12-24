package ru.tykvin.view;

import java.io.File;
import java.math.BigDecimal;
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
        parameters.put("COEFFICIENT", config.getCoefficient().intValue());
        parameters.put("CONTRACT", config.getContract());
        parameters.put("TYPE", config.getType());
        parameters.put("DATE", format.format(source.get(0).getDate()));
        parameters.put("MAKER", config.getMaker());
        for (int i = 0; i < 4; i++) {
            parameters.put("COUNTER" + (i + 1), source.size() <= i ? "" : source.get(i).getNumber());
            parameters.put("SUM" + (i + 1), source.size() <= i ? null : source.get(i).getSum().multiply(config.getCorrection()).doubleValue());
            parameters.put("SUMC" + (i + 1), source.size() <= i ? null : source.get(i).getSum().multiply(config.getCoefficient()).multiply(config.getCorrection()).doubleValue());
        }
    }

    private void fillDataSet(Config config, List<Source> sourceList) throws ParseException {
        SimpleDateFormat dstFormat = new SimpleDateFormat(Constants.timesFormat);
        Calendar beginConsumingCalendar = Calendar.getInstance();
        Date beginConsumingTime = new SimpleDateFormat("H-mm").parse("9-00");
        beginConsumingCalendar.setTime(beginConsumingTime);
        BigDecimal k = config.getCoefficient();
        BigDecimal correct = config.getCorrection();
        DataBean bean;
        Calendar c = Calendar.getInstance();
        Source source;
        int i = 0;
        for (Date t : config.getTimes()) {
            String time = dstFormat.format(t);
            c.setTime(t);
            if (config.getTimes().size() == ++i && "24-00".equals(time)) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            bean = new DataBean();
            for (int j = 0; j < sourceList.size(); j++) {
                source = sourceList.get(j);
                BigDecimal beginConsumingPower = source.get(beginConsumingTime).multiply(correct);
                BigDecimal beginConsuming = source.getBeginConsuming();
                BigDecimal currentPower = source.get(c.getTime()).multiply(correct);
                BigDecimal consuming = beginConsuming.subtract(beginConsumingPower).add(currentPower);
                System.out.println(consuming.doubleValue());
                bean.getVal()[j] = consuming.doubleValue();
                bean.getValC()[j] = consuming.multiply(k).doubleValue();
                bean.setTime(dstFormat.format(c.getTime()));
            }
            dataList.add(bean);
        }
    }

}
