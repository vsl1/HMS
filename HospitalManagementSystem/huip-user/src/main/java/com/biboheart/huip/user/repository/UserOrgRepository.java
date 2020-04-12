package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.UserOrg;

public interface UserOrgRepository extends CustomRepository<UserOrg, Long> {
	UserOrg findByUidAndOid(Long uid, Integer oid);
}
