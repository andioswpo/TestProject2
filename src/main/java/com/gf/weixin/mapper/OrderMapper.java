package com.gf.weixin.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gf.weixin.bean.WeiXinAccount;
import com.gf.weixin.bean.WxOrderInfo;

@Mapper
public interface OrderMapper extends BaseMapper<WxOrderInfo>{

}
