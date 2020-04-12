package com.biboheart.huip.reservation.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.reservation.domain.InspectType;
import com.biboheart.huip.reservation.repository.InspectTypeRepository;
import com.biboheart.huip.reservation.service.InspectTypeService;

@Service
public class InspectTypeServiceImpl implements InspectTypeService {
	@Autowired
	private InspectTypeRepository inspectTypeRepository;

	@Override
	public InspectType save(InspectType it) throws BhException {
		if (null == it.getId()) {
			it.setId(0);
		}
		if (CheckUtils.isEmpty(it.getSn())) {
			throw new BhException("Number cannot be null");
		}
		if (CheckUtils.isEmpty(it.getName())) {
			throw new BhException("Name cannot be null");
		}
		if (null != inspectTypeRepository.findBySnAndIdNot(it.getSn(), it.getId())) {
			throw new BhException("Number already existed");
		}
		if (null != inspectTypeRepository.findByNameAndIdNot(it.getName(), it.getId())) {
			throw new BhException("Name already existed");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(it.getCreateTime())) {
			it.setCreateTime(now);
		}
		it.setUpdateTime(now);
		it = inspectTypeRepository.save(it);
		return it;
	}

	@Override
	public InspectType delete(Integer id) {
		InspectType it = null;
		if (null == it && !CheckUtils.isEmpty(id)) {
			try {
				it = inspectTypeRepository.findById(id).get();
			} catch (Exception e) {
				it = null;
			}
		}
		if (null != it) {
			inspectTypeRepository.delete(it);
		}
		return it;
	}

	@Override
	public InspectType load(Integer id) {
		InspectType it = null;
		if (null == it && !CheckUtils.isEmpty(id)) {
			try {
				it = inspectTypeRepository.findById(id).get();
			} catch (Exception e) {
				it = null;
			}
		}
		return it;
	}

	@Override
	public List<InspectType> list() {
		List<InspectType> its = inspectTypeRepository.findAll();
		return its;
	}

}
