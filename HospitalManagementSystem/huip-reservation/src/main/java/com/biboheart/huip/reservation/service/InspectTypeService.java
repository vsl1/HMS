package com.biboheart.huip.reservation.service;

import java.util.List;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.reservation.domain.InspectType;

public interface InspectTypeService {
	public InspectType save(InspectType it) throws BhException;
	
	public InspectType delete(Integer id);
	
	public InspectType load(Integer id);
	
	public List<InspectType> list();
}
