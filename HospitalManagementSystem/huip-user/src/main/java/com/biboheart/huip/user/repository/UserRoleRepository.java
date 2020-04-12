package com.biboheart.huip.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.UserRole;

public interface UserRoleRepository extends CustomRepository<UserRole, Long> {
	UserRole findByUidAndRid(Long uid, Integer rid);
	
	@Query("select rid from UserRole where uid = ?1")
	List<Integer> findRidByUid(Long uid);
}
