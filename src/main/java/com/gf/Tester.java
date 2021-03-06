package com.gf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gf.model.AddressInfo;
import com.gf.model.AttInfo;
import com.gf.model.BorrowInfo;
import com.gf.model.ExpenseInfo;
import com.gf.model.HolidayInfo;
import com.gf.service.AttService;
import com.gf.service.BorrowService;
import com.gf.service.ExpenseService;
import com.gf.service.FunctionService;
import com.gf.service.HolidayService;
import com.gf.service.OrderService;
import com.gf.statusflow.IOrg;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.StatusFlowData;
import com.gf.statusflow.StatusFlowMng;
import com.gf.statusflow.StatusFlowWAPI;
import com.gf.statusflow.StatusMsg;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;
import com.gf.statusflow.def.DefWorkItem;
import com.gf.statusflow.def.FunctionInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Starter.class)
public class Tester
{
	@Autowired
	private StatusFlowWAPI wapi;
	@Autowired
	private IOrgModel orgmodel;
	@Autowired
	private StatusFlowMng sfmng;
	@Autowired
	private HolidayService hldserv;
	@Autowired
	private BorrowService brwserv;
	@Autowired
	private AttService attserv;
	@Autowired
	private OrderService serv;
	
	@Test
	public void testAddress()
	{
		List<AddressInfo> plist = serv.getProviceList();
		for(AddressInfo ai:plist)
		{
			System.out.println("ai.name="+ai.getName());
			List<AddressInfo> cityList = serv.getCityList(ai.getCode());
			for(AddressInfo ai2:cityList)
			{
				System.out.println("ai2.name="+ai2.getName());
				List<AddressInfo> countyList = serv.getCountyList(ai2.getCode());
				for(AddressInfo ai3:countyList)
					System.out.println("ai3.name="+ai3.getName());
			}
		}
	}
	
