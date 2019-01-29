package com.gf.weixin.http;

import java.util.ArrayList;
import java.util.List;

import com.gf.weixin.Util;

public class MenuDeleteRequest extends BaseRequest{
	private String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void init() {
		String url = getUrl();
		url = Util.getFormatString(url, "ACCESS_TOKEN", this.getAccessToken());
		setUrl(url);
	}
}
