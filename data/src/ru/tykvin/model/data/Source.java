package ru.tykvin.model.data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Source {

    private Date date;
    private String number;
    private SimpleDateFormat headFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss"); //18.12.2014 17:07:45
    private SimpleDateFormat consumingFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm"); //18.12.2014 17:07:45
    private Map<Date, BigDecimal> map = new HashMap<Date, BigDecimal>();
    private NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
    private BigDecimal sum = BigDecimal.valueOf(0);
    private BigDecimal beginConsuming = BigDecimal.valueOf(0);

    public Source(File elementAt) throws Exception {
        if (!elementAt.exists()) {
            throw new Exception("Файл " + elementAt + " не найден.");
        }
        try {
            parse(elementAt);
        } catch (ParseException e) {
            throw new Exception(String.format(e.getMessage(), elementAt));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new Exception("Файл " + elementAt + " имеет неверный фолрмат.");
        }
    }

    private void parse(File elementAt) throws ParserConfigurationException, SAXException, IOException, ParseException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
        Document doc = dbBuilder.parse(elementAt);
        doc.getDocumentElement().normalize();
        NodeList nList = null;
        Node nNode = null;
        NodeList rows = null;
        int i = 0;
        
        { // Получим первый лист
            nList = doc.getElementsByTagName("ss:Worksheet");
            nNode = getFirstNode(nList);
        }
        
        { // Получим таблицу (список строк)
            nNode = getFirstNode(nNode);
        }
        
        { // Получим первую строку
            rows = nNode.getChildNodes();
            nNode = getFirstNode(rows);
            
            { // Получим последнюю ячейку строки (В ней должна быть дата снятия показаний)
                nNode = getLastNode(nNode);
                try {
                    parseDate(getValue(getFirstNode(nNode)));
                } catch (Exception e) {
                    throw new ParseException("Не смог найти данные в файле %s", 0);
                }
            }
        }
        
        { // Перебираем все строки
            Node row = null;
            sum = BigDecimal.valueOf(0);
            //            Calendar c = Calendar.getInstance();
            //            c.clear();
            //            System.out.println(c.getTime() + " : " + sum.doubleValue());
            //            map.put(c.getTime(), sum);
            for (i = 0; i < rows.getLength(); row = getNode(rows.item(i)), i++) {
            	if (row != null && row.getNodeType() != Node.TEXT_NODE) {
	                parseNumber(row.getChildNodes());
	                parseConsuming(row.getChildNodes());
            	}
            }
        }

    }

    private Node getLastNode(Node nNode) throws ParseException {
		return getLastNode(nNode.getChildNodes());
	}

	private String getValue(Node node) {
    	Element element = (Element) node;
		String result = element.getTextContent();
		return result.trim();
	}

	private Node getLastNode(NodeList nList) throws ParseException {
    	Node nNode = null;
		int i = nList.getLength();
        do {
            nNode = getNode(nList.item(--i));
        } while (nNode.getNodeType() == Node.TEXT_NODE);
		return nNode;
	}

	private Node getFirstNode(NodeList nList) throws ParseException {
        Node nNode = null;
        int i = 0;
        do {
            nNode = getNode(nList.item(i++));
        } while (nNode.getNodeType() == Node.TEXT_NODE);
        return nNode;
	}
	

    private Node getNextNode(Node node) {
        Node nNode = node;
        do {
            nNode = nNode.getNextSibling();
        } while (nNode != null && nNode.getNodeType() == Node.TEXT_NODE);
        return nNode;
	}

	private Node getNode(Node item) throws ParseException {
        if (item == null)
            throw new ParseException("Не смог найти данные в файле %s", 0);
        return item;
    }

    private void parseConsuming(NodeList row) throws ParseException {
        if (row.getLength() > 0) {
            Node node = getFirstNode(row);
            try {
            	Node firstNode = getFirstNode(node);
                String dateValue = getValue(firstNode);
                if (dateValue.contains("-")) {
                    date = getRangeDate(dateValue);
                } else {
                    date = getSimpleDate(dateValue);
                }
                String str = getValue(getNextNode(node)).replaceAll(",", ".");
                BigDecimal value = BigDecimal.valueOf(Double.parseDouble(str));
                sum = sum.add(value);
                map.put(date, sum);
                System.out.println(date + " : " + sum);
            } catch (DOMException | ParseException e) {
            }
        }
    }

    // начальное = 9 утра
    private Date getSimpleDate(String dateValue) throws ParseException {
        return prepareDate(consumingFormat.parse(dateValue));
    }

    private Date prepareDate(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        Calendar result = Calendar.getInstance();
        result.clear();
        result.add(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
        result.add(Calendar.MINUTE, c.get(Calendar.MINUTE));
        while (map.containsKey(result.getTime())) {
            result.add(Calendar.DAY_OF_MONTH, 1);
        }
        result.add(Calendar.MINUTE, -30);
        return result.getTime();
    }

    private Date getRangeDate(String dateValue) throws ParseException {
        dateValue = dateValue.split("-")[0].trim();
        date = consumingFormat.parse(dateValue);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, 30);
        return prepareDate(c.getTime());
    }

    private Node getFirstNode(Node node) throws ParseException {
		return getFirstNode(node.getChildNodes());
	}

	private void parseNumber(NodeList row) throws ParseException {
        if (number == null) {
            if (row.getLength() > 0) {
                Node node = getFirstNode(row);
                if ("Заводской номер".equals(getValue(getFirstNode(node.getChildNodes())))) {
                	number = getValue(getFirstNode(getNextNode(node).getChildNodes()));
                }
            }
        }
    }

	private void parseDate(String nodeValue) throws ParseException {
        date = headFormat.parse(nodeValue); 
    }

    public String getNumber() {
        return number;
    }

    public Map<Date, BigDecimal> getConsumings() {
        return map;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getSum() {
		return sum;
	}

    public void setBeginConsuming(BigDecimal consuming) {
        this.beginConsuming = consuming;
    }

    public BigDecimal getBeginConsuming() {
        return beginConsuming;
	}

    public BigDecimal get(Date time) {
        return getConsumings().get(time);
    }

}
