package com.biboheart.huip.user.security.provider;

import java.util.HashSet;
import java.util.Set;

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
import com.biboheart.huip.user.security.tokens.MobileCodeAuthenticationToken;
import com.biboheart.huip.user.service.AccountService;

public class MobileCodeAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private AccountService accountService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println(this.getClass().getName() + " authenticate");
		String mobile = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		String code = (String) authentication.getCredentials();
		if (CheckUtils.isEmpty(code)) {
			throw new BadCredentialsException("Cannot be null");
		}
		Account account = accountService.load(null, null, mobile);
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
		if (!code.equals("0000")) {
			throw new BadCredentialsException("Wrong");
		}
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(account.getSn(), code, listUserGrantedAuthorities(account.getSn()));
		result.setDetails(authentication.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (MobileCodeAuthenticationToken.class.isAssignableFrom(authentication));
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
