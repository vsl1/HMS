package com.biboheart.huip.user.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.huip.user.domain.Client;
import com.biboheart.huip.user.service.ClientService;

@Component("customClientDetailsService")
public class CustomClientDetailsService implements ClientDetailsService {
	@Autowired
	private ClientService clientService;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		ClientDetails details;
		Client client = clientService.load(null, null, clientId);
		if(null == client) {
			throw new NoSuchClientException("Did not find client id：" + clientId );
		}
		details = clientToClientDetails(client);
		return details;
	}
	
	private ClientDetails clientToClientDetails(Client client) {
		if(null == client) {
			return null;
		}
		Set<GrantedAuthority> authorities = clientService.listClientGrantedAuthorities(client.getClientId());
		BaseClientDetails details = new BaseClientDetails(client.getClientId(), client.getResourceIds(), client.getScope(),
				client.getAuthorizedGrantTypes(), client.getAuthorities(), client.getRegisteredRedirectUri());
		// details.setClientSecret(client.getClientSecret());
		details.setClientSecret(new BCryptPasswordEncoder().encode(client.getClientSecret()));
		details.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
		details.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
		details.setAuthorities(authorities);
		Set<String> autoApproveScopes = new HashSet<>();
		if (!CheckUtils.isEmpty(client.getSelf())) {
			autoApproveScopes.add("true");
		}
		details.setAutoApproveScopes(autoApproveScopes);
		details.setAdditionalInformation(new HashMap<String, Object>());
		return details;
	}

}
