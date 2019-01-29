package com.gf.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gf.model.ExpenseInfo;
import com.gf.service.ExpenseService;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.IUser;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;

@Controller
public class ExpenseCtrl {
	private Logger log = LoggerFactory.getLogger(ExpenseCtrl.class);
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private ExpenseService expserv;

	//本方法可以通过新建业务数据进入，也可通过从工作流代办列表中进入
	@RequestMapping("/expense.action")
	public String expense(HttpServletRequest req)
	{
		//从工作流代办列表中进入时，需要传递此四个参数
		String instanceid = req.getParameter("instanceid");
		String instprocessid = req.getParameter("instprocessid");
		String instactivityid = req.getParameter("instactivityid");
		String instworkitemid = req.getParameter("instworkitemid");
		//工作流方法返回结果对象
		StatusMsg wfMsg = null;
		//工作流下一个办理环节
		String nexttask = null;
		//工作流下一办理环节集合，用于生成页面上按钮
		List<Properties> nextBtnList = null;
		//获取当前登录用户
		IUser loginUser = Util.getLoginUser();
		String userId = loginUser.getId();
		//流程名称，与流程模板XML文件同名
		String processId = "expense";
		HashMap hmap = null;
		//获取工作流控制数据
		StatusFlowData wfdata = sfmng.getWorkflowData(userId, processId, 
				nexttask, instworkitemid,hmap);
		//工作流下一办理环节集合，用于生成页面上按钮
		nextBtnList = wfdata.getNextBtnList();
		log.debug("expense nextBtnList="+nextBtnList);
		//返回到前台页面，用于生成页面上按钮
		req.setAttribute("button", nextBtnList);
		//如果实例ID不为空，说明是通过代办件列表进入业务页面的
		if(instanceid != null)
		{
			//返回业务对象和工作流核心数据到页面
			ExpenseInfo eps = expserv.selectById(instanceid);
			req.setAttribute("eps", eps);
			req.setAttribute("instprocessid", instprocessid);
			req.setAttribute("instactivityid", instactivityid);
			req.setAttribute("instworkitemid", instworkitemid);
		}
		return "/fi/expense";
	}
	
	@RequestMapping("/expsubmit.action")
	@ResponseBody
	public StatusMsg expsave(HttpServletRequest req,ExpenseInfo exps)
	{
		try
		{
			//如果实例ID为空，说明是新建业务数据，否则是通过工作流代办列表进入
			String instanceid = exps.getId();
			if("".equals(Util.fmtStr(instanceid)))
				instanceid = UUID.create("expense");
			//通过Request对象获取工作流核心控制数据
			String instprocessid = req.getParameter("instprocessid");
			String instactivityid = req.getParameter("instactivityid");
			String instworkitemid = req.getParameter("instworkitemid");
			//获取下一办理环节，对应页面上的按钮
			String nexttask = req.getParameter("nexttask");
			//获取下一办理环节上的审批人，通过前台页面上按钮事件参数传入
			String nextUserId = req.getParameter("nextUserId");
			//当前登录人
			IUser loginUser = Util.getLoginUser();
			String startUserId = loginUser.getId();
			//流程模板名称
			String processId = "expense";
			//流程分类，可以自定义
			String type = "expense";
			//流程标题
			String title = exps.getName()+":"+exps.getAmount().toString();
			//打开代办列表中不同流程的URL
			String url = "/expense.action";
			HashMap hmap = new HashMap();
			//flag,testMode使用默认值，其对应的功能当前未使用
			String flag = "gf";
			String testMode = "no";
			exps.setId(instanceid);
			//工作流操作返回结果对象
			StatusMsg wfMsg = null;
			//如果代办件ID为空，说明是启动流程
			if("".equals(Util.fmtStr(instworkitemid)))
			{
				//保存业务对象数据
				expserv.insert(exps);
				//获取前台页面传入的下一个办理环节审批人列表
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
				//启动工作流
				wfMsg = wapi.startWorkflow(processId,startUserId,userIdList,instanceid,
						type,title,nexttask,url,hmap,flag,testMode);
			}
			else//从代办件列表进入页面并提交
			{
				List<String> userIdList = new ArrayList<String>();
				String[] dim = nextUserId.split(",");
				for(String s:dim)
				{
					if(!"".equals(Util.fmtStr(s)))
						userIdList.add(s);
				}
				//更新业务数据
				expserv.updateById(exps);
				//提交工作流程
				wfMsg = wapi.submitWorkflow(instprocessid,instactivityid,instworkitemid,
						userIdList,instanceid,type,title,nexttask,url,hmap,flag,testMode);
			}
			return wfMsg;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
