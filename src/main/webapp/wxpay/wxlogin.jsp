<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.math.*,com.gf.weixin.*,com.gf.weixin.bean.*,com.gf.weixin.service.*,com.gf.statusflow.*"%>

<%
String uuid = request.getParameter("uuid");
String url = "http://group5.eazequick.com/wxpay/wxlogindone.jsp?uuid="+uuid;
String url2 = URLEncoder.encode(url);

String url3 = "http://eazequick.com/wxoauth2?url="+url2;
WeiXinHttpClient client = new WeiXinHttpClient();
String url4 = client.getUrl(url3);

response.sendRedirect(url4);
%>
