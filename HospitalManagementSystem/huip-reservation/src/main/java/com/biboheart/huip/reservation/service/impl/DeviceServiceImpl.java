package com.biboheart.huip.reservation.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.reservation.domain.Device;
import com.biboheart.huip.reservation.repository.DeviceRepository;
import com.biboheart.huip.reservation.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
	private DeviceRepository deviceRepository;

	@Override
	public Device save(Device device) throws BhException {
		if (null == device.getId()) {
			device.setId(0);
		}	
		if (CheckUtils.isEmpty(device.getName())) {
			throw new BhException("Name cannot be null");
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(device.getSn())) {
			device.setSn("device_" + now);
		}
		if (null != deviceRepository.findBySnAndIdNot(device.getSn(), device.getId())) {
			throw new BhException("Already existed");
		}
		if (CheckUtils.isEmpty(device.getCreateTime())) {
			device.setCreateTime(now);
		}
		if (CheckUtils.isEmpty(device.getState())) {
			device.setState(1);
		}
		device.setUpdateTime(now);
		device = deviceRepository.save(device);
		return device;
	}

	@Override
	public Device delete(Integer id) {
		Device device = null;
		if (null == device && !CheckUtils.isEmpty(id)) {
			try {
				device = deviceRepository.findById(id).get();
			} catch (Exception e) {
				device = null;
			}
		}
		if (null != device) {
			deviceRepository.delete(device);
		}
		return device;
	}

	@Override
	public Device load(Integer id, String sn) {
		Device device = null;
		if (null == device && !CheckUtils.isEmpty(sn)) {
			device = deviceRepository.findBySnAndIdNot(sn, 0);
		}
		if (null == device && !CheckUtils.isEmpty(id)) {
			try {
				device = deviceRepository.findById(id).get();
			} catch (Exception e) {
				device = null;
			}
		}
		return device;
	}

	@Override
	public List<Device> list(List<Integer> inIdList, String match) {
		Specification<Device> spec = processSpec(inIdList, match);
		List<Device> devices = deviceRepository.findAll(spec);
		return devices;
	}

	@Override
	public Page<Device> find(List<Integer> inIdList, String match) {
		Specification<Device> spec = processSpec(inIdList, match);
		Page<Device> devices = deviceRepository.findAll(spec, (Pageable) null);
		return devices;
	}
	
	private Specification<Device> processSpec(List<Integer> inIdList, String match) {
		return new Specification<Device>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> pres = new ArrayList<>();
				if(!CheckUtils.isEmpty(inIdList)) {
					pres.add(root.get("id").in(inIdList));
				}
				if(!CheckUtils.isEmpty(match)) {
					pres.add(cb.or(
							cb.like(root.get("sn"), "%" + match + "%"),
							cb.like(root.get("name"), "%" + match + "%")
							));
				}
				if(CheckUtils.isEmpty(pres)) {
					return null;
				}
				return cb.and(pres.toArray(new Predicate[pres.size()]));
			}
		};
	}

}
