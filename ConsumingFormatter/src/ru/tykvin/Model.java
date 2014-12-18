package ru.tykvin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Model {

    List<File> fileList = new ArrayList<File>();

    public void addFile(File requestFile) {
        fileList.add(requestFile);
    }

    public List<File> getFileList() {
        return fileList;
    }

}
