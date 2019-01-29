package com.gf.weixin.bean;

public class UserListInfo {
	public Integer total = 0;
	public Integer count = 0;
	public UserListDataInfo data;
	private String next_openid = null;
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}

	public UserListDataInfo getData() {
		return data;
	}

	public void setData(UserListDataInfo data) {
		this.data = data;
	}
	
	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}	

	public String toString()
	{
		return "WXUserListInfo[total="+total+",count="+count+",data="+data+"]";
	}
}
