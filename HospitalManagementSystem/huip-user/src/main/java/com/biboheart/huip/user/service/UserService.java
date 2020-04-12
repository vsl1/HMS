package com.biboheart.huip.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.User;

public interface UserService {
	public User current();
	
	/**
	 * 
	 * @param user
	 *           
	 * @return 
	 * @throws BhException 
	 */
	public User save(User user) throws BhException;

	/**

	 * @param id
	 * @return 
	 */
	public User delete(Long id);

	/**
	 * @param id
	 * @return 
	 */
	public User load(Long id, String account);

	/**
	 * @return 
	 */
	public List<User> list();
	public Page<User> find();
}
