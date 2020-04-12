package com.biboheart.huip.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.OrgType;

public interface OrgTypeRepository extends CustomRepository<OrgType, Integer> {
	OrgType findBySnAndIdNot(String sn, Integer id);
	
	OrgType findByNameAndIdNot(String name, Integer id);

	@Query("select id from OrgType where sn in ?1")
	List<Integer> findIdsBySnIn(List<String> inSnList);
	
	@Query("select id from OrgType")
	public List<Integer> findAllIds();
}
