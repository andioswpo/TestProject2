package com.gf.weixin.http;

import java.util.ArrayList;
import java.util.List;

import com.gf.weixin.Util;
import com.gf.weixin.bean.MenuInfo;


public class MenuCreateRequest extends BaseRequest{
	private String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private List<MenuInfo> menuList = new ArrayList<MenuInfo>();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addMenuList(MenuInfo mi)
	{
		if(menuList == null)
			menuList = new ArrayList<MenuInfo>();
		menuList.add(mi);
	}
	
	public void setMenuList(List<MenuInfo> menuList)
	{
		this.menuList = menuList;
	}
	
	public List<MenuInfo> getMenuList()
	{
		return menuList;
	}
	
	public void init() {
		String url = getUrl();
		url = Util.getFormatString(url, "ACCESS_TOKEN", this.getAccessToken());
		setUrl(url);
	}
	
	public String getJson()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{\r\n");
		sb.append("\"button\":[\r\n");
		StringBuffer sb2 = new StringBuffer();
		String hostUrl = "http://www.eazequick.com";
		for(MenuInfo mi:menuList)
		{
			List<MenuInfo> subMiList = mi.getSub_button();
			if(subMiList == null || subMiList.size() == 0)
			{
				sb2.append("{\r\n");
				sb2.append("\"type\":\""+mi.getType().toLowerCase()+"\",\r\n");
				sb2.append("\"name\":\""+mi.getName()+"\",\r\n");
				if("click".equalsIgnoreCase(Util.fmtStr(mi.getType())))
					sb2.append("\"key\":\""+mi.getKey()+"\"\r\n");
				if("view".equalsIgnoreCase(Util.fmtStr(mi.getType())))
				{
					//认证号授权获取openid
					//如果是外部主机，不需要转换url
					String murl = mi.getUrl();
					if(murl.indexOf(hostUrl)>=0)
					{
						//murl = Util.getWeixinOauth2(murl, cust);
					}
					sb2.append("\"url\":\""+murl+"\"\r\n");
				}
				sb2.append("},");
			}
			else
			{
				sb2.append("{\r\n");
				sb2.append("\"name\":\""+mi.getName()+"\",\r\n");
				if(subMiList != null && subMiList.size()>0)
				{
					sb2.append("\"sub_button\":[\r\n");
					StringBuffer sb3 = new StringBuffer();
					for(MenuInfo subMi:subMiList)
					{
						sb3.append("{\r\n");
						sb3.append("\"type\":\""+subMi.getType().toLowerCase()+"\",\r\n");
						sb3.append("\"name\":\""+subMi.getName()+"\",\r\n");
						if("click".equalsIgnoreCase(Util.fmtStr(subMi.getType())))
							sb3.append("\"key\":\""+subMi.getKey()+"\"\r\n");
						if("view".equalsIgnoreCase(Util.fmtStr(subMi.getType())))
						{
							//认证号授权获取openid
							//如果是外部主机，不需要转换url
							String murl = subMi.getUrl();
							if(murl.indexOf(hostUrl)>=0)
							{
								//murl = Util.getWeixinOauth2(murl, cust);
							}
							sb3.append("\"url\":\""+murl+"\"\r\n");
						}
						sb3.append("},");
					}
					String str3 = sb3.toString();
					if(str3.endsWith(","))
						str3 = str3.substring(0,str3.length()-1);
					sb2.append(str3);
					sb2.append("]\r\n");
				}
				sb2.append("},");
			}
		}
		String str2 = sb2.toString();
		if(str2.endsWith(","))
			str2 = str2.substring(0,str2.length()-1);
		sb.append(str2);
		sb.append("]\r\n");
		sb.append("}\r\n");
		
		String str = "{"+
		"    \"button\": ["+
		"        {"+
		"            \"type\": \"click\", "+
		"            \"name\": \"今日歌曲\", "+
		"            \"key\": \"V1001_TODAY_MUSIC\""+
		"        }, "+
		"        {"+
		"            \"type\": \"click\", "+
		"            \"name\": \"歌手简介\", "+
		"            \"key\": \"V1001_TODAY_SINGER\""+
		"        }, "+
		"        {"+
		"            \"name\": \"菜单\", "+
		"            \"sub_button\": ["+
		"                {"+
		"                    \"type\": \"view\", "+
		"                    \"name\": \"搜索\", "+
		"                    \"url\": \"http://www.soso.com/\""+
		"                }, "+
		"                {"+
		"                    \"type\": \"view\", "+
		"                    \"name\": \"视频\", "+
		"                    \"url\": \"http://v.qq.com/\""+
		"                }, "+
		"                {"+
		"                    \"type\": \"click\", "+
		"                    \"name\": \"赞一下我们\", "+
		"                    \"key\": \"V1001_GOOD\""+
		"                }"+
		"            ]"+
		"        }"+
		"    ]"+
		"}";
		
		return sb.toString();
	}
}
