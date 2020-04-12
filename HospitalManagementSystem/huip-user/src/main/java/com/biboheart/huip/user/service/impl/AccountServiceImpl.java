package com.biboheart.huip.user.service.impl;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.Account;
import com.biboheart.huip.user.repository.AccountRepository;
import com.biboheart.huip.user.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account save(Account account) throws BhException {
		if (null == account.getId()) {
			account.setId(0L);
		}
		if (CheckUtils.isEmpty(account.getUsername())) {
			if (!CheckUtils.isEmpty(account.getMobile())) {
				account.setUsername(account.getMobile());
			}
		}
		if (CheckUtils.isEmpty(account.getUsername())) {
			throw new BhException("Name cannot be null");
		}
		Account source = null;
		if (!CheckUtils.isEmpty(account.getSn())) {
			source = accountRepository.findBySnAndIdNot(account.getSn(), 0L);
		}
		if (null != source && account.getId().equals(0L)) {
			account.setId(source.getId());
		}
		if (null != source && !account.getId().equals(source.getId())) {
			account.setSn(null);
		}
		if (null != source && CheckUtils.isEmpty(account.getPassword())) {
			account.setPassword(source.getPassword());
		}
		if (!CheckUtils.isEmpty(account.getMobile())) {
			source = accountRepository.findByUsernameOrMobile(account.getMobile(), account.getMobile());
			if (null != source && !source.getId().equals(account.getId())) {
				throw new BhException("Phohe number already existed");
			}
		}
		if (!CheckUtils.isEmpty(account.getUsername())) {
			source = accountRepository.findByUsernameOrMobile(account.getUsername(), account.getUsername());
			if (null != source && !source.getId().equals(account.getId())) {
				throw new BhException("User already existed");
			}
		}
		if (!CheckUtils.isEmpty(account.getPassword()) && account.getPassword().length() != 32) {
			account.setPassword(DigestUtils.md5Hex(account.getPassword()));
		}
		if (CheckUtils.isEmpty(account.getSn())) {
			String sn = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			while (null != accountRepository.findBySnAndIdNot(sn, account.getId())) {
				sn = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			}
			account.setSn(sn);
		}
		Long now = TimeUtils.getCurrentTimeInMillis();
		if (CheckUtils.isEmpty(account.getCreateTime())) {
			account.setCreateTime(now);
		}
		account.setUpdateTime(now);
		account = accountRepository.save(account);
		return account;
	}

	@Override
	public Account delete(Long id, String sn) {
		Account account = null;
		if (null == account && !CheckUtils.isEmpty(sn)) {
			account = accountRepository.findBySnAndIdNot(sn, 0L);
		}
		if (null == account && !CheckUtils.isEmpty(id)) {
			account = accountRepository.findById(id).get();
		}
		if (null != account) {
			accountRepository.delete(account);
		}
		return account;
	}
	
	@Override
	public Account load(String sn, String username, String mobile) {
		Account account = null;
		if (null == account && !CheckUtils.isEmpty(sn)) {
			account = accountRepository.findBySnAndIdNot(sn, 0L);
		}
		if (null == account && !CheckUtils.isEmpty(username)) {
			account = accountRepository.findByUsername(username);
		}
		if (null == account && !CheckUtils.isEmpty(mobile)) {
			account = accountRepository.findByMobile(mobile);
		}
		return account;
	}

}
