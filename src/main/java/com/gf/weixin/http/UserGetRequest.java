package com.gf.weixin.http;

import com.gf.weixin.Util;

public class UserGetRequest  extends BaseRequest{
	private String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
	private String openId = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public void init() {
		url = Util.getFormatString(url, "ACCESS_TOKEN", this.getAccessToken());
		url = Util.getFormatString(url, "OPENID", openId);
	}
}
