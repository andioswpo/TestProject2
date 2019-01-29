package com.gf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class Starter extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication springApplication = new SpringApplication(Starter.class);
    	springApplication.addListeners(new ApplicationStartup());
    	springApplication.run(args);
    }
}
