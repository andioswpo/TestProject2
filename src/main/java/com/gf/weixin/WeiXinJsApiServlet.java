package com.gf.weixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.gf.weixin.bean.PayInfo;

//@WebServlet(urlPatterns="/wxjsapi.servlet",loadOnStartup=0)
public class WeiXinJsApiServlet extends HttpServlet {
	private Logger log = LoggerFactory.getLogger(WeiXinJsApiServlet.class);
	private static final long serialVersionUID = 1L;
       
    public WeiXinJsApiServlet() {
        super();
        System.out.println("###################WeiXinJsApiServlet###################");
    }

    //微信后台验证公主号托管服务是否有效
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		com.gf.statusflow.Util.setCtx(ctx);
		
	}
	
	//微信平台Post推送消息到系统
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		try
		{
			PrintWriter out = response.getWriter();
			WeiXinClient wx = (WeiXinClient)com.gf.statusflow.Util.getBean(WeiXinClient.class);
			String operate = Util.getString(request, "operate");
			System.out.println("operate#############################################"+operate);
			//模拟微信后台 使用统一支付接口在微信后台生成订单信息与签名
			//学习测试中使用此链接，真实环境中需要调用微信后台
			if("chooseWXPay".equals(operate))
			{
				String orderid = Util.getString(request, "orderid");
				String clientIp = Util.getString(request, "clientip");
				String openId = Util.getString(request, "openid");
				//支付成功后，微信后台回调通知客户系统是否完成支付
				String notifyUrl = "http://group5.eazequick.com/wxnotify.servlet";
				Integer amount = Util.getInteger(request, "amount");
				String name = Util.getString(request, "name");
				System.out.println("notifyUrl======="+notifyUrl);
				
				String url2 = "http://eazequick.com/weixinpay?operate=chooseWXPay2&orderid="+orderid+"&name="+name+"&amount="+amount+"&openid="+openId+"&clientip="+clientIp+"&notifyurl="+notifyUrl;
				WeiXinHttpClient hc = new WeiXinHttpClient();
				String json = hc.postUrl(url2, "");
				System.out.println("json======="+json);
				out.write(json);
			}
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
