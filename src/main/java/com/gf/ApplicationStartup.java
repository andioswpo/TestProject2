package com.gf;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.Util;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("Run to here");
		
		IOrgModel orgmodel = event.getApplicationContext().getBean(IOrgModel.class);
		orgmodel.initDb();
		orgmodel.initFunc();
		Util.setCtx(event.getApplicationContext());
	}

}
