package com.gf.ctrl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.service.FunctionService;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.IUser;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;
import com.gf.statusflow.def.FunctionInfo;
import com.gf.statusflow.def.TreeNode;
import com.gf.weixin.WeiXinClient;

@Controller
public class MainCtrl {
	@Autowired
	private FunctionService serv;
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private StatusFlowWAPI wapi;
	
	@RequestMapping("/testeui")
	public String testeui(HttpServletRequest req)
	{
		return "/vxeasyui/list";
	}
	
	@RequestMapping("/login")
	public String login(HttpServletRequest req)
	{
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req)
	{
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		WeiXinClient.wxMap = new HashMap<String,String>();
		return "login";
	}
	
	@RequestMapping("/wxcheck")
	@ResponseBody
	public Boolean wxcheck(HttpServletRequest req,HttpServletResponse resp,
			String uuid)
	{
		Map<String,String> map = WeiXinClient.wxMap;

		String openId = map.get("openid");
System.out.println("openId==="+openId);
		IUser user = orgmodel.getUserByOpenId(openId);
		if(user != null)
		{
System.out.println("user==="+user);		
			
			
			Subject subject = SecurityUtils.getSubject();
	        UsernamePasswordToken upToken = new UsernamePasswordToken(user.getLoginId(),user.getPassword());
	        //进行验证，这里可以捕获异常，然后返回对应信息
	        subject.login(upToken);
	        System.out.println("2=========="+subject.getPrincipal());
	        subject.getSession().setAttribute("loginuser", user);
	        return true;
		}
        return false;
	}
	
	@RequestMapping("/logindone")
	@ResponseBody
	public String logindone(HttpServletRequest req,HttpServletResponse resp,
			String loginId,String pwd)
	{
      //添加用户认证信息
    	try
    	{
	        Subject subject = SecurityUtils.getSubject();
	        System.out.println("1=========="+subject.getPrincipal());
	        String pwdMd5 = Util.getMD5(pwd);
	        UsernamePasswordToken upToken = new UsernamePasswordToken(loginId,pwdMd5);
	        //进行验证，这里可以捕获异常，然后返回对应信息
	        subject.login(upToken);
	        System.out.println("2=========="+subject.getPrincipal());
	        IUser user = orgmodel.getUserByLoginId(subject.getPrincipal().toString());
	        subject.getSession().setAttribute("loginuser", user);
	        
			//记录Cookie
			Cookie c = new Cookie("loginId",loginId);  
            c.setPath(req.getContextPath());
            resp.addCookie(c);
			Cookie c2 = new Cookie("pwd",pwd);  
            c2.setPath(req.getContextPath());
            resp.addCookie(c2);
	        return "true";
    	}
    	catch(UnknownAccountException uae)
    	{
    		return "账号错误";
    	}
    	catch(IncorrectCredentialsException ice)
    	{
    		return "密码错误";
    	}

	}
	
	//代办件列表
	@RequestMapping("/workitem.action")
	public String workitem()
	{
		return "/orgmodel/workitem";
	}
	
