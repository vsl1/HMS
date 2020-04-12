package com.biboheart.huip.user.service;

import java.util.List;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.UserOrg;

public interface UserOrgService {
	public UserOrg save(UserOrg uo) throws BhException;
	
	public UserOrg delete(Long id, Long uid, Integer oid);
	
	public List<UserOrg> list(Long uid);
}
