package com.biboheart.huip.user.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.biboheart.brick.exception.BhException;
import com.biboheart.huip.user.domain.Client;

public interface ClientService {
	/**
	 * 
	 * @param client
	 * @return
	 * @throws EberException 
	 */
	public Client save(Client client) throws BhException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws EberException 
	 */
	public Client delete(Integer id);

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Client load(Integer id, String name, String clientId);

	/**
	 * 
	 * @return
	 */
	public List<Client> list();
	
	/**
	 * @return
	 * @throws EberException 
	 */
	public Client current();
	
	public Set<GrantedAuthority> listClientGrantedAuthorities(String clientId);
}
