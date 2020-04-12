package com.biboheart.huip.user.service;

import java.util.List;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.Authority;

public interface AuthorityService {
	public Authority save(Authority authority) throws BhException;
	
	public Authority delete(Integer id);
	
	public Authority load(Integer id, String sn);
	
	public List<Authority> list(List<Integer> inIdList, List<Integer> inPidList, Long uid);
}
