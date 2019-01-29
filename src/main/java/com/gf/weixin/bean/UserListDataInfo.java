package com.gf.weixin.bean;

import java.util.ArrayList;
import java.util.List;

public class UserListDataInfo {
	private List<String> openid = new ArrayList<String>();
	
	
	public List<String> getOpenid() {
		return openid;
	}

	public void setOpenid(List<String> openid) {
		this.openid = openid;
	}

	public String toString()
	{
		return "WXUserListDataInfo[openid="+openid+"]";
	}
}
