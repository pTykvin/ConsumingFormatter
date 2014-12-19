package ru.tykvin.model.data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Source {

    private Date date;
    private String number;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
    private Map<Date, Double> map = new HashMap<Date, Double>();

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
        
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = null;
        Node nNode = null;
        NodeList rows = null;
        int i = 0;
        
        { // Получим первый лист
            nList = doc.getElementsByTagName("ss:Worksheet");
            nNode = null;
            i = 0;
            do {
                nNode = getNode(nList.item(i++));
            } while (nNode.getNodeType() == Node.TEXT_NODE);
        }
        
        { // Получим таблицу (список строк)
            nList = nNode.getChildNodes();
            nNode = null;
            i = 0;
            do {
                nNode = getNode(nList.item(i++));
            } while (nNode.getNodeType() == Node.TEXT_NODE);
        }
        
        { // Получим первую строку
            rows = nNode.getChildNodes();
            i = 0;
            do {
                nNode = getNode(rows.item(i++));
            } while (nNode.getNodeType() == Node.TEXT_NODE);
            
            { // Получим последнюю ячейку строки (В ней должна быть дата снятия показаний)
                nNode = getNode(nNode.getLastChild());
                i = nNode.getChildNodes().getLength();
                do {
                    nNode = getNode(rows.item(--i));
                } while (nNode.getNodeType() == Node.TEXT_NODE);
                try {
                    parseDate(nNode.getNodeValue());
                } catch (Exception e) {
                    throw new ParseException("Не смог найти данные в файле %s", 0);
                }
            }
        }
        
        { // Перебираем все строки
            Node row = null;
            for (i = 0; i < rows.getLength(); row = getNode(rows.item(i)), i++) {
                parseNumber(row.getChildNodes());
                parseConsuming(row.getChildNodes());
            }
        }

    }

    private Node getNode(Node item) throws ParseException {
        if (item == null)
            throw new ParseException("Не смог найти данные в файле %s", 0);
        return item;
    }

    private void parseConsuming(NodeList row) throws ParseException {
        if (row.getLength() > 0) {
            Node node = getNode(row.item(0));
            try {
                Date date = format.parse(node.getFirstChild().getNodeValue());
                double value = Double.parseDouble(row.item(1).getFirstChild().getNodeValue());
                map.put(date, value);
            } catch (DOMException | ParseException e) {
            }
        }
    }

    private void parseNumber(NodeList row) throws ParseException {
        if (number == null) {
            if (row.getLength() > 0) {
                Node node = getNode(row.item(0));
                int i = 0;
                do {
                    node = getNode(row.item(i++));
                } while (node.getNodeType() == Node.TEXT_NODE);
                if ("Заводской номер".equals(getNode(node.getFirstChild()).getNodeValue())) {
                    number = getNode(getNode(node.getNextSibling()).getFirstChild()).getNodeValue();
                }
            }
        }
    }

    private void parseDate(String nodeValue) throws ParseException {
        date = format.parse(nodeValue);
    }

    public String getNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<Date, Double> getConsumings() {
        return map;
    }

    public Date getDate() {
        return date;
    }

}
