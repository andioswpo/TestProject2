package com.gf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronTask {
	//每小时
	@Scheduled(cron="0/5 * * ? * *")
	public void timerToNow(){
		try
		{
			//System.out.println("2019-01-24 ############now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
