package com.biboheart.huip.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.reservation.domain.Device;
import com.biboheart.huip.reservation.service.DeviceService;

@RestController
public class DeviceController {
	@Autowired
	private DeviceService deviceService;
	
	@RequestMapping(value = "/maaapi/maa/device/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Device device) throws BhException {
		device = deviceService.save(device);
		return new BhResponseResult<>(0, "success", device);
	}
	
	@RequestMapping(value = "/maaapi/maa/device/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Device device = deviceService.delete(id);
		return new BhResponseResult<>(0, "success", device);
	}
	
	@RequestMapping(value = "/maaapi/maa/device/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn) {
		Device device = deviceService.load(id, sn);
		return new BhResponseResult<>(0, "success", device);
	}
	
	@RequestMapping(value = "/maaapi/maa/device/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String match) {
		List<Device> devices = deviceService.list(null, match);
		return new BhResponseResult<>(0, "success", devices);
	}
	
	@RequestMapping(value = "/maaapi/maa/device/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find(String match) {
		Page<Device> devices = deviceService.find(null, match);
		return new BhResponseResult<>(0, "success", devices);
	}
}
