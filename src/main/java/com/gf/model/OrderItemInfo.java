package com.gf.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItemInfo implements Serializable{
	private String id = null;
	private String prdtId = null;
	private Integer num = null;
	private BigDecimal price = null;
	private BigDecimal amount = null;
	private String orderId = null;
	private String prdtName = null;
	private String uuid = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPrdtId() {
		return prdtId;
	}
	public void setPrdtId(String prdtId) {
		this.prdtId = prdtId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPrdtName() {
		return prdtName;
	}
	public void setPrdtName(String prdtName) {
		this.prdtName = prdtName;
	}
	public String getUuid() {
		return id;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}	
}
