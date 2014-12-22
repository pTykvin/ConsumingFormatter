package ru.tykvin.model.data;

public class DataBean {

	private String time = "";
	private String[] consuming = new String[]{"","","",""};
	private String[] consumingC = new String[]{"","","",""};
	
	public DataBean(String time, String[] consuming, String[] consumingC) {
		this.time = time;
		this.consuming = consuming;
		this.consumingC = consumingC;
	}
	
	public DataBean(String time) {
		this.time = time;
	}

	public String getConsuming1() {
		return consuming[0];
	}
	
	public String getConsuming2() {
		return consuming[1];
	}
	
	public String getConsuming3() {
		return consuming[2];
	}
	
	public String getConsuming4() {
		return consuming[3];
	}
	
	public String getConsumingC1() {
		return consumingC[0];
	}
	
	public String getConsumingC2() {
		return consumingC[1];
	}
	
	public String getConsumingC3() {
		return consumingC[2];
	}
	
	public String getConsumingC4() {
		return consumingC[3];
	}

	public String getName() {
		return "Jhon";
	}
	
	public int getAge() {
		return 12;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
