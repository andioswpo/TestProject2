package com.gf.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gf.mapper.MainMapper;
import com.gf.service.FunctionService;
import com.gf.statusflow.IOrgModel;
import com.gf.statusflow.def.FunctionInfo;

@Service
public class FunctionServiceImpl implements FunctionService{
	private Logger log = LoggerFactory.getLogger(FunctionServiceImpl.class);
	@Autowired
	private IOrgModel orgmodel;

	@Override
	public FunctionInfo getRoot() {
		try
		{
			return orgmodel.getRootFunc();
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<FunctionInfo> getMenu(String id) {
		try
		{
			return orgmodel.getChildFunc(id);
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	

	/**
		<div id="sysadmin" style="width:150px;">
			<div>
				<span>组织管理</span>
				<div>
					<div>
						<span>用户管理</span>
						<div>
							<div>修改密码</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>	
		<div id="corp" style="width:150px;">
		</div>
	 */
	@Override
	public void recurseFunction(String userId,String id, StringBuffer sb,
			List<FunctionInfo> aclList) 
	{
		List<FunctionInfo> child = getMenu(id);
		for(FunctionInfo fi:child)
		{
			if(hasPrivilege(userId,fi,aclList))
			{
				sb.append("<div data-options=\"iconCls:'"+fi.getIcon()+"'\" onclick='javascript:addTabCheckExist(\""+fi.getName()+"\",\""+fi.getUrl()+"\",\""+fi.getIcon()+"\");'>\r\n");
				List<FunctionInfo> child2 = getMenu(fi.getId());
				if(child2 != null && child2.size()>0)
				{
					sb.append("<span>"+fi.getName()+"</span>\r\n");
					sb.append("<div>\r\n");
					recurseFunction(userId,fi.getId(),sb,aclList);
					sb.append("</div>\r\n");
				}
				else
				{
					sb.append(fi.getName()+"\r\n");
				}
				sb.append("</div>\r\n");
			}
		}
	}

	/**
	<div class="easyui-panel" style="padding:5px;">
		<a href="#" class="easyui-linkbutton" data-options="plain:true">Home</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#mm1',iconCls:'icon-edit'">Edit</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#mm2',iconCls:'icon-help'">Help</a>
		<a href="#" class="easyui-menubutton" data-options="menu:'#mm3'">About</a>
	</div>
	 */
	@Override
	public String generateMenuBar(String userId) {
		FunctionInfo root = getRoot();
		if(root != null)
		{
			List<FunctionInfo> aclList = orgmodel.getFuncListByUserId(userId);
			List<FunctionInfo> child = getMenu(root.getId());
			StringBuffer sb = new StringBuffer();
			sb.append("<div class=\"easyui-panel\" style=\"height:30px;padding-bottom:0px;background:#B3DFDA;\">\r\n");
			StringBuffer sb2 = new StringBuffer();
			for(FunctionInfo fi:child)
			{
				if(hasPrivilege(userId,fi,aclList))
				{
					String id = fi.getId();
					String icon = fi.getIcon();
					String name = fi.getName();
					sb.append("<a class=\"easyui-menubutton\" data-options=\"menu:'#"+id+"',iconCls:'"+icon+"'\">"+name+"</a>\r\n");
					sb2.append("<div id=\""+id+"\" style=\"width:150px;\">\r\n");
					recurseFunction(userId,id,sb2,aclList);
					sb2.append("</div>\r\n");
				}
			}
			sb.append("<a class=\"easyui-linkbutton\" style=\"background:#B3DFDA;\" data-options=\"plain:true,iconCls:'pic_25'\" onclick=\"logout()\">退出系统</a>\r\n");
			sb.append("</div>\r\n");
			String menu = sb.toString()+sb2.toString();
			return menu;
		}
		return "";
	}

	public boolean hasPrivilege(String userId,FunctionInfo fi,List<FunctionInfo> aclList)
	{
		//如果是超级用户不受权限控制
		if(IOrgModel.I_SYSADMIN_ID.equals(userId))
			return true;
		//第一种情况：权限列表aclList中存在元素的Path包含fi.path
		for(FunctionInfo aclFi:aclList)
		{
			if(fi.getPath().startsWith(aclFi.getPath()))
				return true;
		}
		//第二种情况：fi.path 包含在权限列表aclList中
		for(FunctionInfo aclFi:aclList)
		{
			if(aclFi.getPath().startsWith(fi.getPath()))
				return true;
		}
		return false;
	}
}
