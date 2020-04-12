package com.biboheart.huip.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.Role;

public interface RoleService {
	public Role save(Role role) throws BhException;
	
	public Role delete(Integer id);
	
	public Role load(Integer id, String sn);
	
	public List<Role> list(List<Integer> inIdList, List<String> inSnList);
	
	public Page<Role> find(List<Integer> inIdList, List<String> inSnList, String match);
}
