package com.gf.weixin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gf.weixin.bean.WeiXinAccount;
import com.gf.weixin.mapper.AccountMapper;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper,WeiXinAccount>
	implements AccountService
{
	public String getAccessToken()
	{
		Wrapper<WeiXinAccount> wrap = new EntityWrapper<WeiXinAccount>();
		List<WeiXinAccount> lst = selectList(wrap);
		if(lst!=null && lst.size()>0)
			return lst.get(0).getToken();
		return null;
	}
}
