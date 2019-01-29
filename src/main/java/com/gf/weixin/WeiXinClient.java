package com.gf.weixin;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gf.statusflow.UUID;
import com.gf.weixin.bean.AccessToken;
import com.gf.weixin.bean.UserInfo;
import com.gf.weixin.bean.WeiXinAccount;
import com.gf.weixin.service.AccountService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Component
public class WeiXinClient {
	private Logger log = LoggerFactory.getLogger(WeiXinClient.class);
	
	public static Map<String,String> wxMap = new HashMap<String,String>();
	
	@Autowired
	private WeiXinHttpClient client;
	@Autowired
	private AccountService acctServ;
	
	public String getCurrentToken()
	{
		Wrapper<WeiXinAccount> wrap = new EntityWrapper<WeiXinAccount>();
		List<WeiXinAccount> lst = acctServ.selectList(wrap);
		if(lst != null && lst.size()>=1)
		{
			WeiXinAccount acct = lst.get(0);
			return acct.getToken();
		}
		return null;
	}
	
	public void refreshToken(String appId,String appSecret)
	{
		try
		{
			//微信后台
			//String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
			String url = "http://eazequick.com/wxtoken/scancode=1&grant_type=client_credential&appid="+appId+"&secret="+appSecret;
			//log.debug("refreshToken url="+url);
			String json = client.getUrl(url);
			//log.debug("refreshToken json="+json);
			AccessToken token = (AccessToken)JSON.parseObject(json,AccessToken.class);
			//log.debug("refreshToken token="+token);
			//查询是否存在Token记录
			Wrapper<WeiXinAccount> wrap = new EntityWrapper<WeiXinAccount>();
			List<WeiXinAccount> lst = acctServ.selectList(wrap);
			//log.debug("refreshToken lst="+lst);
			if(lst != null && lst.size()>=1)
			{
				WeiXinAccount acct = lst.get(0);
				acct.setToken(token.getAccess_token());
				acct.setTs(new Timestamp(System.currentTimeMillis()));
				acctServ.updateById(acct);
			}
			else
			{
				WeiXinAccount acct = new WeiXinAccount();
				acct.setId(UUID.create("account"));
				acct.setAppId(appId);
				acct.setAppSecret(appSecret);
				acct.setToken(token.getAccess_token());
				acct.setTs(new Timestamp(System.currentTimeMillis()));
				acctServ.insert(acct);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Integer getScanCode(String scanCode)
	{
		Integer groupId = 0; 
		if(scanCode.startsWith("qrscene_"))
		{
			String tempStr = scanCode.substring("qrscene_".length());
			groupId = new Integer(tempStr);
		}
		else
			return new Integer(scanCode);
		return groupId;
	}
	
	/**
	 * 公众号被动推送文本消息
	 * @param out
	 * @param openId
	 * @param wxId
	 * @param msg
		<xml> <ToUserName>< ![CDATA[toUser] ]></ToUserName> 
		<FromUserName>< ![CDATA[fromUser] ]></FromUserName> 
		<CreateTime>12345678</CreateTime> <MsgType>< ![CDATA[text] ]></MsgType> 
		<Content>< ![CDATA[你好] ]></Content> </xml>	 
	 * 
	 */
	public String passiveSendText(String openId,String wxId,String msg)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>\r\n");
		sb.append("<ToUserName><![CDATA["+openId+"]]></ToUserName>\r\n");
		sb.append("<FromUserName><![CDATA["+wxId+"]]></FromUserName>\r\n");
		sb.append("<CreateTime>"+new java.util.Date().getTime()+"</CreateTime>\r\n");
		sb.append("<MsgType><![CDATA[text]]></MsgType>\r\n");
		sb.append("<Content><![CDATA["+msg+"]]></Content>\r\n");
		sb.append("</xml>\r\n");
		String rtn = sb.toString();
		return rtn;
	}
	
	/**
	 * 公众号主动推送消息给微信APP
	 * 时间范围必须是微信APP最后操作公众号内资源的48小时内
	 */
	public void sendTextMessage(String openId,String msg,String accessToken)
	{
		String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accessToken;
		String body = "{"+
				"\"touser\": \""+openId+"\", "+
				"\"msgtype\": \"text\", "+
				"\"text\": {"+
				"\"content\": \""+msg+"\""+
				"}"+
				"}";
		System.out.println(url);
		WeiXinHttpClient client = new WeiXinHttpClient();
		String rtn = client.postUrl(url, body);
		System.out.println("rtn="+rtn);
	}
	
	/**
	 * 根据关注者openId获取关注者信息
	 * @param args
	 */
	public UserInfo getUserByOpenId(String openId,String accessToken)
	{
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId;
		WeiXinHttpClient client = new WeiXinHttpClient();
		String json = client.getUrl(url);
		UserInfo ui = (UserInfo)JSONObject.parseObject(json, UserInfo.class);
		return ui;
	}
	
	/**
	 * 按照微信OAuth2规则封装URL
	 * @param args
	 */
	public String getOAuth2Url(String url)
	{
		url = java.net.URLEncoder.encode(url);
		String url2 = "http://eazequick.com/wxoauth2?url="+url;
		WeiXinHttpClient client = new WeiXinHttpClient();
		String rtn = client.getUrl(url2);		
		return rtn;
	}
	
	public static byte[] generateQrCode(String url)
	{
		try
		{
			Integer width = 100;
			Integer height = 100;
	        String format = "png";
	        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
	        BitMatrix bitMatrix = new MultiFormatWriter().encode(url,  
	                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        MatrixToImageWriter.writeToStream(bitMatrix, format, baos);
	        return baos.toByteArray();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void addScanOpenId(String uuid,String openid)
	{
		if(!wxMap.containsValue(openid))
		{
			wxMap.put("uuid", uuid);
			wxMap.put("openid", openid);
		}
		System.out.println("wxMap==="+wxMap);
	}
	
	public static void main(String[] args)
	{
		try
		{
			String json = "{\"access_token\":\"17_kFuE-O6g5llnXKNqANUwjrChcgI3K2RkWIwBTxUEkQFGk6DmLXxFH0eta7vza9xAPJAdnxYp-M3rQKm4LlwxNvqpVf9DdiB4da68Ej1MXOIFziyQnW3zPEMXTvfr-RfxbGIzKeFZ7Nn1WSbuJBMhAFAASD\",\"expires_in\":\"1548345600000\"}";
			AccessToken token = (AccessToken)JSON.parseObject(json,AccessToken.class);
			System.out.println("refreshToken token="+token.getExpires_in());
			
			WeiXinClient cli = new WeiXinClient();
			String openId = "ol1n0jmosU8qvxZasIUweZadAEcg";
			String accessToken = "18_eU8Bnyak3073Z4zQ-ENBYYs1FIKGm64zt2fyXzpKM-7KTZT1quHR1kLO4gpewNblPlDPniwpyPEUh2FBoRbT7lewcqNHKYU-aHjk4-l0aNWcThXxltE-gjDfZnljpLSaLehMPLeGnzLMP4wTRJDbABAZRL";
			String msg = "客服消息";
			//cli.sendTextMessage(openId, msg, accessToken);
			//cli.getUserByOpenId(openId, accessToken);
			
			cli.generateQrCode("http://group5.eazequick.com/login");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
