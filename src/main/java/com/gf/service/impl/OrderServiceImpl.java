package com.gf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gf.mapper.MainMapper;
import com.gf.model.AddressInfo;
import com.gf.model.OrderInfo;
import com.gf.model.OrderItemInfo;
import com.gf.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private MainMapper mapper;
	
	public OrderInfo findOrderById(String id)
	{
		try
		{
			return mapper.findOrderById(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<OrderInfo> findOrder() {
		try
		{
			return mapper.findOrder();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<OrderItemInfo> findOrderItem(String orderId) {
		try
		{
			return mapper.findOrderItem(orderId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@Override
	public void saveOrder(OrderInfo oi) {
		try
		{
			mapper.saveOrder(oi);
			
			//sql1
			//sql2
			//sql3
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void updateOrder(OrderInfo oi) {
		try
		{
			mapper.updateOrder(oi);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void saveOrderItem(OrderItemInfo oi) {
		try
		{
			mapper.saveOrderItem(oi);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void updateOrderItem(OrderItemInfo oi) {
		try
		{
			mapper.updateOrderItem(oi);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void deleteOrderItem(String orderId)
	{
		try
		{
			mapper.deleteOrderItem(orderId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<AddressInfo> getProviceList()
	{
		try
		{
			return mapper.getProviceList();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public List<AddressInfo> getCityList(String pcode)
	{
		try
		{
			String subCode = pcode.substring(0,2);
			return mapper.getCityList(subCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public List<AddressInfo> getCountyList(String city)
	{
		try
		{
			String subCode = city.substring(0,4);
			return mapper.getCountyList(subCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
