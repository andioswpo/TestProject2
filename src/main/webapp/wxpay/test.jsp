<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.math.*,com.gf.weixin.bean.*,com.gf.weixin.service.*,com.gf.statusflow.*"%>

<%
String openid = request.getParameter("openid");
String code = request.getParameter("code");

out.println("openid="+openid+",code="+code);

%>
