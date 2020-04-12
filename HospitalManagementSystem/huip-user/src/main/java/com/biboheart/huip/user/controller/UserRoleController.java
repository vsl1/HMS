package com.biboheart.huip.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.user.domain.UserRole;
import com.biboheart.huip.user.service.UserRoleService;

@RestController
public class UserRoleController {
	@Autowired
	private UserRoleService userRoleService;

	@RequestMapping(value = "/userapi/user/userRole/save", method = { RequestMethod.POST })
	public BhResponseResult<?> save(UserRole ur) throws BhException {
		ur = userRoleService.save(ur);
		return new BhResponseResult<>(0, "success", ur);
	}
	
	@RequestMapping(value = "/userapi/user/userRole/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Long uid, Integer rid) {
		UserRole ur = userRoleService.delete(uid, rid);
		return new BhResponseResult<>(0, "success", ur);
	}
	
	@RequestMapping(value = "/userapi/user/userRole/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String uids, String rids) {
		List<Long> inUidList = PrimaryTransverter.idsStr2LongList(uids);
		List<Integer> inRidList = PrimaryTransverter.idsStr2List(rids);
		List<UserRole> urs = userRoleService.list(inUidList, inRidList);
		return new BhResponseResult<>(0, "success", urs);
	}
}
