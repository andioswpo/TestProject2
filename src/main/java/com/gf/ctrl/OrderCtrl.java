package com.gf.ctrl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gf.model.AddressInfo;
import com.gf.model.OrderInfo;
import com.gf.model.OrderItemInfo;
import com.gf.service.OrderService;
import com.gf.statusflow.UUID;
import com.gf.statusflow.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
public class OrderCtrl {
	@Autowired
	private OrderService serv;
	
	@RequestMapping(value="/order.action")
	public String list()
	{	
		return "ordervue";
	}
	
	@RequestMapping("/orderagl.action")
	public String orderagl()
	{
		return "orderagl";
	}
	
	@RequestMapping("/orderangular.action")
	public String orderangula()
	{
		return "orderangular";
	}
	
	@RequestMapping("/province.action")
	@ResponseBody
	public List<AddressInfo> province()
	{
		return serv.getProviceList();
	}
	
	@RequestMapping("/city.action")
	@ResponseBody
	public List<AddressInfo> city(String pcode)
	{
		return serv.getCityList(pcode);
	}

	@RequestMapping("/county.action")
	@ResponseBody
	public List<AddressInfo> county(String city)
	{
		return serv.getCountyList(city);
	}
	
	@RequestMapping("/orderlist.action")
	@ResponseBody
	public Map<String,Object> orderlist(Integer page,Integer rows)
	{
		System.out.println("page==="+page+",rows==="+rows);
		if(page == null)
			page = 1;
		if(rows == null)
			rows = 4;
		Map<String,Object> m = new HashMap<String,Object>();
		PageHelper.startPage(page, rows);
		List<OrderInfo> list = serv.findOrder();
		PageInfo<OrderInfo> pi = new PageInfo<OrderInfo>(list);
		m.put("total",pi.getTotal());
		m.put("rows", pi.getList());
		
		return m;
	}
	
	@RequestMapping("/loadorder.action")
	@ResponseBody
	public OrderInfo loadorder(String id)
	{
		OrderInfo oi = serv.findOrderById(id);
System.out.println("id==="+id+",oi="+oi);
		List<OrderItemInfo> items = serv.findOrderItem(oi.getId());
		oi.setItems(items);
		return oi;
	}
	
	@RequestMapping("/ordersave.action")
	@ResponseBody
	public OrderInfo ordersave(HttpServletRequest req)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStream is = req.getInputStream();
			FileCopyUtils.copy(is, baos);
			String json = new String(baos.toByteArray());
			//{id:'1',name:'Java',items:[{},{}]}
			JSONObject jsonObj = JSONObject.parseObject(json);
			OrderInfo oi = (OrderInfo)JSONObject.toJavaObject(jsonObj, OrderInfo.class);
			String id = oi.getId();
			System.out.println("Id="+id);
			if("".equals(Util.fmtStr(id)))
			{
				id = UUID.create("order");
				oi.setId(id);
				serv.saveOrder(oi);
				List<OrderItemInfo> items = oi.getItems();
				for(OrderItemInfo oii:items)
				{
					oii.setId(UUID.create("oitem"));
					oii.setOrderId(id);
					Integer num = oii.getNum();
					BigDecimal price = oii.getPrice();
					BigDecimal amount = price.multiply(new BigDecimal(num));
					oii.setAmount(amount);
					serv.saveOrderItem(oii);
				}
			}
			else
			{
				serv.updateOrder(oi);
				serv.deleteOrderItem(id);
				List<OrderItemInfo> items = oi.getItems();
				for(OrderItemInfo oii:items)
				{
					oii.setId(UUID.create("oitem"));
					oii.setOrderId(id);
					Integer num = oii.getNum();
					BigDecimal price = oii.getPrice();
					BigDecimal amount = price.multiply(new BigDecimal(num));
					oii.setAmount(amount);
					serv.saveOrderItem(oii);
				}
			}
			return oi;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		List lst = new ArrayList();
		lst.add("Java");
	}
}
