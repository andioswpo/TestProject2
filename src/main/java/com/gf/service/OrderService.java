package com.gf.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gf.model.AddressInfo;
import com.gf.model.OrderInfo;
import com.gf.model.OrderItemInfo;

public interface OrderService {
	public OrderInfo findOrderById(String id);
	public List<OrderInfo> findOrder();
	public List<OrderItemInfo> findOrderItem(String orderId);
	public void deleteOrderItem(String orderId);
	public void saveOrder(OrderInfo oi);
	public void updateOrder(OrderInfo oi);
	public void saveOrderItem(OrderItemInfo oi);
	public void updateOrderItem(OrderItemInfo oi);
	public List<AddressInfo> getProviceList();
	public List<AddressInfo> getCityList(String pcode);
	public List<AddressInfo> getCountyList(String city);
}