	@RequestMapping("/workitemlist.action")
	@ResponseBody
	public List workitemlist()
	{
		try
		{
			IUser user = Util.getLoginUser();
			String testMode = "no";
			return wapi.getWorkitemList(user.getId(), testMode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//办结列表
	@RequestMapping("/finishitem.action")
	public String finishitem()
	{
		return "/orgmodel/finishitem";
	}
	
	@RequestMapping("/finishitemlist.action")
	@ResponseBody
	public List finishitemlist()
	{
		try
		{
			IUser user = Util.getLoginUser();
			String testMode = "no";
			return wapi.getFinishList(user.getId(), testMode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/main.action")
	public String main(HttpServletRequest req)
	{
		Subject subject = SecurityUtils.getSubject();
System.out.println("0========="+subject.getSession().getAttribute("loginuser").getClass().getClassLoader());
System.out.println("1========="+subject.getSession().getAttribute("loginuser").getClass().getClassLoader());
System.out.println("2========="+IUser.class.getClassLoader());
System.out.println("3========="+SecurityUtils.class.getClassLoader());
recurseClassLoader(IUser.class.getClassLoader());
System.out.println("#############################"+subject.getSession().getClass());

recurseClassLoader(SecurityUtils.class.getClassLoader());

		IUser user = (IUser)subject.getSession().getAttribute("loginuser");
		String userId = user.getId();
		String menu = serv.generateMenuBar(userId);
		req.setAttribute("menu", menu);
		req.setAttribute("user", user);
		return "main";
	}
	
	private void recurseClassLoader(ClassLoader cl)
	{
		ClassLoader parent = cl.getParent();
		if(parent != null)
		{
			System.out.println("1==="+cl + " Parent is "+parent);
			recurseClassLoader(parent);
		}
		else
			System.out.println("2==="+cl + " Parent is "+parent);
	}
	
	@RequestMapping("/function.action")
	public String function()
	{
		return "/orgmodel/function";
	}
	
	
	@ResponseBody
	@RequestMapping("/funcroot.action")
	public List<TreeNode> funcroot()
	{
		FunctionInfo rootFi = orgmodel.getRootFunc();
		if(rootFi == null)
		{
			rootFi = orgmodel.initFunc();
		}
		List<TreeNode> trees = new ArrayList<TreeNode>();
		TreeNode rootTree = new TreeNode();
		rootTree.setId(rootFi.getId().toString());
		rootTree.setText(rootFi.getName());
		rootTree.setState("closed");
		Map attributes = new HashMap();
		attributes.put("parentId",rootFi.getParentId());
		attributes.put("path", "/");
		attributes.put("fullName", "/"+rootFi.getName());
		attributes.put("isload", "false");
		rootTree.setAttributes(attributes);
		
		trees.add(rootTree);
		return trees;
	}
	
	@ResponseBody
	@RequestMapping("/funcsubsave.action")
	public boolean funcsubsave(FunctionInfo fi)
	{
		String parentId = fi.getId();
		if(parentId == null)
		{
			return false;
		}
		FunctionInfo parent = orgmodel.getFuncById(parentId);
		String id = UUID.create("function");
		fi.setId(id);
		fi.setParentId(parentId);
		fi.setPath(parent.getPath()+"/"+id);
		fi.setFullName(parent.getFullName()+"/"+fi.getName());
		orgmodel.saveFunc(fi);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/funcsave.action")
	public boolean funcsave(FunctionInfo fi)
	{
		String id = fi.getId();
		if(id == null)
		{
			fi.setId(UUID.create("function"));
			orgmodel.saveFunc(fi);
		}
		else
			orgmodel.updateFunc(fi);
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/funcdel.action")
	public boolean funcdel(FunctionInfo fi)
	{
		orgmodel.deleteFunc(fi.getId());
		return true;
	}
	
	@ResponseBody
	@RequestMapping("/funcicon.action")
	public List funcicon()
	{
		List lst = new ArrayList();
		for(int i=1;i<=100;i++)
		{
			Map m = new HashMap();
			m.put("id","pic_"+i);
			m.put("name","pic_"+i);
			lst.add(m);
		}
		return lst;
	}
	
	@ResponseBody
	@RequestMapping("/gettreebyid")
	public List<TreeNode> gettreebyid(String id)
	{
		List<FunctionInfo> funs = orgmodel.getChildFunc(id);
		List<TreeNode> rtn = new ArrayList<TreeNode>();
		for(FunctionInfo fi:funs)
		{
			TreeNode ti = new TreeNode();
			ti.setId(fi.getId().toString());
			ti.setText(fi.getName());
			ti.setIconCls(fi.getIcon()!=null?fi.getIcon():"pic_1");
			ti.setUrl(fi.getUrl());
			List<FunctionInfo> chd = orgmodel.getChildFunc(fi.getId());
			if(chd != null && chd.size()>0)
				ti.setState("closed");
			else
				ti.setState("open");
			ti.addAttribute("path",fi.getPath());
			ti.addAttribute("fullName",fi.getFullName());
			ti.addAttribute("isload","false");
			if(fi.getPriority() != null)
				ti.addAttribute("priority",fi.getPriority().toString());
			if(fi.getParentId() != null)
				ti.addAttribute("parentId",fi.getParentId().toString());
			rtn.add(ti);
		}
		return rtn;
	}

}
