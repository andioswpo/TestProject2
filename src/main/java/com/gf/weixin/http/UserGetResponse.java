package com.gf.weixin.http;

import com.gf.weixin.bean.UserInfo;

public class UserGetResponse{
	private UserInfo user = new UserInfo();
	
	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}
}
