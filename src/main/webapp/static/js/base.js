/**
 * 获取所有选中Checkbox的值，以逗号分隔
 * @param objname CheckBox控件的name属性
 * @returns {String}
 */
function getCheckedId(objname)
{
	var rtnVal = '';
	var allCheck = document.getElementsByTagName("input"); 
	for(var i=0; i<allCheck.length; i++) 
	{ 
		if(allCheck[i].type=="checkbox")
		{
			if(allCheck[i].name == objname && allCheck[i].checked)
				rtnVal = rtnVal + allCheck[i].value + ',';
		}
	}
	return rtnVal;
}
/**
 * 根据Checkbox的名称chkname，全选或反选
 * @param chkname
 */
function selectCheckedOrNotByName(chkname)
{
	$('input[name="'+chkname+'"').each(function()
		{
			this.checked = !this.checked;
		}
	);
}
/**
 * 根据传入CheckBox的值选中Check控件
 * @param chkname CheckBox的名称
 * @param val 传入的值，多值使用逗号分隔
 */
function setCheckValByName(chkname,val)
{
	
	$('input[name="'+chkname+'"').each(function()
	{
		if(val.indexOf(this.value)>=0)
			this.checked = true;
		else
			this.checked = false;
	});
}
/**
 * 判断数值oNum是否为数字
 * @param oNum
 * @returns {Boolean}
 */
function isNumber(oNum) 
{ 
	if(!oNum)
		return false;
	if(oNum == "0")
		return true;
	var strP=/^[0-9]*[1-9][0-9]*$/; 
	if(!strP.test(oNum))
		return false;
	try
	{ 
		if(parseFloat(oNum)!=oNum)
			return false; 
	} 
	catch(ex) 
	{ 
		return false; 
	} 
	return true; 
}
/**
 * 判断数值oNum是否为浮点数字
 * @param oNum
 * @returns {Boolean}
 */
function isFloat(oNum) 
{ 
	if(!oNum)
		return false;
	if(oNum == "0")
		return true;
	var strP=/^((-?|\+?)\d+)(\.\d+)?$/;
	if(!strP.test(oNum))
		return false;
	try
	{ 
		if(parseFloat(oNum)!=oNum)
			return false; 
	} 
	catch(ex) 
	{ 
		return false; 
	} 
	return true; 
}
/**
 * 判断参数str是否为日期类型yyyy-MM-dd
 * @param str
 * @returns {Boolean}
 */
function isDate(str){  
	var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
	var arr=reg.exec(str);  
	if(str=="")
		return false;
	if(!reg.test(str))  
		return false;  
	return true;  
}
/**
 * 根据Select控件的ID，获取选中的value值，使用Jquery类库
 * @param selId
 * @returns
 */
function getSelectedValById(selId)
{
	return $("#"+selId+" option:selected").val();
}
/**
 * 根据Select控件的ID，获取选中的text值，使用Jquery类库
 * @param selId
 * @returns
 */
function getSelectedTextById(selId)
{
	return $("#"+selId+" option:selected").text();
}
/**
 * 向Select控件添加数据，数据参数data是集合对象，集合中每个对象
 * 包含属性id和name
 * @param selId
 * @param data
 */
function fillSelectDataById(selId,data)
{
	for(var i=0;i<data.length;i++)
	{
		$("#"+selId).append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
	}
}
/**
 * 根据value选中id为selId的Select控件
 * @param selId
 * @param val
 */
function setSelectedValById(selId,val)
{
	$("#"+selId).val(val);	
}
/**
 * VUE前台分页
 */
function vuepagediv(total,pageSize,page,js)
{
	//alert('total='+total+',pageSize='+pageSize);
	var totalPage = 0;
	if(total%pageSize == 0)
		totalPage=total/pageSize;
	else
		totalPage=total/pageSize + 1;
	var html = '<div id="pagediv">';
	html = html + '<a href="javascript:'+js+'(1,'+pageSize+')">首页</a>';
	for(var i=1;i<totalPage;i++)
		html = html + '<a href="javascript:'+js+'('+i+','+pageSize+')">'+i+'页</a>';
	html = html + '<a href="javascript:'+js+'('+totalPage+','+pageSize+')">尾页</a>';
	return html;
}
/**
 * 使用VUE前台分页公共方法
 * @param js 点击页面调用JS方法
 * @param page 查询页码
 * @param rows 每页显示记录数
 * @param total 总记录数
 * @returns {String}
 */
function pagedivjs(js,page,rows,total)
{
	var pages = 0;
	if(total%rows==0)
		pages = parseInt(total/rows);
	else
		pages = parseInt(total/rows) + 1;
	var pageDiv = "<div id='pagediv'>";
	page=1;
	pageDiv = pageDiv + "<a href='javascript:"+js+"("+page+","+rows+");'>首页</a>";
    page = (page==1)?1:(page-1);
    pageDiv = pageDiv + "<a href='javascript:"+js+"("+page+","+rows+");'>上一页</a>";
    for(var i=1;i<=pages;i++)
    {
        if(i == page)
        	pageDiv = pageDiv + "<a href='javascript:"+js+"("+i+","+rows+");'><b><font color='red'>"+i+"</font></b></a>&nbsp;";
        else
        	pageDiv = pageDiv + "<a href='javascript:"+js+"("+i+","+rows+");'>"+i+"</a>&nbsp;";
    }
    page = (page==pages)?pages:(page+1);
    pageDiv = pageDiv + "<a href='javascript:"+js+"("+page+","+rows+");'>下一页</a>";
    //定义尾页连接
    page = pages;
    pageDiv = pageDiv + "<a href='javascript:"+js+"("+page+","+rows+");'>尾页</a>";
    pageDiv = pageDiv + "&nbsp;&nbsp;&nbsp;&nbsp;第"+page+"/"+pages+"页";
    pageDiv = pageDiv + "&nbsp;&nbsp;&nbsp;&nbsp;共"+total+"条记录";
    pageDiv = pageDiv + "</div>";
    return pageDiv;
}

