<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.math.*,com.gf.weixin.bean.*,com.gf.weixin.service.*,com.gf.statusflow.*"%>

<%
//此网页内容将被eazequick.com域名下的JSP包括进去,因为微信后台配置支付目录在eazequick.com域名下
//此网页负责完成微信支付，支付记录新生成或者查询未完成的支付记录
//此网页接收的参数为
//orderid：本系统生成的订单ID,可以检索出订单金额
//openid：当前微信用户唯一ID
//可选参数，如果需要微信用户提供收货地址，获取如下参数
//userName：收货人姓名
//telNumber：收货人电话
//addressPostalCode：邮编
//proviceFirstStageName：省份
//addressCitySecondStageName：地区
//addressCountiesThirdStageName：县市
//addressDetailInfo：详细地址
//检查微信客户端
String hname = "user-agent";
String hvalue = request.getHeader(hname);
if(hvalue == null)
{
	out.println("必须使用微信客户端......");
}
//user-agent:=Mozilla/5.0 (iPhone; CPU iPhone OS 8_4 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12H143 MicroMessenger/6.2.3 NetType/WIFI Language/zh_CN
String wxname = "MicroMessenger/";
int wxpos = hvalue.indexOf(wxname);
if(wxpos == -1)
{
	out.println("必须使用微信客户端......");
}
//获取微信APP版本号
wxpos = wxpos+wxname.length();
hvalue = hvalue.substring(wxpos);
wxpos = hvalue.indexOf(" ");
hvalue = hvalue.substring(0,wxpos);
int wxversion = 0;
try
{
	wxversion = Integer.parseInt(hvalue.substring(0,1));
}
catch(Exception e)
{
	wxversion = 0;
}
System.out.println("WeiXin Version="+wxversion);
//获取客户端IP
String clientIp = request.getRemoteAddr();
//获取客户端openid
String openid = request.getParameter("openid");
//如果购物车进入支付页面,获取订单id
String orderid = request.getParameter("orderid");
//从Spring容器中获取订单服务对象
WxOrderService ordServ = (WxOrderService)Util.getBean(WxOrderService.class);
//根据orderId查询本地订单对象,如果订单对象为新生成订单对象
WxOrderInfo order = ordServ.selectById(Util.fmtStr(orderid));
if(order == null)
{
	//自定义支付金额为1分钱
	Integer total = 1;
	//运费为0
	Integer transportFree = 0;
	//商品名称
	String prdtName = "测试商品";
	//生成订单对象并保存到数据库
	order = ordServ.generateOrder(openid,total,transportFree,prdtName);
	orderid = order.getId();
}
//订单支付成功后，微信后台调用的通知URL
String notifyUrl = "http://group5.eazequick.com/wxnotify.servlet";
%>

<!DOCTYPE HTML>
<meta name="viewport" content="user-scalable=0,width=device-width, initial-scale=1">
<HTML class="no-js " lang="zh-CN">
<HEAD>
<META content="IE=11.0000" http-equiv="X-UA-Compatible">
<TITLE>微信订单</TITLE>
<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
<script type='text/javascript' src='//g.alicdn.com/sj/lib/zepto/zepto.min.js' charset='utf-8'></script>
<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js' charset='utf-8'></script>

<SCRIPT src="/static/js/jquery.min.js" type="text/javascript"></SCRIPT>
<SCRIPT src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></SCRIPT>

<script>
//encodeURIComponent(location.href.split('#')[0]);
var myurl = location.href.split('#')[0];
//myurl = myurl.replace(/&/g,"___");
alert(encodeURIComponent(myurl));

//获取JS支付环境签名
//正式环境需要通过微信后台获取，学习开发环境中是通过模拟微信后台获取
//var xhr = $.ajax('/wxpay.servlet?openid=<%=openid%>&url='+encodeURIComponent(myurl),
//{
//	type: "get",
//	success:getjsconfig
//});

