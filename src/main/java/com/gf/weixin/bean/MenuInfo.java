package com.gf.weixin.bean;

import java.util.ArrayList;
import java.util.List;

public class MenuInfo {
	private String type = null;
	private String name = null;
	private String key = null;
	private String url = null;
	private List<MenuInfo> sub_button = new ArrayList<MenuInfo>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<MenuInfo> getSub_button()
	{
		return sub_button;
	}
	public void setSub_button(List<MenuInfo> sub_button)
	{
		this.sub_button = sub_button;
	}
}
