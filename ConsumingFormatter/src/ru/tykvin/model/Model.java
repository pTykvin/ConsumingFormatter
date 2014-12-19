package ru.tykvin.model;

import java.io.File;
import java.util.Date;
import java.util.Map;

import javax.swing.DefaultListModel;

import ru.tykvin.loader.Config;

public class Model {

    DefaultListModel<File> fileList = new DefaultListModel<File>();
    private Config config;
    private int selectIndex;
    private Map<String, Map<Date, Double>> source;

    public void fireAddFile(File file) {
        if (!fileList.contains(file)) {
            fileList.addElement(file);
        }
    }

    public void fireRemoveFile() {
        if (selectIndex >= 0) {
            fileList.remove(selectIndex);
        }
    }

    public DefaultListModel<File> getListModel() {
        return fileList;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getCompanyName() {
        return config.getCompany();
    }

    public void fireSelectIndexAction(int index) {
        this.selectIndex = index;
    }

    public void fireParseResult(Map<String, Map<Date, Double>> data) {
        this.source = data;
    }

    public Map<String, Map<Date, Double>> getSource() {
        return source;
    }

}
