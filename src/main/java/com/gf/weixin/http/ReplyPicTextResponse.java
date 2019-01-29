package com.gf.weixin.http;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.gf.weixin.Util;
import com.gf.weixin.bean.WxPostMsgItemInfo;



public class ReplyPicTextResponse{
	protected String fromUserName = null;
	protected String toUserName = null;
	private List<PicTextInfo> ptList = new ArrayList<PicTextInfo>();
	private List<WxPostMsgItemInfo> msgList = new ArrayList<WxPostMsgItemInfo>();
	private String param = null;
	
	
	public ReplyPicTextResponse(String fromUserName, String toUserName,String param) {
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
		this.param = Util.fmtStr(param);
	}
	
	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
	public List<WxPostMsgItemInfo> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<WxPostMsgItemInfo> msgList) {
		this.msgList = msgList;
		String hostUrl = "";
		for(WxPostMsgItemInfo item:msgList)
		{
			PicTextInfo pti = new PicTextInfo();
			pti.setTitle(item.getTitle());
			pti.setDescription(item.getDescription());
			String url = Util.fmtStr(item.getUrl());
			System.out.println("url="+url);
			if(url.startsWith("http:"))
			{
				if(url.indexOf("login.action")>0)
				{
					if(url.indexOf("?")>=0)
						url = url + "&"+param;
					else
						url = url + "?"+param;
				}
				else
				{
					if(url.indexOf("?")>=0)
						url = url + "&openid="+Util.fmtStr(toUserName)+"&popenid=&uuid=&puuid=&"+param;
					else
						url = url + "?openid="+Util.fmtStr(toUserName)+"&popenid=&uuid=&puuid=&"+param;
				}
				//认证号授权获取openid
				//如果是外部主机，不需要转换url
				if(url.indexOf(hostUrl)>=0)
				{
					//url = Util.getWeixinOauth2(url, cust);
				}
				pti.setUrl(url);//连接外部URL
			}
			
			String picUrl = hostUrl + "/downimg?cmd=image&id="+item.getPicUrl();
			pti.setPicUrl(picUrl);
			System.out.println("ReplyPicTextResponse.picUrl="+picUrl);
			System.out.println("ReplyPicTextResponse.url="+pti.getUrl());
			addPicText(pti);
		}
	}

	public List<PicTextInfo> getPtList() {
		return ptList;
	}

	public void setPtList(List<PicTextInfo> ptList) {
		this.ptList = ptList;
	}
	
	public void addPicText(PicTextInfo pt)
	{
		if(ptList == null)
			ptList = new ArrayList<PicTextInfo>();
		ptList.add(pt);
	}

	public String getText()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>\r\n");
		sb.append("<ToUserName><![CDATA["+toUserName+"]]></ToUserName>\r\n");
		sb.append("<FromUserName><![CDATA["+fromUserName+"]]></FromUserName>\r\n");
		sb.append("<CreateTime>"+new java.util.Date().getTime()+"</CreateTime>\r\n");
		sb.append("<MsgType><![CDATA[news]]></MsgType>\r\n");
		sb.append("<ArticleCount>"+ptList.size()+"</ArticleCount>\r\n");
		sb.append("<Articles>\r\n");
		for(PicTextInfo pti:ptList)
		{
			String title = pti.getTitle();
			String description = pti.getDescription();
			String picurl = pti.getPicUrl();
			String url = pti.getUrl();
			sb.append("<item>\r\n");
			sb.append("<Title><![CDATA["+title+"]]></Title>\r\n");
			sb.append("<Description><![CDATA["+description+"]]></Description>\r\n");
			sb.append("<PicUrl><![CDATA["+picurl+"]]></PicUrl>\r\n");
			sb.append("<Url><![CDATA["+url+"]]></Url>\r\n");
			sb.append("</item>\r\n");
		}
		sb.append("</Articles>\r\n");
		sb.append("</xml>\r\n");
		return sb.toString();
	}
	
}
