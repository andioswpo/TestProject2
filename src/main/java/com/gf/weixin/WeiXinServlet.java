package com.gf.weixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
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

import com.gf.weixin.bean.MemberInfo;
import com.gf.weixin.bean.UserInfo;
import com.gf.weixin.bean.WxPostMsgInfo;
import com.gf.weixin.bean.WxPostMsgItemInfo;
import com.gf.weixin.http.ReplyPicTextResponse;
import com.gf.weixin.service.AccountService;
import com.gf.weixin.xml.EventInfo;
import com.gf.weixin.xml.EventParse;
import com.gf.weixin.xml.PostParse;

@WebServlet(urlPatterns="/wx/*",loadOnStartup=0)
public class WeiXinServlet extends HttpServlet {
	private Logger log = LoggerFactory.getLogger(WeiXinServlet.class);
	private static final long serialVersionUID = 1L;
       
    public WeiXinServlet() {
        super();
        System.out.println("###################WeiXinServlet###################");
    }
    
    public void init(ServletConfig config)
    {
    	System.out.println("###################WeiXinServlet.init###################");
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		com.gf.statusflow.Util.setCtx(ctx);
    }

    //微信后台验证公主号托管服务是否有效
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String signature = Util.getString(request, "signature");
		String timestamp = Util.getString(request, "timestamp");
		String nonce = Util.getString(request, "nonce");
		String echostr = Util.getString(request, "echostr");
		log.debug("doGet signature="+signature+",timestamp="+timestamp);
		log.debug("doGet nonce="+nonce+",echostr="+echostr);
		//通过Spring容器获取对象
		WeiXinClient client = (WeiXinClient)com.gf.statusflow.Util.getBean(WeiXinClient.class);
		String token = client.getCurrentToken();
		log.debug("doGet token="+token);
		String[] dim = {timestamp,nonce,token};
		Arrays.sort(dim);
		String encodeKey = dim[0]+dim[1]+dim[2];
		log.debug("doGet encodeKey="+encodeKey);
		String mySig = Util.getSHA1(encodeKey);
		log.debug("doGet mySig="+mySig);
		PrintWriter out = response.getWriter();
		if(signature.equals(mySig))
		{
			out.write(echostr);
		}
		else
		{
			out.write("Invalid Request");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String reqScanCode = request.getParameter("scancode");
		WeiXinClient client = (WeiXinClient)com.gf.statusflow.Util.getBean(WeiXinClient.class);
		AccountService acctServ = (AccountService)com.gf.statusflow.Util.getBean(AccountService.class);
		
		PrintWriter out = response.getWriter();
		InputStream is = request.getInputStream();
		byte[] data = FileCopyUtils.copyToByteArray(is);
		log.debug("doPost data="+new String(data));
		PostParse xmlP = new PostParse();
		xmlP.parse(data);
		String content = Util.fmtStr(xmlP.getContent());
		String msgType = xmlP.getMsgType();
		String wxId = xmlP.getToUserName();
		String fromUserName = xmlP.getFromUserName();
		String fromOpenId = fromUserName;
		Timestamp createTime = new Timestamp(new Date().getTime());
		String picUrl = xmlP.getPicUrl();
		String mediaId = xmlP.getMediaId();
		String tmMediaId = xmlP.getThumbMediaId();
		String format = xmlP.getFormat();
		String locationX = xmlP.getLocationX();
		String locationY = xmlP.getLocationY();
		int scale = xmlP.getScale();
		String label = xmlP.getLabel();
		String title = xmlP.getTitle();
		String description = xmlP.getDescription();
		String url = xmlP.getUrl();
		long msgId = xmlP.getMsgId();
		log.debug("doPost content="+content+",msgType="+msgType);
		log.debug("doPost wxId="+wxId+",msgType="+msgType);
		log.debug("doPost fromUserName="+fromUserName+",fromOpenId="+fromOpenId);
		log.debug("doPost createTime="+createTime+",picUrl="+picUrl);
		log.debug("doPost label="+label+",title="+title);
		log.debug("doPost description="+description+",url="+url);
		log.debug("doPost msgId="+msgId+",msgType="+msgType);
		//事件类消息
		if("event".equals(msgType))
		{
			EventParse evtP = new EventParse();
			evtP.parse(data);
			EventInfo evtInfo = evtP.getEvent();
			String event = evtInfo.getEvent();
			String eventKey = evtInfo.getEventKey();
			String openId = evtInfo.getFromUserName();
			System.out.println("event="+event+",eventKey="+eventKey+",openId="+openId);
			//关注消息
			if("subscribe".equalsIgnoreCase(event))
			{
				//获取关注者信息
				String accessToken = acctServ.getAccessToken();
				UserInfo ui = client.getUserByOpenId(fromOpenId, accessToken);
				System.out.println("ui==="+ui);
				
				client.sendTextMessage(fromOpenId, "欢迎关注...", accessToken);
				
				//扫描带参数的二维码保存参数到会员信息同时此参数作为微信关注者分组ID
				Integer scanCode = client.getScanCode(eventKey);
				//String msg = "欢迎关注...";
				//String echoStr = client.passiveSendText(fromOpenId,wxId,msg);
				//if(echoStr != null)
				//	out.println(echoStr);
				
				String imgUrl = "http://group5.eazequick.com/wxpay/1111.png";
				String payUrl = "http://group5.eazequick.com/wxpay/buy.jsp?openid="+fromOpenId;
				String proxyUrl = "http://eazequick.com/wxpay/payproxy.jsp?url="+java.net.URLEncoder.encode(payUrl);
				StringBuffer sb = new StringBuffer();
				sb.append("<xml>\r\n");
				sb.append("<ToUserName><![CDATA["+fromOpenId+"]]></ToUserName>\r\n");
				sb.append("<FromUserName><![CDATA["+wxId+"]]></FromUserName>\r\n");
				sb.append("<CreateTime>"+System.currentTimeMillis()+"</CreateTime>\r\n");
				sb.append("<MsgType><![CDATA[news]]></MsgType>\r\n");
				sb.append("<ArticleCount>2</ArticleCount>\r\n");
				sb.append("<Articles>\r\n");
				sb.append("<item>\r\n");
				sb.append("<Title><![CDATA[我的测试图文消息]]></Title>\r\n");
				sb.append("<Description><![CDATA[测试支付]]></Description>\r\n");
				sb.append("<PicUrl><![CDATA["+imgUrl+"]]></PicUrl>\r\n");
				sb.append("<Url><![CDATA["+proxyUrl+"]]></Url>\r\n");
				sb.append("</item>\r\n");
				sb.append("<item>\r\n");
				sb.append("<Title><![CDATA[我的测试图文消息2]]></Title>\r\n");
				sb.append("<Description><![CDATA[测试支付2]]></Description>\r\n");
				sb.append("<PicUrl><![CDATA["+imgUrl+"]]></PicUrl>\r\n");
				sb.append("<Url><![CDATA["+payUrl+"]]></Url>\r\n");
				sb.append("</item>\r\n");
				sb.append("</Articles></xml>\r\n");
				out.println(sb.toString());
			}
			//取消关注消息
			if("unsubscribe".equalsIgnoreCase(event))
			{
				//扫描带参数的二维码保存参数到会员信息同时此参数作为微信关注者分组ID
				Integer scanCode = client.getScanCode(eventKey);
				System.out.println("unsubscribe  "+fromOpenId);
			}
			//菜单点击消息
			if("click".equalsIgnoreCase(event))
			{
				//扫描带参数的二维码保存参数到会员信息同时此参数作为微信关注者分组ID
				Integer scanCode = client.getScanCode(eventKey);
				System.out.println("click  "+fromOpenId);
			}
			//二维码Scan事件
			if("scan".equalsIgnoreCase(event))
			{
				//扫描带参数的二维码保存参数到会员信息同时此参数作为微信关注者分组ID
				Integer scanCode = client.getScanCode(eventKey);
				System.out.println("scan  "+fromOpenId);
			}
		}
		else
		{
			//MemberInfo mem = wxserv.getMemberByOpenId(fromOpenId);
			Integer scanCode = new Integer(reqScanCode);//mem.getWxScanCode();
			//微信平台Post文本消息到系统
			if("text".equalsIgnoreCase(msgType))
			{
				//content是客户端微信输入的字符串
				if("hi".equals(content))
				{
					//WxPostMsgInfo msg = new WxPostMsgInfo();
					String msg = "欢迎关注...";
					String echoStr = client.passiveSendText(fromOpenId,wxId,msg);
					System.out.println("text === "+echoStr);
					if(echoStr != null)
						out.println(echoStr);
				}
				if("cj".equals(content))
				{
					//WxPostMsgInfo msg = new WxPostMsgInfo();
					String msg = "抽奖...";
					String echoStr = client.passiveSendText(fromOpenId,wxId,msg);
					System.out.println("text === "+echoStr);
					if(echoStr != null)
						out.println(echoStr);
				}
				if("pay".equals(content))
				{
					//<xml><ToUserName>< ![CDATA[toUser] ]></ToUserName><FromUserName>
					//< ![CDATA[fromUser] ]></FromUserName>
					//<CreateTime>12345678</CreateTime>
					//<MsgType>< ![CDATA[news] ]></MsgType>
					//<ArticleCount>1</ArticleCount>
					//<Articles><item><Title>< ![CDATA[title1] ]></Title> 
					//<Description>< ![CDATA[description1] ]></Description>
					//<PicUrl>< ![CDATA[picurl] ]></PicUrl>
					//<Url>< ![CDATA[url] ]></Url></item>
					//</Articles></xml>
					String imgUrl = "http://group5.eazequick.com/wxpay/1111.png";
					String payUrl = "http://group5.eazequick.com/wxpay/buy.jsp?openid="+fromOpenId;
					String proxyUrl = "http://eazequick.com/wxpay/payproxy.jsp?url="+java.net.URLEncoder.encode(payUrl);
					StringBuffer sb = new StringBuffer();
					sb.append("<xml>\r\n");
					sb.append("<ToUserName><![CDATA["+fromOpenId+"]]></ToUserName>\r\n");
					sb.append("<FromUserName><![CDATA["+wxId+"]]></FromUserName>\r\n");
					sb.append("<CreateTime>"+System.currentTimeMillis()+"</CreateTime>\r\n");
					sb.append("<MsgType><![CDATA[news]]></MsgType>\r\n");
					sb.append("<ArticleCount>1</ArticleCount>\r\n");
					sb.append("<Articles>\r\n");
					sb.append("<item>\r\n");
					sb.append("<Title><![CDATA[我的测试图文消息]]></Title>\r\n");
					sb.append("<Description><![CDATA[测试支付]]></Description>\r\n");
					sb.append("<PicUrl><![CDATA["+imgUrl+"]]></PicUrl>\r\n");
					sb.append("<Url><![CDATA["+proxyUrl+"]]></Url>\r\n");
					sb.append("</item>\r\n");
					sb.append("</Articles></xml>\r\n");
					out.println(sb.toString());
				}
				if("test".equals(content))
				{
					String imgUrl = "http://group5.eazequick.com/wxpay/1111.png";
					String payUrl = "http://group5.eazequick.com/wxpay/test.jsp";
					
					payUrl = client.getOAuth2Url(payUrl);
					System.out.println("####################"+payUrl);
					
					StringBuffer sb = new StringBuffer();
					sb.append("<xml>\r\n");
					sb.append("<ToUserName><![CDATA["+fromOpenId+"]]></ToUserName>\r\n");
					sb.append("<FromUserName><![CDATA["+wxId+"]]></FromUserName>\r\n");
					sb.append("<CreateTime>"+System.currentTimeMillis()+"</CreateTime>\r\n");
					sb.append("<MsgType><![CDATA[news]]></MsgType>\r\n");
					sb.append("<ArticleCount>1</ArticleCount>\r\n");
					sb.append("<Articles>\r\n");
					sb.append("<item>\r\n");
					sb.append("<Title><![CDATA[我的测试图文消息]]></Title>\r\n");
					sb.append("<Description><![CDATA[测试支付]]></Description>\r\n");
					sb.append("<PicUrl><![CDATA["+imgUrl+"]]></PicUrl>\r\n");
					sb.append("<Url><![CDATA["+payUrl+"]]></Url>\r\n");
					sb.append("</item>\r\n");
					sb.append("</Articles></xml>\r\n");
					out.println(sb.toString());
				}
				
				if("login".equals(content))
				{
					String imgUrl = "http://group5.eazequick.com/wxpay/1111.png";
					String payUrl = "http://group5.eazequick.com/wxpay/wxlogin.jsp";
					
					payUrl = client.getOAuth2Url(payUrl);
					System.out.println("####################"+payUrl);
					
					StringBuffer sb = new StringBuffer();
					sb.append("<xml>\r\n");
					sb.append("<ToUserName><![CDATA["+fromOpenId+"]]></ToUserName>\r\n");
					sb.append("<FromUserName><![CDATA["+wxId+"]]></FromUserName>\r\n");
					sb.append("<CreateTime>"+System.currentTimeMillis()+"</CreateTime>\r\n");
					sb.append("<MsgType><![CDATA[news]]></MsgType>\r\n");
					sb.append("<ArticleCount>1</ArticleCount>\r\n");
					sb.append("<Articles>\r\n");
					sb.append("<item>\r\n");
					sb.append("<Title><![CDATA[我的测试图文消息]]></Title>\r\n");
					sb.append("<Description><![CDATA[测试支付]]></Description>\r\n");
					sb.append("<PicUrl><![CDATA["+imgUrl+"]]></PicUrl>\r\n");
					sb.append("<Url><![CDATA["+payUrl+"]]></Url>\r\n");
					sb.append("</item>\r\n");
					sb.append("</Articles></xml>\r\n");
					out.println(sb.toString());
				}
				
//				WxPostMsgInfo postmsg = wxserv.getMsgByKey(flag, content);
//				if(postmsg!=null)
//				{
//					ReplyPicTextResponse ptRes = new ReplyPicTextResponse(wxId,fromUserName,null);
//					List<WxPostMsgItemInfo> itemList = wxserv.getMsgItem(postmsg.getId());
//					ptRes.setMsgList(itemList);
//					String rtn = ptRes.getText();
//					out.println(rtn);
//				}
			}
			//微信平台Post图片消息到系统
			if("image".equalsIgnoreCase(msgType))
			{
				
			}
			//微信平台Post音频消息到系统
			if("voice".equalsIgnoreCase(msgType))
			{
				
			}
			//微信平台Post音频消息到系统
			if("video".equalsIgnoreCase(msgType))
			{
				
			}
			//微信平台Post地理位置消息到系统
			if("location".equalsIgnoreCase(msgType))
			{
				
			}
			//微信平台Post链接消息到系统
			if("link".equalsIgnoreCase(msgType))
			{
				
			}
		}

		out.close();
		
	}
	
	public static void main(String[] args)
	{
		String timestamp = "9";
		String nonce = "2";
		String token = "3";
		String[] dim = {timestamp,nonce,token};
		Arrays.sort(dim);
		for(String s:dim)
			System.out.println(s);
	}

}
