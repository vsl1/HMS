package com.biboheart.huip.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.user.domain.Account;
import com.biboheart.huip.user.service.AccountService;

@RestController
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/userapi/account/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Account account) throws BhException {
		account = accountService.save(account);
		return new BhResponseResult<>(0, "success", account);
	}

}
