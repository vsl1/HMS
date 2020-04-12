package com.biboheart.huip.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.Org;

public interface OrgService {
	public Org save(Org org) throws BhException;
	
	public Org delete(Integer id);
	
	public Org load(Integer id, String sn, Integer pid, String name, Integer parentTypeId);
	
	public List<Org> list(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, Integer descendant, Integer parents, String match);
	public Page<Org> find(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, Integer descendant, Integer parents, String match);
	public List<Integer> listId(List<Integer> inIdList, List<Integer> inPidList, List<Integer> inOtidList, Integer descendant, Integer parents);
}
