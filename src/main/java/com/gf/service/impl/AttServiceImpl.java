package com.gf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gf.mapper.MainMapper;
import com.gf.model.AttInfo;
import com.gf.service.AttService;

@Service
public class AttServiceImpl implements AttService{
	@Autowired
	private MainMapper mapper;

	@Override
	public void saveAtt(AttInfo ai) {
		try
		{
			mapper.saveAtt(ai);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public AttInfo getAttById(String id) {
		try
		{
			return mapper.getAttById(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AttInfo> getAttByInstanceId(String instanceId) {
		try
		{
			return mapper.getAttByInstanceId(instanceId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteAttById(String id) {
		try
		{
			mapper.deleteAttById(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAttByInstanceId(String instanceId) {
		try
		{
			mapper.deleteAttByInstanceId(instanceId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