	//@Test
	public void testAtt()
	{
		try
		{
			AttInfo att = new AttInfo();
			String id = UUID.create("attachement");
			att.setId(id);
			att.setName("test.png");
			att.setContextType("image/png");
			String file = "h:/qxnlogo.png";
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int pos = 0;
			while((pos=fis.read(data))!=-1)
			{
				baos.write(data,0,pos);
			}
			fis.close();
			byte[] imgdata = baos.toByteArray();
			att.setLength((long)imgdata.length);
			att.setContent(imgdata);
			att.setInstanceId("1");
			//attserv.saveAtt(att);
			
			AttInfo att2 = attserv.getAttById("attad466bf8489f0");
			byte[] imgdata2 = att2.getContent();
			FileOutputStream fos = new FileOutputStream("h:/test.png");
			fos.write(imgdata2);
			fos.close();
			
			List<AttInfo> lst = attserv.getAttByInstanceId("borr650c69ac8a71");
			System.out.println(lst);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
    //@Test
    public void testBorrow()
    {
    	BorrowInfo bi = new BorrowInfo();
    	bi.setId(UUID.create("borrow"));
    	bi.setName("出差借款2000元");
    	bi.setTs(Timestamp.valueOf("2018-12-20 12:20:00"));
    	bi.setUserId("1");
    	bi.setAmount(new BigDecimal(2000.00));
    	brwserv.insert(bi);
    	
    	Wrapper wrap = new EntityWrapper<BorrowInfo>();
    	wrap.eq("userId", "1");
    	List lst = brwserv.selectList(wrap);
    	System.out.println("testBorrow list="+lst);
    }
	
    //@Test
    public void testMethod()
    {
    	try
    	{
    		//1、打开业务页面时，检索工作流核心数据传递到前台
    		String userId = "1";
    		String processId = "expense";
    		String instanceId = null;//业务表单的主键
    		String instProcessId = null;//流程实例ID
    		String instActivityId = null;//流程实例ActivityId
    		String instWorkitemId = null;//工作代办件ID
    		String nexttask = null;//下一办理环节ID
	    	String flag = "gf";//区分客户标识，每一个用户分配统一的标识
	    	String testMode = "no";//是否进入测试模式
    		//通过工作流接口获取个人代办工作列表
    		List<DefWorkItem> wkList = wapi.getWorkitemList(userId, testMode);
    		for(DefWorkItem dwi:wkList)
    		{
    			if(processId.equals(dwi.getProcessId()))
    			{
    				instanceId = dwi.getInstanceId();
    				instProcessId = dwi.getInstProcessId();
    				instActivityId = dwi.getInstActivityId();
    				instWorkitemId = dwi.getId();
    			}
    		}
    		//instWorkitemId 为空  新建业务表单入口
    		//instWorkitemId 不为空  从代办列表进入业务表单
    		StatusFlowData sfdata = sfmng.getWorkflowData(userId, processId,instWorkitemId);
    		System.out.println(sfdata.getNextBtnList());
	    	//通过Request对象传递sfdata.getNextBtnList()到前台页面
	    	//数据形式为:[
    		//		{name=财务审批, actor=user3aa650d08695,财务总监, 
    		//			js=wfsubmit('account','user3aa650d08695,财务总监',''), id=account}
    		//	]		
    		
    		//2、前台提交数据到后台Controller，调用流程接口
    		//代办列表中无记录，需要新建流程
    		if("".equals(Util.fmtStr(instanceId)))
    		{
    			//模拟业务数据保持后id=1
    			instanceId = "1";
    			
    	    	String startUserId = "1";//流程实例启动用户
    	    	String type = "报销单";//流程类型，通常取流程模板名称
    	    	String title = "UserA的报销单";//流程实例标题
    	    	String url = "/expense.action";//业务模块应用链接
    	    	HashMap hmap = new HashMap();//工作流实例相关数据
    	    	hmap.put("days", 5);

    	    	
    	    	//通过前台页面点击不同按钮，调用wfsubmit JS方法传递到后台
    	    	//通过Request.getParameter("nextUserId")获取,userId间以逗号分隔
    	    	//后台分割userId转存到List<String>
    	    	List<String> userIdLst = new ArrayList<String>();
    	    	userIdLst.add("1");//模拟测试，使用超级用户ID
    	    	//通过Request.getParameter("nexttask")获取
    	    	nexttask = "account";
    	    	//创建工作流实例
    	    	wapi.startWorkflow(processId, startUserId, userIdLst, instanceId, type, 
    	    			title, nexttask, url, hmap, flag, testMode);
    	    	//wapi.setVariable("days","2")
    		}
    		//通过代办列表进入业务页面，已经存在流程实例
    		else
    		{        		
    	    	String type = "报销单";//流程类型，通常取流程模板名称
    	    	String title = "UserA的报销单";//流程实例标题
    	    	String url = "/expense.action";//业务模块应用链接
    	    	HashMap hmap = new HashMap();//工作流实例相关数据
    	    	hmap.put("days", 5);
    	    	
    	    	//通过前台页面点击不同按钮，调用wfsubmit JS方法传递到后台
    	    	//通过Request.getParameter("nextUserId")获取,userId间以逗号分隔
    	    	//后台分割userId转存到List<String>
    	    	List<String> userIdLst = new ArrayList<String>();
    	    	userIdLst.add("1");//模拟测试，使用超级用户ID
    	    	//通过Request.getParameter("nexttask")获取
    	    	//nexttask = "vp";
    	    	List<Properties> nextBtnList = sfdata.getNextBtnList();
    	    	Properties prop = nextBtnList.get(0);
    	    	nexttask = prop.getProperty("id");
    	    	
        		wapi.submitWorkflow(instProcessId, instActivityId, instWorkitemId, 
        				userIdLst, instanceId, type, title, nexttask, url, 
        				hmap, flag, testMode);
    		}
    		
    		/**
    		Wrapper<HolidayInfo> wrap = new EntityWrapper<HolidayInfo>();
    		wrap.eq("days", 2);
    		wrap.and().eq("userid", "1");
    		List<HolidayInfo> list = hldserv.selectList(wrap);
    		for(HolidayInfo hdi2:list)
    		{
    			System.out.println("hdi2="+hdi2.getId()+",days="+hdi2.getDays());
    			hdi2.setUserId("1");
    			hldserv.updateById(hdi2);
    		}
    		hldserv.delete(wrap);
    		*/
	    }
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
