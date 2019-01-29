package com.gf.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gf.mapper.BorrowMapper;
import com.gf.model.BorrowInfo;
import com.gf.service.BorrowService;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper,BorrowInfo>
	implements BorrowService
{

}
