package com.biboheart.huip.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.user.domain.User;
import com.biboheart.huip.user.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	/**
	 * @param 
	 * @return
	 * @throws BhException
	 */
	@RequestMapping(value = "/userapi/user/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(User user) throws BhException {
		user = userService.save(user);
		return new BhResponseResult<>(0, "success", user);
	}
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Long id) {
		User user = userService.delete(id);
		return new BhResponseResult<>(0, "success", user);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Long id) {
		User user = userService.load(id, null);
		return new BhResponseResult<>(0, "success", user);
	}
	
	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list() {
		List<User> users = userService.list();
		return new BhResponseResult<>(0, "success", users);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/userapi/user/find", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> find() {
		Page<User> users = userService.find();
		return new BhResponseResult<>(0, "success", users);
	}
}
