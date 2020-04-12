package com.biboheart.huip.user.service;

import java.util.List;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.UserRole;

public interface UserRoleService {
	public UserRole save(UserRole ur) throws BhException;
	
	public UserRole delete(Long uid, Integer rid);
	
	public List<UserRole> list(List<Long> inUidList, List<Integer> inRidList);
}
