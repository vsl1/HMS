package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.User;

public interface UserRepository extends CustomRepository<User, Long> {
	User findBySnAndIdNot(String sn, Long id);
	User findByAccount(String account);
}
