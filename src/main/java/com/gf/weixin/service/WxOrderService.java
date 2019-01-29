package com.gf.weixin.service;

import com.baomidou.mybatisplus.service.IService;
import com.gf.weixin.bean.WeiXinAccount;
import com.gf.weixin.bean.WxOrderInfo;

public interface WxOrderService extends IService<WxOrderInfo>{
	public WxOrderInfo generateOrder(String openId,Integer total,Integer transportFree,String prdtName);
}
