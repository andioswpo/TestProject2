package com.gf.weixin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gf.weixin.bean.WeiXinAccount;

@Mapper
public interface AccountMapper extends BaseMapper<WeiXinAccount>{

}
