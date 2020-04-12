package com.biboheart.huip.reservation.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.reservation.domain.Device;

public interface DeviceService {
	public Device save(Device device) throws BhException;
	
	public Device delete(Integer id);
	
	public Device load(Integer id, String sn);
	
	public List<Device> list(List<Integer> inIdList, String match);
	
	public Page<Device> find(List<Integer> inIdList, String match);
}
