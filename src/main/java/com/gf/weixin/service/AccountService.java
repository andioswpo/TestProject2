package com.gf.weixin.service;

import com.baomidou.mybatisplus.service.IService;
import com.gf.weixin.bean.WeiXinAccount;

public interface AccountService extends IService<WeiXinAccount>{
	public String getAccessToken();
}
