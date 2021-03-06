package ru.tykvin.loader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private ArrayList<Date> times;

	@XmlElement
	private Integer coefficient;

	@XmlElement
	private String code;

	@XmlElement
	private String contract;

	@XmlElement
	private String company;
	
	@XmlElement
	private String type;

    @XmlElement
    private String maker;

    @XmlElement
    private String correction;

	@XmlElement
	public void setTimesFormat(String timesFormat) {
		Constants.timesFormat = timesFormat;
	}

	/**
	 * Коэффициент трасформации
	 * 
	 * @return
	 */
    public BigDecimal getCoefficient() {
        return BigDecimal.valueOf(coefficient);
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	/**
	 * Номер потребителя
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Номер договора
	 * 
	 * @return
	 */

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * Наименование компании
	 * 
	 * @return
	 */
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Тип счетчика
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Список временных периодов для отчета 
	 */
	public List<Date> getTimes() {
		return times;
	}
	
	public void setType(String type) {
		this.type = type;
	}

    /**
     * Исполнитель
     * 
     * @return
     */
    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    /**
     * Коэффициент для коррекции показаний
     * 
     * @return
     */
    public BigDecimal getCorrection() {
        return BigDecimal.valueOf(Double.valueOf(correction));
    }

    public void setCorrection(String correction) {
        this.correction = correction.replace(",", ".");
    }

}