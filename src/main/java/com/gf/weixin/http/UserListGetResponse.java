package com.gf.weixin.http;

import com.gf.weixin.bean.UserListInfo;

public class UserListGetResponse{
	private UserListInfo userList = new UserListInfo();
	
	public UserListInfo getUserList() {
		return userList;
	}

	public void setUserList(UserListInfo userList) {
		this.userList = userList;
	}
}
