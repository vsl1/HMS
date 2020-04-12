package com.biboheart.huip.user.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
	@Autowired
	private ConnectionFactory connectionFactory;

	public final static String userRpcServiceExchangeName = "rpc.exchange.huip.user";
	public final static String userLoadRpcServiceQueueName = "rpc.queue.huip.user.load";
	
	@Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
	
	/**
	 * @return
	 */
	@Bean
	public Queue userLoadRpcServiceQueue() {
		return new Queue(userLoadRpcServiceQueueName, false);
	}
	
	/**
	 * @return
	 */
	@Bean
	public DirectExchange userRpcServiceExchange() {
		return new DirectExchange(userRpcServiceExchangeName);
	}
	
	/**
	 * @param userLoadRpcServiceQueue 
	 * @param userUserRpcServiceExchange 
	 * @return
	 */
	@Bean
	@Autowired
	public Binding userLoadRpcServiceQueueBindingUserRpcServiceExchange(Queue userLoadRpcServiceQueue, DirectExchange userRpcServiceExchange) {
		return BindingBuilder.bind(userLoadRpcServiceQueue).to(userRpcServiceExchange).with("load");
	}

}
