package ru.tykvin.loader;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Config {

    @XmlElement(name = "times")
    @XmlJavaTypeAdapter(TimesAdapter.class)
    private ArrayList<String> times;
    
    @XmlElement
    private String timesFormat;

    @XmlElement
    private String coefficient;

    @XmlElement
    private String code;

    @XmlElement
    private String contract;

    @XmlElement
    private String company;

    public String getTimesFormat() {
        return timesFormat;
    }

    public void setTimesFormat(String timesFormat) {
        this.timesFormat = timesFormat;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    
}