package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.Org;

public interface OrgRepository extends CustomRepository<Org, Integer> {
	Long countByOtid(Integer otid);
	
	Org findBySnAndIdNot(String sn, Integer id);
	
	Org findByNameAndPidAndIdNot(String name, Integer pid, Integer id);
}
