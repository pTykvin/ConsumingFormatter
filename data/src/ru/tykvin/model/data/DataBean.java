package ru.tykvin.model.data;
public class DataBean {
    private Double[] val;
    private Double[] valC;
    private String time;
    
    {
        val = new Double[4];
        valC = new Double[4];
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {

    	this.time = time;
    }
    
	public Double[] getVal() {
		return val;
	}
	
	public void setVal(Double[] val) {
		this.val = val;
	}
	
	public Double getVal1() {
		return val[0];
	}
	
	public Double getVal2() {
		return val[1];
	}
	
	public Double getVal3() {
		return val[2];
	}
	
	public Double getVal4() {
		return val[3];
	}

    public Double getVal1C() {
        return valC[0];
    }

    public Double getVal2C() {
        return valC[1];
    }

    public Double getVal3C() {
        return valC[2];
    }

    public Double getVal4C() {
        return valC[3];
    }

    public Double[] getValC() {
        return valC;
    }

}