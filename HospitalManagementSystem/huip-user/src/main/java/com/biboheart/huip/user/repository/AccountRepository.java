package com.biboheart.huip.user.repository;

import com.biboheart.huip.user.basejpa.CustomRepository;
import com.biboheart.huip.user.domain.Account;

public interface AccountRepository extends CustomRepository<Account, Long> {
	Account findBySnAndIdNot(String sn, Long id);
	
	Account findByUsername(String username);
	
	Account findByMobile(String mobile);
	
	Account findByUsernameOrMobile(String username, String mobile);
}
