<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.math.*,com.gf.weixin.*,com.gf.weixin.bean.*,com.gf.weixin.service.*,com.gf.statusflow.*"%>

<%
String uuid = request.getParameter("uuid");
String openid = request.getParameter("openid");

out.println("uuid="+uuid);
out.println("openid="+openid);

WeiXinClient.addScanOpenId(uuid,openid);
%>