//此网页内容是在eazequick.com域名下访问,Servlet /weixinpay也是访问eazequick.com域名下的Servlet
//此请求微信后台获取签名数据初始化微信APP的JS支付环节
//对应微信支付帮助文档https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115
var xhr = $.ajax('/weixinpay?openid=<%=openid%>&url='+encodeURIComponent(myurl),
{
	type: "get",
	success:getjsconfig
});
//获取JS支付环境回调方法,配置微信支付环境
function getjsconfig(data)
{
	var dataObj = eval('(' + data + ')');
	alert('appId='+dataObj.appId+',timestamp='+dataObj.timestamp+',nonceStr='+dataObj.nonceStr+',signature='+dataObj.signature);
	//配置JS微信支付环境
	wx.config({
	    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	    appId: dataObj.appId, // 必填，公众号的唯一标识
	    timestamp:dataObj.timestamp, // 必填，生成签名的时间戳
	    nonceStr: dataObj.nonceStr, // 必填，生成签名的随机串
	    signature: dataObj.signature,// 必填，签名，见附录1
	    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','chooseWXPay','closeWindow'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	});
}
//就绪回调事件
wx.ready(function(){
	alert('ready');
});
//错误回调事件
wx.error(function(res){
	alert('页面初始化出现错误，请关闭后重新操作：'+res);
	wx.closeWindow();
});
//支付方法
function generateorder(data)
{
	alert(data);
	var dataObj = eval('(' + data + ')');
	//在微信界面调起支付页面,进行支付
	wx.chooseWXPay({
	    timestamp: dataObj.timeStamp,
	    nonceStr: dataObj.nonceStr,
	    package: dataObj.prepay_id,
	    signType: dataObj.signType,
	    paySign: dataObj.paySign,
	    success: function (res) {
	    	alert('支付成功='+res);
	    },
	    failure:function (res)
	    {
	    	alert('FAILURE='+res);
	    }
	});
}
//支付函数
function pay()
{
	alert('pay click');
	//正式环境需要调用微信后台获取，学习开发环境中是调用模拟微信后台获取
	//参数：
	//name 订单名称，通常使用产品名称
	//orderid 订单ID
	//amount 支付金额
	//openid 微信APP唯一ID
	//clientip 客户端IP
	//var xhr = $.ajax('http://group5.eazequick.com/wxjsapi.servlet?operate=chooseWXPay&name=TestOrder&orderid=<%=orderid%>&amount=<%=order.getAllTotal()%>&openid=<%=openid%>&clientip=<%=clientIp%>',
	//{
	//	type: "post",
	//	success:generateorder
	//});
	
	//此网页内容是在eazequick.com域名下访问,Servlet /weixinpay也是访问eazequick.com域名下的Servlet
	//此请求微信后台获取签名数据初始化微信APP的JS支付环节
	var xhr = $.ajax('/weixinpay?operate=chooseWXPay2&name=TestOrder&orderid=<%=orderid%>&amount=<%=order.getAllTotal()%>&openid=<%=openid%>&clientip=<%=clientIp%>&notifyurl=<%=notifyUrl%>',
	{
		type: "post",
		success:generateorder
	});
}
</script>
</HEAD>

<div class="content">
  <div class="buttons-tab" style="background-image:url('/static/images/body_bg.jpg')">
    <a href="#tab1" class="tab-link active button">微信支付</a>
  </div>
    
  <div class="content-block" style="padding:0;margin:0;">
    <div class="tabs">
      <div id="tab1" class="tab active">
		  <div class="list-block" style="padding:0;margin:0;">
		    <ul style="background-image:url('/static/images/body_bg.jpg')">


		    </ul>
		  </div>
		  <div class="content-block" style="padding:0;margin:0;background-image:url('/static/images/body_bg.jpg')">
		    <div class="row">
		      <div class="col-50"><a href="javascript:pay();" class="button button-big button-fill button-danger">支付</a></div>
		    </div>
		  </div>
      </div>

    </div>
  </div>
</div>
</html>