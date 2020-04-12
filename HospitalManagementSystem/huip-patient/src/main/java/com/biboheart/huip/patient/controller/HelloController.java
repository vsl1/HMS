package com.biboheart.huip.patient.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biboheart.huip.patient.amqp.RpcPatientUserClient;

@Controller
public class HelloController {
	@Autowired
	private RpcPatientUserClient rpcPatientUserClient;
	
	@RequestMapping(value = "/loadUser", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> loadUser() {
		String account = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				account = authentication.getName();
			}
		}
		if (null == account) {
			return null;
		}
		Map<String, Object> user = rpcPatientUserClient.load(null, account);
		System.out.println(user);
		return user;
	}
	
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String homePage() {
		return "index";
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return "hello";
	}
	
	@RequestMapping(value = "/demo")
	@ResponseBody
	public String username(Principal principal, HttpSession session) {
		System.out.println("1:" + principal.getName());
		Object springSecurityContext = session.getAttribute("SPRING_SECURITY_CONTEXT");
		if (springSecurityContext instanceof SecurityContext) {
			SecurityContext sc = (SecurityContext) springSecurityContext;
			Authentication authentication = sc.getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				System.out.println("2:" + authentication.getName());
			}
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				System.out.println("3:" + authentication.getName());
			}
		}
		return principal.getName();
	}

}
