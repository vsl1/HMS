package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.Authority;

public interface AuthorityRepository extends CustomRepository<Authority, Integer> {
	Authority findBySnAndIdNot(String sn, Integer id);
	
	Authority findByNameAndPidAndIdNot(String name, Integer pid, Integer id);
}
