package com.gf.weixin.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("gf_wxorder")
public class WxOrderInfo {
	private String id;
	private String no;
	private Timestamp createDate = new Timestamp(System.currentTimeMillis());
	//订单微信用户ID
	private String openId;
	//订单微信用户昵称
	private String wxName = null;
	//商品名称
	private String prdtName = null;
	//订单总金额单位(分)
	private Integer allTotal = 0;//allTotal=total+transportFree
	//支付金额单位(分)
	private Integer total = 0;
	//运费
	private Integer transportFree = 0;
	//用户名称
	private String userName;
	//用户联系电话
	private String telNo;
	//用户邮编
	private String postalCode;
	//用户所在身份
	private String provice;
	//用户所在地市
	private String city;
	//用户所在区县
	private String area;
	//用户详细地址
	private String addr;
	//订单状态
	private String status = "draft";//draft草稿, finish支付完成,refund退款，destory作废

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public Integer getAllTotal() {
		return allTotal;
	}
	public void setAllTotal(Integer allTotal) {
		this.allTotal = allTotal;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getTransportFree() {
		return transportFree;
	}
	public void setTransportFree(Integer transportFree) {
		this.transportFree = transportFree;
	}
	public String getPrdtName() {
		return prdtName;
	}
	public void setPrdtName(String prdtName) {
		this.prdtName = prdtName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
