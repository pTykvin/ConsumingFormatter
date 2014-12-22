package ru.tykvin.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

public class MainSourcer {

    public List<Source> load(DefaultListModel<File> defaultListModel) throws Exception {
    	List<Source> result = new ArrayList<Source>();
        for (int i = 0; i < defaultListModel.getSize(); i++) {
            Source source = new Source(defaultListModel.getElementAt(i));
            result.add(source);
        }
        return result;
    }

}
