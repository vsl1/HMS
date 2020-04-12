package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.Role;

public interface RoleRepository extends CustomRepository<Role, Integer> {
	Role findBySnAndIdNot(String sn, Integer id);
	
	Role findByNameAndIdNot(String name, Integer id);
}
