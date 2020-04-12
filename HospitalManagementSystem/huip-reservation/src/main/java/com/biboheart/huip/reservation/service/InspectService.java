package com.biboheart.huip.reservation.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.reservation.domain.Inspect;

public interface InspectService {
	public Inspect save(Inspect inspect) throws BhException;
	
	public Inspect delete(Integer id, String sn, Long pat, Integer device, Integer project);
	
	public Inspect load(Integer id, String sn, Long pat, Integer device, Integer project);
	
	public List<Inspect> list(Long start, Long end, List<Long> inPatList, List<Integer> inDeviceList, List<Integer> inProjectList, String match);
	
	public Page<Inspect> find(Long start, Long end, List<Long> inPatList, List<Integer> inDeviceList, List<Integer> inProjectList, String match);
}
