package com.gf.weixin.xml;

public class EventInfo {
	private String toUserName = null;//开发者微信号
	private String fromUserName = null;//发送方帐号（一个OpenID）
	private java.sql.Timestamp createTime = null;
	private String msgType = null;
	private String event = null;
	private String eventKey = null;
	private String ticket = null;
	private String latitude = null;
	private String longitude = null;
	private String precision = null;
	
	public String toString()
	{
		return "WXEventInfo[toUserName="+toUserName+",fromUserName="+fromUserName+
				",createTime="+createTime+",msgType="+msgType+",event="+event+
				",eventKey="+eventKey+",ticket="+ticket+",latitude="+latitude+
				",longitude="+longitude+",precision="+precision+"]";
	}


	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public java.sql.Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
}
