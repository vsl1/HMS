package com.biboheart.huip.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.biboheart.brick.exception.BhException;
import com.biboheart.brick.model.BhResponseResult;
import com.biboheart.huip.user.domain.Client;
import com.biboheart.huip.user.service.ClientService;

@RestController
public class ClientController {
	@Autowired
	private ClientService clientService;
	
	/**
	 * @param client
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/client/save", method = {RequestMethod.POST})
	public BhResponseResult<?> save(Client client) throws BhException {
		client = clientService.save(client);
		return new BhResponseResult<>(0, "success", client);
	}
	/**
	 * @param id
	 * @return
	 * @throws EberException
	 */
	@RequestMapping(value = "/userapi/client/update", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> update(Integer id) throws BhException {
		Client client = clientService.load(id, null, null);
		if (null == client) {
			throw new BhException("Client don't exist");
		}
		client.setClientId(null);
		client.setClientSecret(null);
		client = clientService.save(client);
		return new BhResponseResult<>(0, "success", client);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/userapi/client/delete", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> delete(Integer id) {
		Client client = clientService.delete(id);
		return new BhResponseResult<>(0, "success", client);
	}
	
	/**
	 * @param id
	 * @param name
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/userapi/client/load", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> load(Integer id, String name, String clientId) {
		Client client = clientService.load(id, name, clientId);
		return new BhResponseResult<>(0, "success", client);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/userapi/client/list", method = {RequestMethod.POST, RequestMethod.GET})
	public BhResponseResult<?> list() {
		List<Client> clients = clientService.list();
		return new BhResponseResult<>(0, "success", clients);
	}
}
