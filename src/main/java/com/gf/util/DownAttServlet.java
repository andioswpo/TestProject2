package com.gf.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.portable.OutputStream;

import com.gf.model.AttInfo;
import com.gf.service.AttService;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;
import com.gf.weixin.WeiXinClient;

@WebServlet(urlPatterns = "/downfile/*")
public class DownAttServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public DownAttServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			response.setHeader("Pragma","no-cache");
			response.setHeader("Cache-Control","no-store");
			response.setDateHeader("Expires",-1);
			String attId = request.getParameter("attid");
			String wxlogin = request.getParameter("wxlogin");
			if(!"".equals(wxlogin))
			{
				String uuid = request.getParameter("uuid");
				String url = "http://group5.eazequick.com/wxpay/wxlogin.jsp?uuid="+uuid;
				byte[] data = WeiXinClient.generateQrCode(url);
				response.setHeader("Content-Disposition", "attachment; filename=test.png"); 
				ServletOutputStream os = response.getOutputStream();
				os.write(data);
			}
			else
			{		
				AttService attserv = (AttService)Util.getBean(AttService.class);
				AttInfo att = attserv.getAttById(attId);
				response.setHeader("Content-Disposition", "attachment; filename="+att.getName()); 
				ServletOutputStream os = response.getOutputStream();
				os.write(att.getContent());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
