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
import com.biboheart.brick.utils.PrimaryTransverter;
import com.biboheart.huip.user.domain.OrgType;
import com.biboheart.huip.user.domain.OrgTypeRule;
import com.biboheart.huip.user.service.OrgTypeService;

@RestController
public class OrgTypeController {
	@Autowired
	private OrgTypeService orgTypeService;
	
	/**
	 * @param ot
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgType/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(OrgType ot) throws BhException {
		ot = orgTypeService.save(ot);
		return new BhResponseResult<>(0, "success", ot);
	}
	
	/**
	 * @param id
	 * @return
	 * @throws BhException 
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgType/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id, String sn) throws BhException {
		OrgType ot = orgTypeService.delete(id, sn);
		return new BhResponseResult<>(0, "success", ot);
	}
	
	/**
	 * @param id
	 * @param sn
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgType/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String sn) {
		OrgType ot = orgTypeService.load(id, sn);
		return new BhResponseResult<>(0, "success", ot);
	}
	
	/**
	 * @param ids
	 * @param pid
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgType/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list(String ids, Integer pid, String sns, Integer descendant, Integer self) {
		List<OrgType> ots = null;
		List<Integer> idList = PrimaryTransverter.idsStr2List(ids);
		List<Integer> pidList = new ArrayList<>();
		if (!CheckUtils.isEmpty(pid)) {
			pidList.add(pid);
		}
		List<String> snList = new ArrayList<>();
		if(!CheckUtils.isEmpty(sns)) {
			String[] snArr = sns.split(",");
			for(String sn : snArr) {
				snList.add(sn);
			}
		}
		ots = orgTypeService.list(idList, snList, pidList, descendant, self);
		return new BhResponseResult<>(0, "success", ots);
	}
	
	/**
	 * @param id
	 * @param cid
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgTypeRule/save", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> saveRule(Integer id, Integer cid) throws BhException {
		OrgTypeRule otr = orgTypeService.addOrgTypeRule(id, cid);
		return new BhResponseResult<>(0, "success", otr);
	}
	
	/**
	 * @param id
	 * @param cid
	 * @return
	 * @throws BhException 
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/user/orgTypeRule/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> deleteRule(Integer id, Integer cid) throws BhException {
		OrgTypeRule otr = orgTypeService.deleteOrgTypeRule(id, cid);
		return new BhResponseResult<>(0, "success", otr);
	}
}
