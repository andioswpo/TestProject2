package com.gf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gf.model.AddressInfo;
import com.gf.model.AttInfo;
import com.gf.model.OrderInfo;
import com.gf.model.OrderItemInfo;
import com.gf.statusflow.def.DefaultOrg;
import com.gf.statusflow.def.FunctionInfo;

@Mapper
public interface MainMapper {
	public void saveAtt(AttInfo ai);
	public AttInfo getAttById(@Param("id") String id);
	public List<AttInfo> getAttByInstanceId(@Param("instanceId") String instanceId);
	public void deleteAttById(@Param("id") String id);
	public void deleteAttByInstanceId(@Param("instanceId") String instanceId);
	
	public OrderInfo findOrderById(@Param("id") String id);
	public List<OrderInfo> findOrder();
	public List<OrderItemInfo> findOrderItem(@Param("orderId") String orderId);
	public void deleteOrderItem(@Param("orderId") String orderId);
	public void saveOrder(OrderInfo oi);
	public void updateOrder(OrderInfo oi);
	public void saveOrderItem(OrderItemInfo oi);
	public void updateOrderItem(OrderItemInfo oi);
	public List<AddressInfo> getProviceList();
	public List<AddressInfo> getCityList(@Param("pcode") String pcode);
	public List<AddressInfo> getCountyList(@Param("city") String city);
}
