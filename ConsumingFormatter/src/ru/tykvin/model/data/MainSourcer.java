package ru.tykvin.model.data;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

public class MainSourcer {

    public Map<String, Map<Date, Double>> load(DefaultListModel<File> defaultListModel) throws Exception {
        Map<String, Map<Date, Double>> result = new HashMap<String, Map<Date, Double>>();
        for (int i = 0; i < defaultListModel.getSize(); i++) {
            Source source = new Source(defaultListModel.getElementAt(i));
            result.put(source.getNumber(), source.getConsumings());
        }
        return result;
    }

}
