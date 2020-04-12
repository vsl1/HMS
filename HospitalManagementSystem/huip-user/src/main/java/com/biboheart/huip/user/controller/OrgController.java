package com.biboheart.huip.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.ListUtils;
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.user.domain.Org;
import com.biboheart.huip.user.domain.OrgType;
import com.biboheart.huip.user.domain.User;
import com.biboheart.huip.user.domain.UserOrg;
import com.biboheart.huip.user.service.OrgService;
import com.biboheart.huip.user.service.OrgTypeService;
import com.biboheart.huip.user.service.UserOrgService;
import com.biboheart.huip.user.service.UserService;

@RestController
public class OrgController {
	@Autowired
	private OrgService orgService;
	@Autowired
	private OrgTypeService orgTypeService;
	@Autowired
	private UserOrgService userOrgService;
	@Autowired
	private UserService userService;
	
	/**
	 * @param org
	 * @return
	 * @throws BhException
	 */
	@RequestMapping(value = "/userapi/user/org/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Org org) throws BhException {
		org = orgService.save(org);
		return new BhResponseResult<>(0, "success", org);
	}
	
	/**
	 * @param id 
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/org/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Org org = orgService.delete(id);
		return new BhResponseResult<>(0, "success", org);
	}
	
	/**
	 * @param id 
	 * @param sn 
	 * @param pid 
	 * @param name 
	 * @param parentTypeId 
	 * @param parentTypeSn 
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/org/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn, Integer pid, String name, Integer parentTypeId, String parentTypeSn) {
		if (CheckUtils.isEmpty(parentTypeId) && !CheckUtils.isEmpty(parentTypeSn)) {
			OrgType ot = orgTypeService.load(null, parentTypeSn);
			parentTypeId = ot.getId();
		}
		Org org = orgService.load(id, sn, pid, name, parentTypeId);
		return new BhResponseResult<>(0, "success", org);
	}
	
	
	@RequestMapping(value = "/userapi/user/org/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String ids, String pids, String otids, String otsns, Integer descendant, Integer parents, String match) {
		List<Integer> inIdList = PrimaryTransverter.idsStr2List(ids);
		List<Integer> inOtidList = PrimaryTransverter.idsStr2List(otids);
		if(!CheckUtils.isEmpty(otsns)) {
			String[] otSnArr = otsns.split(",");
			for(String otSn : otSnArr) {
				OrgType ot = orgTypeService.load(null, otSn);
				if(null == ot) {
					continue;
				}
				if(null == inOtidList) {
					inOtidList = new ArrayList<>();
				}
				if(!inOtidList.contains(ot.getId())) {
					inOtidList.add(ot.getId());
				}
			}
		}
		User user = userService.current();
		if (null != user) {
			List<UserOrg> uos = userOrgService.list(user.getId());
			List<Integer> userAllowOidList = null;
			if (!CheckUtils.isEmpty(uos)) {
				List<Integer> userOidList = new ArrayList<>();
				for (UserOrg uo : uos) {
					userOidList.add(uo.getOid());
				}
				userAllowOidList = orgService.listId(null, userOidList, null, 1, null);
			}
			if (CheckUtils.isEmpty(userAllowOidList)) {
				inIdList = new ArrayList<>();
				inIdList.add(0);
			} else {
				if (CheckUtils.isEmpty(inIdList)) {
					inIdList = userAllowOidList;
				} else {
					
					inIdList = ListUtils.intersectionList(inIdList, userAllowOidList);
				}
			}
		}
		List<Integer> inPidList = PrimaryTransverter.idsStr2List(pids);
		List<Org> orgs = orgService.list(inIdList, inPidList, inOtidList, descendant, parents, match);
		return new BhResponseResult<>(0, "success", orgs);
	}
	
	
	@RequestMapping(value = "/userapi/user/org/listId", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> listId(String ids, String pids, String otids, String otsns, Integer descendant, Integer parents) {
		List<Integer> inIdList = PrimaryTransverter.idsStr2List(ids);
		List<Integer> inOtidList = PrimaryTransverter.idsStr2List(otids);
		if(!CheckUtils.isEmpty(otsns)) {
			String[] otSnArr = otsns.split(",");
			for(String otSn : otSnArr) {
				OrgType ot = orgTypeService.load(null, otSn);
				if(null == ot) {
					continue;
				}
				if(null == inOtidList) {
					inOtidList = new ArrayList<>();
				}
				if(!inOtidList.contains(ot.getId())) {
					inOtidList.add(ot.getId());
				}
			}
		}
		User user = userService.current();
		if (null != user) {
			List<UserOrg> uos = userOrgService.list(user.getId());
			List<Integer> userAllowOidList = null;
			if (!CheckUtils.isEmpty(uos)) {
				List<Integer> userOidList = new ArrayList<>();
				for (UserOrg uo : uos) {
					userOidList.add(uo.getOid());
				}
				userAllowOidList = orgService.listId(null, userOidList, null, 1, null);
			}
			if (CheckUtils.isEmpty(userAllowOidList)) {
				inIdList = new ArrayList<>();
				inIdList.add(0);
			} else {
				if (CheckUtils.isEmpty(inIdList)) {
					inIdList = userAllowOidList;
				} else {
					
					inIdList = ListUtils.intersectionList(inIdList, userAllowOidList);
				}
			}
		}
		List<Integer> inPidList = PrimaryTransverter.idsStr2List(pids);
		List<Integer> idList = orgService.listId(inIdList, inPidList, inOtidList, descendant, parents);
		return new BhResponseResult<>(0, "success", idList);
	}
}
