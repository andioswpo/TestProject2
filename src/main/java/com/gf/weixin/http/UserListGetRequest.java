package com.gf.weixin.http;

import com.gf.weixin.Util;

public class UserListGetRequest  extends BaseRequest{
	private String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
	private String nextOpenId = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getNextOpenId() {
		return nextOpenId;
	}

	public void setNextOpenId(String nextOpenId) {
		this.nextOpenId = nextOpenId;
	}
	
	public void init() {
		url = Util.getFormatString(url, "ACCESS_TOKEN", this.getAccessToken());
		url = Util.getFormatString(url, "NEXT_OPENID", nextOpenId);
	}
}
