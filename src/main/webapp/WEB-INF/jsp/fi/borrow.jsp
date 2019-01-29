<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript" src="/static/easyui/jquery.min.js"></script>
<script type="text/javascript" src="/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/easyui/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="/static/easyui/themes/default/easyui.css"/>
<link rel="stylesheet" href="/static/easyui/themes/icon.css"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>借款流程</title>
<script>
//nexttask 工作流模板里定义活动ID
//nextuserid userid,username;userid,username 工作流模板定义的活动参与者
//nextuserid = fiadmin,财务总监;sysadmin,超级管理员
function wfsubmit(nexttask,nextuserid)
{
	//表单校验
	var rtn = $('#frm').form('validate');
	if(!rtn)
		return;
	var userId = '';
	//结束流程活动ID，不需要参与者
	if('end' != nexttask && 'endall' != nexttask && 'destory' != nexttask)
	{
		//工作流模板其他活动节点必须配置参与者
		if(nextuserid == null || nextuserid == '')
		{
			$.messager.alert('错误提示','下一办理人为空!','error');
			return;
		}
		//解析nextuserid
		var objdim = nextuserid.split(";");
		for(var k=0;k<objdim.length;k++)
		{
			var objdim2 = objdim[k].split(",");
			if(objdim2[0] == null || objdim2[0] == '')
			{
				$.messager.alert('错误提示','下一办理人为空!','error');
				return;
			}
			userId = userId + objdim2[0] + ',';
		}
	}
	var rtn = $('#frm').form('validate');
	if(rtn)
	{
		//下一环节
		$('#nexttask').val(nexttask);
		//赋值表单隐藏域，代表下一环节参与者
		$('#nextuserid').val(userId);
		$('#frm').form('submit', {    
		    url:'/borrowsubmit.action',    
		    onSubmit: function(){    
	  
		    },    
		    success:function(data){
		    	var dataObj = JSON.parse(data);
		    	$('#buttondiv').empty();
		    	loadimg(dataObj.instProcessId,dataObj.instanceId);
		    	$('#attdg').datagrid({
		    		url:'/borrowattload.action?instanceid='+dataObj.instanceId
		    	});
		    }
		});
	}
}
function loadimg(instprocessid,instanceid)
{
	var url = $("#wfuiimg").attr('src');
	url = '/downimg.jsp?width=800&height=100&processid=borrow&instprocessid='+instprocessid+'&instanceid='+instanceid+'&r='+ Math.random();
	$("#wfuiimg").prop('src', url);
}
function showimg(cellVal,rowObj,rowNo)
{
	var html = '<img src="/downfile?attid='+rowObj.id+'" width="100px" height="100px"/>';
	return html;
}
</script>
</head>
<body class="easyui-layout">
    <div data-options="region:'center',title:'借款单'" style="overflow:hidden;padding:5px;">
    	<div >
    		<img id="wfuiimg" height="100" width="800" src="/downimg.jsp?width=800&height=100&processid=borrow&instprocessid=${instprocessid}&instanceid=${bi.id}&t=<%=new Date()%>"/>
    	</div>
		<form id="frm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="instprocessid" name="instprocessid" value="${instprocessid}"/>
			<input type="hidden" id="instactivityid" name="instactivityid" value="${instactivityid}"/>
			<input type="hidden" id="instworkitemid" name="instworkitemid" value="${instworkitemid}"/>
			<input type="hidden" id="id" name="id" value="${bi.id}"/>
			<input type="hidden" id="nexttask" name="nexttask"/>
			<input type="hidden" id="nextuserid" name="nextuserid"/>
			<input type="hidden" id="userId" name="userId" value="${bi.userId}"/>
		    <div style="margin-left:50px;margin-top:30px">   
		        <input class="easyui-textbox" type="text" id="name" name="name" value="${bi.name}" data-options="width:300,label:'请款名称:'" />   
		    </div>   
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-datetimebox" type="text" id="ts" name="ts" value="${bi.ts}" data-options="width:300,label:'时间:'" />   
		    </div>
		    <div style="margin-left:50px;margin-top:30px"> 
		        <input class="easyui-textbox" type="text" id="amount" name="amount" value="${bi.amount}" data-options="width:300,label:'金额(单位:分):'" />   
		    </div>
  		    <div id="buttondiv" style="margin-left:50px;margin-top:30px">
<%
			List<Properties> button = (List<Properties>)request.getAttribute("button");
			for(Properties prop:button)
			{
				String name = prop.getProperty("name");
				String js = prop.getProperty("js");
%>
				<a id="btn" onclick="<%=js %>" class="easyui-linkbutton"><%=name %></a>
<%
			}
%>  
		    </div>
		    
			<table id="attdg" class="easyui-datagrid" style="width:100%;height:250px"   
			        data-options="url:'/borrowattload.action?instanceid=${bi.id}',fitColumns:true,
			        singleSelect:true,toolbar: '#tb'">   
			    <thead>   
			        <tr>
			        	<th data-options="field:'id',width:100,hidden:true">ID</th>
			            <th data-options="field:'name',width:100">名称</th>
			            <th data-options="field:'length',width:100">大小</th>  
			            <th data-options="field:'ts',width:100">上传时间</th>
			            <th data-options="field:'contextType',width:100">文件类型</th>  
						<th data-options="field:'img',width:100,formatter:showimg">图片</th>  
			        </tr>   
			    </thead>   
			</table>
			<div id="tb">
				<input name="uploadfile" class="easyui-filebox" style="width:300px">
			</div>

		</form>
    </div>
</body>
</html>