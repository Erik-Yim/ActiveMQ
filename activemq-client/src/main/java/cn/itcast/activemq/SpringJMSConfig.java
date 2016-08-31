package cn.itcast.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import cn.itcast.activemq.listener.ClientPushListener;
import cn.itcast.activemq.listener.NewsPushListener;
import cn.itcast.activemq.listener.UserPushListener;

@Configuration
@ComponentScan(value = {"cn.itcast.activemq.listener"})
public class SpringJMSConfig {

	// *********************** ActiveMQ 消息通信配置 ****************************
	// 这里暴露内部统一使用的MQ地址
	@Bean(name = {"targetConnectionFactory"})
	public ActiveMQConnectionFactory createActiveMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory() ;
		factory.setBrokerURL("tcp://localhost:61616");
		return factory ;
	}
	
	@Bean(name={"connectionFactory"},destroyMethod = "stop")
	public PooledConnectionFactory createPooledConnectionFactory(ActiveMQConnectionFactory factory) {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory() ;
		pooledConnectionFactory.setConnectionFactory(factory);
		pooledConnectionFactory.setMaxConnections(50);
		return pooledConnectionFactory ;
	}
	
	// Spring提供的JMS工具类，它可以进行消息发送、接收等
	@Bean(name={"jmsTemplate"})
	public JmsTemplate createJmsTemplate(PooledConnectionFactory pooledConnectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate() ;
		jmsTemplate.setConnectionFactory(pooledConnectionFactory);
		return jmsTemplate ;
	}
	
	// 推送给用户信息  创建一个Queue
	@Bean(name = {"userPushListenerMQ"})
	public ActiveMQQueue userServiceQueue() {
		return new ActiveMQQueue("user.service.queue") ;
	}
	
	// 推送给新闻信息   创建一个Queue
	@Bean(name = {"newsPushListenerMQ"})
	public ActiveMQQueue newsServiceQueue() {
		return new ActiveMQQueue("news.service.queue") ;
	}
	
	// 推送给客户信息   创建一个Queue
	@Bean(name = {"clientPushListenerMQ"})
	public ActiveMQQueue clientServiceQueue() {
		return new ActiveMQQueue("client.service.queue") ;
	}
	
	// 推送给客户信息
	@Bean
	public DefaultMessageListenerContainer userPushListenerConsumer(PooledConnectionFactory connectionFactory, UserPushListener userPushListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer() ;
		container.setConnectionFactory(connectionFactory);
		container.setDestination(userServiceQueue());
		container.setMessageListener(userPushListener);
		return container ;
	}
	
	// 新闻接受推送
	@Bean
	public DefaultMessageListenerContainer newsPushListenerConsumer(PooledConnectionFactory connectionFactory, NewsPushListener newsPushListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer() ;
		container.setConnectionFactory(connectionFactory);
		container.setDestination(newsServiceQueue());
		container.setMessageListener(newsPushListener);
		return container ;
	}
	
	// 客户接受推送 
	@Bean
	public DefaultMessageListenerContainer clientPushListenerConsumer(PooledConnectionFactory connectionFactory, ClientPushListener clientPushListener) {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer() ;
		container.setConnectionFactory(connectionFactory);
		container.setDestination(clientServiceQueue());
		container.setMessageListener(clientPushListener);
		return container ;
	}
}
