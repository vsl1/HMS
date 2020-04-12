package com.biboheart.huip.user.amqp;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biboheart.brick.utils.CheckUtils;
import com.biboheart.brick.utils.JsonUtils;
import com.biboheart.brick.utils.MapUtils;
import com.biboheart.huip.user.domain.User;
import com.biboheart.huip.user.service.UserService;

@Component
public class RpcUserService {
	@Autowired
	private UserService userService;

	@RabbitListener(queues = RabbitmqConfig.userLoadRpcServiceQueueName)
	public String load(Map<String, Object> params) {
		System.out.println("RPC request users info");
		Long id = MapUtils.getLongValue(params, "id");
		String account = MapUtils.getStringValue(params, "account");
		if (CheckUtils.isEmpty(id) && CheckUtils.isEmpty(account)) {
			return null;
		}
		User user = userService.load(id, account);
		return JsonUtils.obj2json(user);
	}
}
