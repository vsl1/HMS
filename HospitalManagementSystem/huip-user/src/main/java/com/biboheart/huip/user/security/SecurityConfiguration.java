package com.biboheart.huip.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.biboheart.huip.user.security.filter.MobileCodeAuthenticationProcessingFilter;
import com.biboheart.huip.user.security.provider.MobileCodeAuthenticationProvider;
import com.biboheart.huip.user.security.provider.UsernamePasswordAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
	/*@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
		auth.authenticationProvider(mobileCodeAuthenticationProvider());
		auth.authenticationProvider(usernamePasswordAuthenticationProvider());
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
        http
        	.csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/home", "/oauth/token", "/oauth/authorize").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        http.addFilterBefore(mobileCodeAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }
	
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public MobileCodeAuthenticationProcessingFilter mobileCodeAuthenticationProcessingFilter() {
    	MobileCodeAuthenticationProcessingFilter filter = new MobileCodeAuthenticationProcessingFilter();
    	filter.setAuthenticationManager(authenticationManager);
    	return filter;
    }
    
    @Bean
    public UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider() {
    	return new UsernamePasswordAuthenticationProvider();
    }
    
    @Bean
    public MobileCodeAuthenticationProvider mobileCodeAuthenticationProvider() {
    	return new MobileCodeAuthenticationProvider();
    }
}
