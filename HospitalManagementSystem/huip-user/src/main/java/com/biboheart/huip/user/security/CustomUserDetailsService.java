package com.biboheart.huip.user.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.huip.user.domain.Account;
import com.biboheart.huip.user.service.AccountService;

@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private AccountService accountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(CheckUtils.isEmpty(username)) {
			throw new BadCredentialsException("Name cannot be null");
		}
		Account account = accountService.load(null, username, username);
		if (null == account) {
			throw new BadCredentialsException("User don't exist");
		}
		return new org.springframework.security.core.userdetails.User(account.getSn(), account.getPassword(), true, true, true, true, listUserGrantedAuthorities(account.getSn()));
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
