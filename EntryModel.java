package uiperformanceutilities.model;

public class EntryModel {

	private String entryName="";
	private String entryType="";
	private String initiatorType="";
	private String duration="";
	private String transferSize="";
	private String startTime="";
	private String endTime="";
	
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public String getInitiatorType() {
		return initiatorType;
	}
	public void setInitiatorType(String initiatorType) {
		this.initiatorType = initiatorType;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTransferSize() {
		return transferSize;
	}
	public void setTransferSize(String transferSize) {
		this.transferSize = transferSize;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	
}