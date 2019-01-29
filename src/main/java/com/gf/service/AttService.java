package com.gf.service;

import java.util.List;

import com.gf.model.AttInfo;

public interface AttService {
	public void saveAtt(AttInfo ai);
	public AttInfo getAttById(String id);
	public List<AttInfo> getAttByInstanceId(String instanceId);
	public void deleteAttById(String id);
	public void deleteAttByInstanceId(String instanceId);
}
