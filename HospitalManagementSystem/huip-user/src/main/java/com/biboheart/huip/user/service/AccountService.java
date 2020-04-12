package com.biboheart.huip.user.service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.Account;

public interface AccountService {
	public Account save(Account account) throws BhException;
	
	public Account delete(Long id, String sn);
	
	public Account load(String sn, String username, String mobile);
}
