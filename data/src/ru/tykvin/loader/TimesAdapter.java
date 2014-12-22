package ru.tykvin.loader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimesAdapter extends XmlAdapter<String, ArrayList<Date>> {
    private SimpleDateFormat format = new SimpleDateFormat(Constants.timesFormat);

    @Override
    public ArrayList<Date> unmarshal(String value) throws Exception {
        String[] arr = value.split(",");
        ArrayList<Date> result = new ArrayList<Date>();
        for (String s : arr) {
            result.add(format.parse(s));
        }
        return result;
    }

    @Override
    public String marshal(ArrayList<Date> list) throws Exception {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result = String.format("%s%s", format.format(list.get(i)), (i == (list.size() - 1) ? "" : ","));
        }
        return result;
    }

}
