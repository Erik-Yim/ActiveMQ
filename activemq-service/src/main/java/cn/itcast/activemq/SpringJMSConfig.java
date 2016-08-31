package cn.itcast.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@ComponentScan(value = {"cn.itcast.activemq.service"})
public class SpringJMSConfig {

	// *********************** ActiveMQ 消息通信配置 ****************************
	// 这里暴露内部统一使用的MQ地址
	@Bean(name = {"internalTargetConnectionFactory"})
	public ActiveMQConnectionFactory createActiveMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory() ;
		factory.setBrokerURL("tcp://localhost:61616");
		return factory ;
	}
	
	@Bean(name={"internalConnectionFactory"},destroyMethod = "stop")
	public PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory factory) {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory() ;
		pooledConnectionFactory.setConnectionFactory(factory);
		pooledConnectionFactory.setMaxConnections(20);
		return pooledConnectionFactory ;
	}
	
	// Spring提供的JMS工具类，它可以进行消息发送、接收等
	@Bean(name={"internalJmsTemplate"})
	public JmsTemplate createJmsTemplate(PooledConnectionFactory pooledConnectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate() ;
		jmsTemplate.setConnectionFactory(pooledConnectionFactory);
		return jmsTemplate ;
	}
	
	// 推送给用户信息  创建一个Queue
	@Bean(name = {"userServiceQueue"})
	public ActiveMQQueue userServiceQueue() {
		return new ActiveMQQueue("user.service.queue") ;
	}
	
	// 推送给新闻信息   创建一个Queue
	@Bean(name = {"newsServiceQueue"})
	public ActiveMQQueue newsServiceQueue() {
		return new ActiveMQQueue("news.service.queue") ;
	}
	
	// 推送给客户信息   创建一个Queue
	@Bean(name = {"clientServiceQueue"})
	public ActiveMQQueue clientServiceQueue() {
		return new ActiveMQQueue("client.service.queue") ;
	}
}
