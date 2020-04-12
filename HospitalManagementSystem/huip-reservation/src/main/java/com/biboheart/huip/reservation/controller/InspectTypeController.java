package com.biboheart.huip.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.reservation.domain.InspectType;
import com.biboheart.huip.reservation.service.InspectTypeService;

@RestController
public class InspectTypeController {
	@Autowired
	private InspectTypeService inspectTypeService;
	
	@RequestMapping(value = "/maaapi/maa/inspectType/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(InspectType it) throws BhException {
		it = inspectTypeService.save(it);
		return new BhResponseResult<>(0, "success", it);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspectType/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		InspectType it = inspectTypeService.delete(id);
		return new BhResponseResult<>(0, "success", it);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspectType/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id) {
		InspectType it = inspectTypeService.load(id);
		return new BhResponseResult<>(0, "success", it);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspectType/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(Long start, Long end, String pats, String devices, String projects, String match) {
		List<InspectType> its = inspectTypeService.list();
		return new BhResponseResult<>(0, "success", its);
	}
}
