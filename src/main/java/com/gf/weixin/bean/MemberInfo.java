package com.gf.weixin.bean;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("hk_member")
public class MemberInfo implements java.io.Serializable {
	@TableId(type=IdType.UUID)
	private String id;//主键
	private String name;//真实姓名
	private String mobile;//手机号
	private String sex;//性别
	private Date regDate = new Date();//注册日期
	private Date bornDate = new Date();//出生日期
	private String userId;//关联办公系统用户ID
	private String wxName;//微信昵称
	private String wxOpenId;//微信OpenId
	private String wxCity;	//微信用户所在城市
	private String wxProvince;//微信用户所在省份
	private String wxCountry;//微信用户所在区县
	private String wxSubscribe;//是否关注
	private Long wxSubscribeTime;//关注时间
	private Integer wxScanCode;//微信二维码参数
	private Integer wxGroupId = 0;//微信分组  
	private String qq;//QQ
	private String address;//详细地址
	private String description;//描述
	private String headimgurl;//头像
	private String flag = "hk";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public Date getBornDate() {
		return bornDate;
	}
	public void setBornDate(Date bornDate) {
		this.bornDate = bornDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}
	public String getWxOpenId() {
		return wxOpenId;
	}
	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}
	public String getWxCity() {
		return wxCity;
	}
	public void setWxCity(String wxCity) {
		this.wxCity = wxCity;
	}
	public String getWxProvince() {
		return wxProvince;
	}
	public void setWxProvince(String wxProvince) {
		this.wxProvince = wxProvince;
	}
	public String getWxCountry() {
		return wxCountry;
	}
	public void setWxCountry(String wxCountry) {
		this.wxCountry = wxCountry;
	}
	public String getWxSubscribe() {
		return wxSubscribe;
	}
	public void setWxSubscribe(String wxSubscribe) {
		this.wxSubscribe = wxSubscribe;
	}
	public Long getWxSubscribeTime() {
		return wxSubscribeTime;
	}
	public void setWxSubscribeTime(Long wxSubscribeTime) {
		this.wxSubscribeTime = wxSubscribeTime;
	}
	public Integer getWxScanCode() {
		return wxScanCode;
	}
	public void setWxScanCode(Integer wxScanCode) {
		this.wxScanCode = wxScanCode;
	}
	public Integer getWxGroupId() {
		return wxGroupId;
	}
	public void setWxGroupId(Integer wxGroupId) {
		this.wxGroupId = wxGroupId;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}