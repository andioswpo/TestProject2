package com.gf.weixin;

import javax.servlet.http.HttpServletRequest;

public class Util {
	public static String getString(HttpServletRequest request,String name)
	{
		String value = request.getParameter(name);
		if(value == null)
			return "";
		return value;
	}
	
	public static Integer getInteger(HttpServletRequest request,String name)
	{
		String value = request.getParameter(name);
		try
		{
			return Integer.parseInt(value);
		}
		catch(Exception e)
		{
			return new Integer(0);
		}
	}
	
	public static String fmtStr(Object obj)
	{
		if(obj == null)
			return "";
		return obj.toString();
	}
	
	public static String getFormatString(String fmtStr,String paraName,String value)
	{
		String rtnStr = "";
		rtnStr = fmtStr.replaceAll(paraName, value);
		return rtnStr;
	}
	
	public static String getSHA1(String pwd)
	{   
		byte[] source = pwd.getBytes();   
		String s = null;      
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
		try     
		{      
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA1");      
			md.update( source );      
			byte tmp[] = md.digest(); 
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}       
		s = new String(str);
		}catch( Exception e ) {
			e.printStackTrace();      
		}      
		return s;      
	}
}
