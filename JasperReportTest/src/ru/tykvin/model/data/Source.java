package ru.tykvin.model.data;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private Map<Date, Double> map = new HashMap<Date, Double>();
    private NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
	private double sum;

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
            sum = 0;
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
				Date date = consumingFormat.parse(getValue(firstNode));				
			    Number number = format.parse(getValue(getNextNode(node)));			    
                double value = Math.floor(number.doubleValue() * 10000);
			    sum += value;
                map.put(date, sum);
                System.out.println(date + " : " + sum);
            } catch (DOMException | ParseException e) {
            }
        }
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

    public Map<Date, Double> getConsumings() {
        return map;
    }

    public Date getDate() {
        return date;
    }

	public double getSum() {
		return sum;
	}

	public Double getBeginConsuming() {
        return 5000d;
	}

    public Double get(Date time) {
        return getConsumings().get(time) / 10000;
    }

}
