package com.biboheart.huip.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.user.domain.Authority;
import com.biboheart.huip.user.service.AuthorityService;

@RestController
public class AuthorityController {
	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value = "/userapi/user/authority/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Authority authority) throws BhException {
		authority = authorityService.save(authority);
		return new BhResponseResult<>(0, "success", authority);
	}
	
	@RequestMapping(value = "/userapi/user/authority/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Authority authority = authorityService.delete(id);
		return new BhResponseResult<>(0, "success", authority);
	}
	
	@RequestMapping(value = "/userapi/user/authority/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn) {
		Authority authority = authorityService.load(id, sn);
		return new BhResponseResult<>(0, "success", authority);
	}
	
	@RequestMapping(value = "/userapi/user/authority/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String ids, String pids, Long uid) {
		List<Integer> inIdList = PrimaryTransverter.idsStr2List(ids);
		List<Integer> inPidList = PrimaryTransverter.idsStr2List(pids);
		List<Authority> authoritys = authorityService.list(inIdList, inPidList, uid);
		return new BhResponseResult<>(0, "success", authoritys);
	}
}
