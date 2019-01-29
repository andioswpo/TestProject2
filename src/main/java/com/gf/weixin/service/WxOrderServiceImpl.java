package com.gf.weixin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gf.statusflow.UUID;
import com.gf.weixin.bean.WeiXinAccount;
import com.gf.weixin.bean.WxOrderInfo;
import com.gf.weixin.mapper.AccountMapper;
import com.gf.weixin.mapper.OrderMapper;

@Service
public class WxOrderServiceImpl extends ServiceImpl<OrderMapper,WxOrderInfo>
	implements WxOrderService
{
	@Autowired
	private OrderMapper mapper;
	
	public WxOrderInfo generateOrder(String openId,Integer total,Integer transportFree,
			String prdtName)
	{
		WxOrderInfo ord = new WxOrderInfo();
		ord.setId(UUID.create("order"));
		ord.setTotal(total);
		ord.setOpenId(openId);
		ord.setTransportFree(transportFree);
		ord.setAllTotal(total+transportFree);
		ord.setPrdtName(prdtName);
		ord.setNo(UUID.create("ORDER"));
		mapper.insert(ord);
		return ord;
	}
}
