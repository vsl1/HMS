package com.biboheart.huip.user.security.provider;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.huip.user.domain.Account;
import com.biboheart.huip.user.service.AccountService;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountService accountService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println(this.getClass().getName() + " authenticate");
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		String password = (String) authentication.getCredentials();
		if (CheckUtils.isEmpty(password)) {
			throw new BadCredentialsException("Password cannot be null");
		}
		Account account = accountService.load(null, username, username);
		if (null == account) {
			throw new BadCredentialsException("User don't exist");
		}
		if (StringUtils.isEmpty(account.getSn())) {
			try {
				account = accountService.save(account);
			} catch (BhException e) {
				e.printStackTrace();
			}
		}
		if (password.length() != 32) {
			password = DigestUtils.md5Hex(password);
		}
		if (!password.equals(account.getPassword())) {
			throw new BadCredentialsException("Username or password wrong");
		}
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(account.getSn(), password, listUserGrantedAuthorities(account.getSn()));
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	private Set<GrantedAuthority> listUserGrantedAuthorities(String account) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if (CheckUtils.isEmpty(account)) {
			return authorities;
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}
