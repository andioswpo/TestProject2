package com.gf.ctrl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gf.model.AttInfo;
import com.gf.model.BorrowInfo;
import com.gf.service.AttService;
import com.gf.service.BorrowService;
import com.gf.statusflow.IUser;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;

@Controller
public class BorrowCtrl {
	private Logger log = LoggerFactory.getLogger(BorrowCtrl.class);
	@Autowired
	private StatusFlowMng wfmng;
	@Autowired
	private BorrowService serv;
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private AttService attserv;
	
	@RequestMapping("/borrow.action")
	public String borrow(HttpServletRequest req)
	{
		//进入借款页面有两个入口，一是新建借款单，二是从工作代办件进入
		String processId = "borrow";
		IUser user = Util.getLoginUser();
		String userId = user.getId();
		//从工作代办件进入,获取四个参数
		String status = req.getParameter("status");//从办结列表进入时,此参数为end
		String instanceId = req.getParameter("instanceid");//借款单ID
		String instProcessId = req.getParameter("instprocessid");//流程实例ID
		String instActivityId = req.getParameter("instactivityid");//活动实例ID
		String instWorkitemId = req.getParameter("instworkitemid");//代办件ID
		
		//获取工作流模板相关数据
		StatusFlowData wfdata = wfmng.getWorkflowData(userId, processId, instWorkitemId);
		//根据流程模板定义获取页面操作按钮
		List<Properties> btn = wfdata.getNextBtnList();
		req.setAttribute("button", btn);
		//从办结列表进入删除按钮
		if("end".equals(status))
			req.setAttribute("button", new ArrayList<Properties>());
		
		if(!"".equals(Util.fmtStr(instanceId)))
		{
			BorrowInfo bi = serv.selectById(instanceId);
			req.setAttribute("bi", bi);
			req.setAttribute("instprocessid", instProcessId);
			req.setAttribute("instactivityid", instActivityId);
			req.setAttribute("instworkitemid", instWorkitemId);
		}
		
		return "/fi/borrow";
	}
	
	@RequestMapping("/borrowsubmit.action")
	@ResponseBody
	public StatusMsg borrowsubmit(HttpServletRequest req,BorrowInfo bi)
	{
		StatusMsg msg = new StatusMsg();
		try
		{
			String nexttask = req.getParameter("nexttask");//下一办理环节
			String nextUserId = req.getParameter("nextuserid");//下一办理环节参与者,以逗号分隔userid
			String instProcessId = req.getParameter("instprocessid");//流程实例ID
			String instActivityId = req.getParameter("instactivityid");//活动实例ID
			String instWorkitemId = req.getParameter("instworkitemid");//代办件ID
			log.debug("borrowsubmit nexttask="+nexttask);
			log.debug("borrowsubmit nextUserId="+nextUserId);
			log.debug("borrowsubmit instProcessId="+instProcessId);
			log.debug("borrowsubmit instActivityId="+instActivityId);
			log.debug("borrowsubmit instWorkitemId="+instWorkitemId);
			String instanceId = bi.getId();
			String processId = "borrow";
			String type = "借款流程";
			IUser loginUser = Util.getLoginUser();
			String startUserId = loginUser.getId();
			String title = loginUser.getName()+" 借款单[金额:"+bi.getAmount()+"]";
			String url = "/borrow.action";
			HashMap hmap = new HashMap();
			hmap.put("amount", bi.getAmount());
			String flag = "gf";
			String testMode = "no";
			List<String> userIdLst = new ArrayList<String>();
			if(nextUserId.endsWith(","))
				nextUserId = nextUserId.substring(0, nextUserId.length()-1);
			String[] dim = nextUserId.split(",");
			for(String s:dim)
			{
				userIdLst.add(s);
			}
			
			//业务对象ID为空，代表新建流程
			if("".equals(Util.fmtStr(instanceId)))
			{
				instanceId = UUID.create("borrow");
				bi.setId(instanceId);
				serv.insert(bi);
				msg = wapi.startWorkflow(processId, startUserId, userIdLst, instanceId, type, title, 
						nexttask, url, hmap, flag, testMode);
			}
			else//从代办件列表进入，提交流程
			{
				serv.updateById(bi);
				msg = wapi.submitWorkflow(instProcessId, instActivityId, instWorkitemId, 
						userIdLst, instanceId, type, title, nexttask, url, hmap, 
						flag, testMode);
			}
			if(req instanceof MultipartHttpServletRequest)
			{
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
				MultipartFile multipartFile = mreq.getFile("uploadfile");
				if(!"".equals(Util.fmtStr(multipartFile.getOriginalFilename())))
				{
					InputStream is = multipartFile.getInputStream();
					byte[] data = FileCopyUtils.copyToByteArray(is);
					String name = multipartFile.getOriginalFilename();
					String contextType = multipartFile.getContentType();
					Long length = new Long(data.length);
					instanceId = bi.getId();
					AttInfo att = new AttInfo();
					String id = UUID.create("attachement");
					att.setId(id);
					att.setName(name);
					att.setContextType(contextType);
					att.setLength(length);
					att.setInstanceId(instanceId);
					att.setContent(data);
					attserv.saveAtt(att);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return msg;
	}
	
	@RequestMapping("/borrowattload.action")
	@ResponseBody
	public List borrowattload(HttpServletRequest req,String instanceid)
	{
		List<AttInfo> lst = attserv.getAttByInstanceId(instanceid);
		return lst;
	}
}
