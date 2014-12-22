package ru.tykvin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JRViewer;
import ru.tykvin.controller.Controller;
import ru.tykvin.model.Model;

public class View extends JFrame {
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private JFileChooser fileChooser;
	@SuppressWarnings("unused")
	private Model model;

	int i = 0;
	private DefaultListModel<File> listModel;
	private JList<File> list;
	private JButton removeButton;
	private JButton beginButton;

	public void init() {

		Dimension size = new Dimension(500, 200);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		list = new JList<File>(listModel);
		list.setSelectedIndex(0);
		list.setFocusable(false);
		list.addListSelectionListener(controller.getSelectAction());
		setPreferredSize(size);

		mainPanel.add(new JScrollPane(list), BorderLayout.CENTER);

		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 3, 5, 0));
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		final JButton addButton = new JButton("Добавить");
		addButton.setFocusable(false);
		addButton.addActionListener(controller.getAddFileAction());
		buttonsPanel.add(addButton);

		removeButton = new JButton("Удалить");
		removeButton.setFocusable(false);
		removeButton.addActionListener(controller.getRemoveFileAction());
		removeButton.setEnabled(false);
		buttonsPanel.add(removeButton);

		beginButton = new JButton("Нвчать");
		beginButton.setFocusable(false);
		beginButton.addActionListener(controller.getBeginAction());
		beginButton.setEnabled(false);
		buttonsPanel.add(beginButton);

		getContentPane().add(mainPanel);

		setLocationRelativeTo(null);
		setTitle("Калькулятор показаний счетчиков");

		fileChooser = new JFileChooser();
		FileFilter xlsFilter = new FileNameExtensionFilter("XML Document", "xml");
		fileChooser.addChoosableFileFilter(xlsFilter);
		fileChooser.setFileFilter(xlsFilter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		pack();
		setVisible(true);

	}

	public void showReport() {
		String reportName = "M:/WS/JasperReportTest/src/test/ireport/template.jrxml"; // полный путь к отчету		
		List<Object> sourceList = new ArrayList<Object>();
		sourceList.add(model);
		JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(sourceList); //создаем коллекцию Jasper Report Bean Collection
		JasperPrint jp = null;
		try {
			jp = JasperFillManager.fillReport(reportName, new HashMap(), beanCollectionDataSource);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // заполняем датасет отчета данными из коллекции
		JRViewer jv = new JRViewer(jp); // компонент просмотра отчета
		jv.setFitWidthZoomRatio();
		JFrame reportFrame = new JFrame();
		
		reportFrame.getContentPane().add(jv);
		reportFrame.validate();
		reportFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		reportFrame.setVisible(true);		
	}

	public void fireRemoveFile() {
		if (listModel.size() == 0) {
			beginButton.setEnabled(false);
		}
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
		this.model = model;
		listModel = model.getListModel();
	}

	public File requestFile() {
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			beginButton.setEnabled(true);
			return fileChooser.getSelectedFile();
		}
		return null;
	}

	public int getSelectListIndex() {
		return list.getSelectedIndex();
	}

	public void fireSelectIndexAction(int index) {
		removeButton.setEnabled(list.getSelectedIndex() >= 0);
	}

	public void fireError(Exception e1) {
		JOptionPane.showMessageDialog(this, e1.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
	}

}
