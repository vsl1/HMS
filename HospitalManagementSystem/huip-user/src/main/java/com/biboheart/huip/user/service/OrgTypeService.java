package com.biboheart.huip.user.service;

import java.util.List;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.OrgType;
import com.biboheart.huip.user.domain.OrgTypeRule;

public interface OrgTypeService {
	/**
	 * @param orgType 
	 * @return
	 */
	public OrgType save(OrgType orgType) throws BhException;
	
	/**
	 * @param id 
	 * @return
	 * @throws BhException 
	 */
	public OrgType delete(Integer id, String sn) throws BhException;
	
	/**
	 * @param id
	 * @param sn
	 * @return
	 */
	public OrgType load(Integer id, String sn);
	
	/**
	 * @return
	 */
	public List<OrgType> list(List<Integer> ids, List<String> sns, List<Integer> pidList, Integer descendant, Integer self);
	public List<Integer> listId(List<String> sns, List<Integer> pidList, Integer descendant, Integer self);
	
	/**
	 * @param pid 
	 * @param cid 
	 * @throws EberException 
	 */
	public OrgTypeRule addOrgTypeRule(Integer pid, Integer cid) throws BhException;
	
	public OrgTypeRule deleteOrgTypeRule(Integer pid, Integer cid) throws BhException;
}
