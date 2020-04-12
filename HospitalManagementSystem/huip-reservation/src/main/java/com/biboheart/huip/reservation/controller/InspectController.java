package com.biboheart.huip.reservation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.reservation.domain.Inspect;
import com.biboheart.huip.reservation.service.InspectService;

@RestController
public class InspectController {
	@Autowired
	private InspectService inspectService;
	
	@RequestMapping(value = "/maaapi/maa/inspect/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Inspect inspect) throws BhException {
		inspect = inspectService.save(inspect);
		return new BhResponseResult<>(0, "success", inspect);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspect/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id, Long pat, Integer device, Integer project) {
		Inspect inspect = inspectService.delete(id, null, pat, device, project);
		return new BhResponseResult<>(0, "success", inspect);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspect/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, Long pat, Integer device, Integer project) {
		Inspect inspect = inspectService.load(id, null, pat, device, project);
		return new BhResponseResult<>(0, "success", inspect);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspect/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(Long start, Long end, String pats, String devices, String projects, String match) {
		List<Long> inPatList = PrimaryTransverter.idsStr2LongList(pats);
		List<Integer> inDeviceList = PrimaryTransverter.idsStr2List(devices);
		List<Integer> inProjectList = PrimaryTransverter.idsStr2List(projects);
		List<Inspect> inspects = inspectService.list(start, end, inPatList, inDeviceList, inProjectList, match);
		return new BhResponseResult<>(0, "success", inspects);
	}
	
	@RequestMapping(value = "/maaapi/maa/inspect/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find(Long start, Long end, String pats, String devices, String projects, String match) {
		List<Long> inPatList = PrimaryTransverter.idsStr2LongList(pats);
		List<Integer> inDeviceList = PrimaryTransverter.idsStr2List(devices);
		List<Integer> inProjectList = PrimaryTransverter.idsStr2List(projects);
		Page<Inspect> inspects = inspectService.find(start, end, inPatList, inDeviceList, inProjectList, match);
		return new BhResponseResult<>(0, "success", inspects);
	}
}
