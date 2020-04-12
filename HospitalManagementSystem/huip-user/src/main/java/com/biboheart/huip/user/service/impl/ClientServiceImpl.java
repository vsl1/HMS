package com.biboheart.huip.user.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.TimeUtils;
import com.biboheart.huip.user.domain.Client;
import com.biboheart.huip.user.repository.ClientRepository;
import com.biboheart.huip.user.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public Client save(Client client) throws BhException {
		if(null == client.getId()) {
			client.setId(0);
		}
		if(CheckUtils.isEmpty(client.getClientName())) {
			throw new BhException("Client name cannot be null");
		}
		Client source = clientRepository.findByClientNameAndIdNot(client.getClientName(), client.getId());
		if (null != source && source.getId() != client.getId()) {
			throw new BhException("Client already existed");
		}
		if(CheckUtils.isEmpty(client.getCreateTime())) {
			client.setCreateTime(TimeUtils.getCurrentTimeInMillis());
		}
		if(null != source) {
			client.setClientId(source.getClientId());
			client.setClientSecret(source.getClientSecret());
		}
		if(CheckUtils.isEmpty(client.getClientId()) || CheckUtils.isEmpty(client.getClientSecret())) {
			client.setClientId(DigestUtils.md5Hex(client.getClientName() + "_client_" + UUID.randomUUID().toString()));
			client.setClientSecret(DigestUtils.md5Hex(client.getClientName() + "_secret_" + UUID.randomUUID().toString()));
		}
		client.setScope("read,write,trust");
		client = clientRepository.save(client);
		return client;
	}

	@Override
	public Client delete(Integer id) {
		Client client = null;
		if (CheckUtils.isEmpty(id)) {
			return null;
		}
		client = clientRepository.findById(id).get();
		if (null == client) {
			return null;
		}
		clientRepository.delete(client);
		return client;
	}

	@Override
	public Client load(Integer id, String name, String clientId) {
		Client client = null;
		if(!CheckUtils.isEmpty(id)) {
			client = clientRepository.findById(id).get();
		}
		if(null == client && !CheckUtils.isEmpty(name)) {
			client = clientRepository.findByClientNameAndIdNot(name, 0);
		}
		if(null == client && !CheckUtils.isEmpty(clientId)) {
			client = clientRepository.findByClientIdAndIdNot(clientId, 0);
		}
		return client;
	}

	@Override
	public List<Client> list() {
		List<Client> clients = clientRepository.findAll();
		return clients;
	}

	@Override
	public Client current() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null == authentication) {
			return null;
		}
		String clientId = ((OAuth2Authentication) authentication).getOAuth2Request().getClientId();
		if(CheckUtils.isEmpty(clientId)) {
			return null;
		}
		Client client = clientRepository.findByClientIdAndIdNot(clientId, 0);
		return client;
	}
	
	@Override
	public Set<GrantedAuthority> listClientGrantedAuthorities(String clientId) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		if(CheckUtils.isEmpty(clientId)) {
			return authorities;
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
		return authorities;
	}

}
