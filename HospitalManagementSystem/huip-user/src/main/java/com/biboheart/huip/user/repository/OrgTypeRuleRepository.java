package com.biboheart.huip.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.OrgTypeRule;

public interface OrgTypeRuleRepository extends CustomRepository<OrgTypeRule, Integer> {
	OrgTypeRule findByPidAndCid(Integer pid, Integer cid);

	@Query("select cid from OrgTypeRule where pid in ?1")
	public List<Integer> findCidsByPidIn(List<Integer> pids);
	
	@Query("select pid from OrgTypeRule where cid in ?1")
	public List<Integer> findPidsByCidIn(List<Integer> cids);
}
