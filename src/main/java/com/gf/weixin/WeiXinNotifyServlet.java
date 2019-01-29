package com.gf.weixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.gf.statusflow.UUID;
import com.gf.weixin.bean.PayInfo;
import com.gf.weixin.bean.WxOrderPayNotifyInfo;
import com.wxap.RequestHandler;
import com.wxap.util.XMLUtil;

/**
 * 此Servlet等待微信后台支付通知结果
 * @author java
 *
 */
@WebServlet(urlPatterns="/wxnotify.servlet",loadOnStartup=0)
public class WeiXinNotifyServlet extends HttpServlet {
	private Logger log = LoggerFactory.getLogger(WeiXinNotifyServlet.class);
	private static final long serialVersionUID = 1L;
       
    public WeiXinNotifyServlet() {
        super();
        System.out.println("###################WeiXinNotifyServlet###################");
    }

	public void service(HttpServletRequest request,HttpServletResponse response) throws ServletException
	{
		try
		{
			request.setCharacterEncoding("UTF8");
			response.setCharacterEncoding("UTF8");
			String method = request.getMethod();
			PrintWriter out = response.getWriter();
			InputStream is = request.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int cnt = 0;
			byte[] data = new byte[1024];
			while((cnt=is.read(data))!=-1)
				baos.write(data,0,cnt);			
			System.out.println("PayNotifyServlet#############################################BEGIN");
			Enumeration<String> e = request.getParameterNames();
			for(;e.hasMoreElements();)
			{
				String p = e.nextElement();
				String v = request.getParameter(p);
				log.debug("#"+p+"="+v);
			}
			System.out.println("PayNotifyServlet#############################################END");
			String xml = baos.toString();
			System.out.println("PayNotifyServlet inBody="+xml);
			if("POST".equalsIgnoreCase(method))
			{
				Map map = XMLUtil.doXMLParse(xml);
				String appid = (String)map.get("appid");
				String bank_type = (String)map.get("bank_type");
				String cash_fee = (String)map.get("cash_fee");
				String fee_type = (String)map.get("fee_type");
				String is_subscribe = (String)map.get("is_subscribe");
				String mch_id = (String)map.get("mch_id");
				String nonce_str = (String)map.get("nonce_str");
				String openid = (String)map.get("openid");
				String out_trade_no = (String)map.get("out_trade_no");
				String result_code = (String)map.get("result_code");
				String return_code = (String)map.get("return_code");
				String sign = (String)map.get("sign");
				String time_end = (String)map.get("time_end");
				String total_fee = (String)map.get("total_fee");
				String trade_type = (String)map.get("trade_type");
				String transaction_id = (String)map.get("transaction_id");
				WxOrderPayNotifyInfo wxpni = new WxOrderPayNotifyInfo();
				wxpni.setId(UUID.create("notify"));
				wxpni.setAppid(appid);
				wxpni.setBank_type(bank_type);
				wxpni.setCash_fee(new Integer(cash_fee));
				wxpni.setFee_type(fee_type);
				wxpni.setIs_subscribe(is_subscribe);
				wxpni.setMch_id(mch_id);
				wxpni.setNonce_str(nonce_str);
				wxpni.setOpenid(openid);
				wxpni.setOut_trade_no(out_trade_no);
				wxpni.setResult_code(result_code);
				wxpni.setReturn_code(return_code);
				wxpni.setSign(sign);
				wxpni.setTime_end(time_end);
				wxpni.setTotal_fee(new Integer(total_fee));
				wxpni.setTrade_type(trade_type);
				wxpni.setTransaction_id(transaction_id);
				
				//判断签名
		        SortedMap<String,String> smap = new TreeMap<String,String>();
		        smap.put("appid",appid);
		        smap.put("bank_type",bank_type);
		        smap.put("cash_fee",cash_fee.toString());
		        smap.put("fee_type",fee_type);
		        smap.put("is_subscribe",is_subscribe);
		        smap.put("mch_id",mch_id);
		        smap.put("nonce_str",nonce_str);
		        smap.put("openid",openid);
		        smap.put("out_trade_no",out_trade_no);
		        smap.put("result_code",result_code);
		        smap.put("return_code",return_code);
		        smap.put("time_end",time_end);
		        smap.put("total_fee",total_fee.toString());
		        smap.put("trade_type",trade_type);
		        smap.put("transaction_id",transaction_id);
		        RequestHandler wxrqh = new RequestHandler();
		        //wxrqh.setKey(cust.getApiKey());
		        String sign2 = wxrqh.createSign(smap);
		        System.out.println("sign1======"+sign);
		        System.out.println("sign2======"+sign2);
		        //说明请求确实来自微信后台,缺少ApiKey获得签名不同相同
		        if(sign.equals(sign2))
		        {
		        	//同步方法，更新订单状态及发货状态，避免函数重入
					
		        	String bodyXml = "";
					bodyXml = bodyXml + "<xml>";
					bodyXml = bodyXml + "<return_code>SUCCESS</return_code>";
					bodyXml = bodyXml + "</xml>";
					out.write(bodyXml);
					System.out.println("PayNotifyServlet1 bodyXml="+bodyXml);
		        }
		        else
		        {
//					String bodyXml = "";
//					bodyXml = bodyXml + "<xml>";
//					bodyXml = bodyXml + "<return_code>FAIL</return_code>";
//					bodyXml = bodyXml + "<return_msg>SIGN ERROR</return_msg>";
//					bodyXml = bodyXml + "</xml>";
//					out.write(bodyXml);
//					System.out.println("PayNotifyServlet2 bodyXml="+bodyXml);
					
		        	String bodyXml = "";
					bodyXml = bodyXml + "<xml>";
					bodyXml = bodyXml + "<return_code>SUCCESS</return_code>";
					bodyXml = bodyXml + "</xml>";
					out.write(bodyXml);
					System.out.println("PayNotifyServlet1 bodyXml="+bodyXml);
		        }
				out.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
