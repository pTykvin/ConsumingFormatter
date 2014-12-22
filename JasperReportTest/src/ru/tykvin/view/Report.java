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

        JasperReport report = (JasperReport) JRLoader.loadObject(new File("templates/template.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanColDataSource);        

        JFrame frame = new JFrame("Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		for (int i = 0; i < source.size(); i++) {
			parameters.put("COUNTER"+(i+1),source.get(i).getNumber());
		}
		
	}

	private void fillDataSet(Config config, List<Source> sourceList) throws ParseException {
		SimpleDateFormat dstFormat = new SimpleDateFormat(Constants.timesFormat);
		int i = 0;
		DataBean bean;
		Source source = sourceList.get(0);
		Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		for (Date t : config.getTimes()) {
			String time = dstFormat.format(t);
			bean = new DataBean();
			if (i++ == 0 && "24-00".equals(time)) {
				bean.setTime("0-00");
				bean.getVal()[0] = source.getBeginConsuming();
				dataList.add(bean);	
			} else {				
				c.setTime(source.getDate());
				c1.setTime(t);
				c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), 0);
				if ("24-00".equals(time)) {
					c.add(Calendar.DAY_OF_MONTH, 1);
				}
				Double val = source.getConsumings().get(c.getTime());
				System.out.println(c.getTime() + " >> " + (val == null ? 0 : val));
				bean.getVal()[0] = val / 10000;		
				bean.setTime(dstFormat.format(c1.getTime()));
			}
			dataList.add(bean);			
		}
	}
	
}

