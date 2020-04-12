package com.biboheart.huip.patient.amqp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biboheart.brick.utils.JsonUtils;

@Component
public class RpcPatientUserClient {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> load(Long id, String account) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("account", account);
		Object response = rabbitTemplate.convertSendAndReceive("rpc.exchange.huip.user", "load", params);
		if (null == response) {
			return null;
		}
		Map<String, Object> result = (Map<String, Object>) JsonUtils.json2obj(String.valueOf(response), HashMap.class);
		return result;
	}
}
