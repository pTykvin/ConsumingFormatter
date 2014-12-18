package ru.tykvin;

import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import ru.tykvin.view.FilePanel;

public class View extends JFrame {
    private static final long serialVersionUID = 1L;
    private Controller controller;
    private Box fileList;
    private JFileChooser fileChooser;
    private Model model;

    public void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Калькулятор показаний счетчиков");
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JButton button = new JButton("Добавить файл с показаниями");
        button.addActionListener(controller.getAddFileAction());
        menuBar.add(button);

        fileList = new Box(BoxLayout.Y_AXIS);

        getContentPane().add(fileList);

        fileChooser = new JFileChooser();
        FileFilter xlsFilter = new FileNameExtensionFilter("Microsoft Excel Document (.xls или .xlsx)", "xls", "xlsx");
        fileChooser.addChoosableFileFilter(xlsFilter);
        fileChooser.setFileFilter(xlsFilter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        setVisible(true);

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public File requestFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void update() {

    }

    private void updateFileList() {
        fileList.removeAll();
        FilePanel filePanel = null;
        for (File f : model.getFileList()) {
            filePanel = new FilePanel(f);
            fileList.add(filePanel);
        }
        fileList.revalidate();
        fileList.repaint();
    }

    public void updateView() {
        updateFileList();
    }

}
