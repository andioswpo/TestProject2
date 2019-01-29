package com.gf.weixin;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronTokenTask {
	@Value("${weixin.appid}")
	private String appId;
	@Value("${weixin.appsecret}")
	private String appSecret;
	@Autowired
	private WeiXinClient client;
	
	@Scheduled(cron="0/10 * * ? * *")
	public void refreshToken()
	{
		//System.out.println("refreshToken======"+new Timestamp(System.currentTimeMillis()));
		client.refreshToken(appId, appSecret);
	}
}
