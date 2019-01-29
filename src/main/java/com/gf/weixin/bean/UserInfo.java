package com.gf.weixin.bean;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {
	private Integer subscribe;
	private String openid = null;
	private String nickname = null;
	private String sex = null;
	private String language = null;
	private String city = null;
	private String province = null;
	private String country = null;
	private String headimgurl = null;
	private Long subscribe_time;
	private List tagid_list = new ArrayList();

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Long getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Long subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public List getTagid_list() {
		return tagid_list;
	}
	
	public void setTagid_list(List tagid_list) {
		this.tagid_list = tagid_list;
	}

	public String toString()
	{
		return "WXUserInfo[openid="+openid+",nickname="+nickname+",city="+city+"]";
	}
}
