package com.gf.weixin;

import java.io.InputStream;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class WeiXinHttpClient {
	public String getUrl(String url)
	{
		try
		{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();
			String str = new String(FileCopyUtils.copyToByteArray(is));
			return str;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public String postUrl(String url,String body)
	{
		try
		{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			HttpEntity entity = new StringEntity(body,ContentType.create("text/plain", Consts.UTF_8));
			httppost.setEntity(entity);
			CloseableHttpResponse response = httpclient.execute(httppost);
			InputStream is = response.getEntity().getContent();
			String str = new String(FileCopyUtils.copyToByteArray(is));
			return str;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		WeiXinHttpClient hc = new WeiXinHttpClient();
		hc.getUrl("http://www.baidu.com/s?wd=Java");
	}
}
