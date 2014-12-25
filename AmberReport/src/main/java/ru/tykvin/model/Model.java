package ru.tykvin.model;

import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;

import ru.tykvin.loader.Config;
import ru.tykvin.model.data.Source;

public class Model {

    DefaultListModel<File> fileList = new DefaultListModel<File>();
    private Config config;
    private int selectIndex;
    private List<Source> source;

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
    
    public Config getConfig() {
        return this.config;
    }

    public void fireSelectIndexAction(int index) {
        this.selectIndex = index;
    }

    public void fireParseResult(List<Source> data) {
        this.source = data;
    }

    public List<Source> getSource() {
        return source;
    }
    
    public Integer getCount() {
    	return 3;
    }

}

