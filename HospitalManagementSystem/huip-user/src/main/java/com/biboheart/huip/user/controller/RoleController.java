package com.biboheart.huip.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.user.domain.Role;
import com.biboheart.huip.user.service.RoleService;

@RestController
public class RoleController {
	@Autowired
	public RoleService roleService;
	
	@RequestMapping(value = "/userapi/user/role/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Role role) throws BhException {
		role = roleService.save(role);
		return new BhResponseResult<>(0, "success", role);
	}
	
	@RequestMapping(value = "/userapi/user/role/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Role role = roleService.delete(id);
		return new BhResponseResult<>(0, "success", role);
	}
	
	@RequestMapping(value = "/userapi/user/role/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn) {
		Role role = roleService.load(id, sn);
		return new BhResponseResult<>(0, "success", role);
	}
	
	@RequestMapping(value = "/userapi/user/role/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String ids, String sns) {
		List<Integer> inIdList = PrimaryTransverter.idsStr2List(ids);
		List<String> inSnList = new ArrayList<>();
		if (!CheckUtils.isEmpty(sns)) {
			String[] snArr = sns.split(",");
			if (snArr.length > 0) {
				for (String sn : snArr) {
					if (CheckUtils.isEmpty(sn) || inSnList.contains(sn)) {
						continue;
					}
					inSnList.add(sn);
				}
			}
		}
		List<Role> roles = roleService.list(inIdList, inSnList);
		return new BhResponseResult<>(0, "success", roles);
	}
	
	@RequestMapping(value = "/userapi/user/role/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find(String ids, String sns, String match) {
		List<Integer> inIdList = PrimaryTransverter.idsStr2List(ids);
		List<String> inSnList = new ArrayList<>();
		if (!CheckUtils.isEmpty(sns)) {
			String[] snArr = sns.split(",");
			if (snArr.length > 0) {
				for (String sn : snArr) {
					if (CheckUtils.isEmpty(sn) || inSnList.contains(sn)) {
						continue;
					}
					inSnList.add(sn);
				}
			}
		}
		Page<Role> roles = roleService.find(inIdList, inSnList, match);
		return new BhResponseResult<>(0, "success", roles);
	}
}
