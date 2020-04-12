package com.biboheart.huip.user.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.User;
import com.biboheart.huip.user.repository.UserRepository;
import com.biboheart.huip.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User current() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null == authentication) {
			return null;
		}
		String sn = null;
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			sn = authentication.getName();
		}
		if(CheckUtils.isEmpty(sn)) {
			return null;
		}
		User user = userRepository.findByAccount(sn);
		return user;
	}

	@Override
	public User save(User user) throws BhException {
		if (null == user.getId()) {
			user.setId(0L);
		}
		// 以下用到了 com.biboheart.brick 中的BhException,CheckUtils.isEmpty,TimeUtils.getCurrentTimeInMillis
		if (CheckUtils.isEmpty(user.getName())) {
			throw new BhException("Name cannot be null");
		}
		if (CheckUtils.isEmpty(user.getSn())) {
			String sn = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			while (null != userRepository.findBySnAndIdNot(sn, user.getId())) {
				sn = UUID.randomUUID().toString().replace("-", "").toUpperCase();
			}
			user.setSn(sn);
		}
		Long now = TimeUtils.getCurrentTimeInMillis(); 
		if (CheckUtils.isEmpty(user.getCreateTime())) {
			user.setCreateTime(now);
		}
		user.setUpdateTime(now);
		user = userRepository.save(user);
		return user;
	}

	@Override
	public User delete(Long id) {
		if (CheckUtils.isEmpty(id)) {
			return null;
		}
		User user = userRepository.findById(id).get();
		if (null != user) {
			userRepository.delete(user);
		}
		return user;
	}

	@Override
	public User load(Long id, String account) {
		User user = null;
		if (null == user && !CheckUtils.isEmpty(account)) {
			user = userRepository.findByAccount(account);
		}
		if (null == user && !CheckUtils.isEmpty(id)) {
			user = userRepository.findById(id).get();
		}
		return user;
	}

	@Override
	public List<User> list() {
		List<User> users = userRepository.findAll();
		return users;
	}

	@Override
	public Page<User> find() {
		Page<User> users = userRepository.findAll((Pageable)null);
		return users;
	}

}
