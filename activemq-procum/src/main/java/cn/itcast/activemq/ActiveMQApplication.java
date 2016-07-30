package cn.itcast.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;

@SpringBootApplication
@EnableJms
public class ActiveMQApplication {
	
	/*
	 * 配置ConnectionFactory
		connectionFactory是Spring用于创建到JMS服务器链接的，Spring提供了多种connectionFactory，
		我们介绍两个SingleConnectionFactory和CachingConnectionFactory。
		SingleConnectionFactory：对于建立JMS服务器链接的请求会一直返回同一个链接，并且会忽略Connection的close方法调用。
		CachingConnectionFactory：继承了SingleConnectionFactory，所以它拥有SingleConnectionFactory的所有功能，
		同时它还新增了缓存功能，它可以缓存Session、MessageProducer和MessageConsumer
		
		Spring提供的ConnectionFactory只是Spring用于管理ConnectionFactory的，
		真正产生到JMS服务器链接的ConnectionFactory还得是由JMS服务厂商提供，并且需要把它注入到Spring提供的ConnectionFactory中。
		我们这里使用的是ActiveMQ实现的JMS，所以在我们这里真正的可以产生Connection的就应该是由ActiveMQ提供的ConnectionFactory
	 */
	// ActiveMQ 连接工厂
	public ActiveMQConnectionFactory createActiveMQConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616") ;
		factory.setTrustAllPackages(true);
		return factory;
	}
	
	// Spring Caching连接工厂
	@Bean
	public CachingConnectionFactory createCachingConnectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory() ;
		// 目标ConnectionFactory对应真实的可以产生JMS Connection的ConnectionFactory
		factory.setTargetConnectionFactory(createActiveMQConnectionFactory());
		// Session缓存数量
		factory.setSessionCacheSize(100);
		return factory ;
	}
	
	// 消息处理器
	@Bean
	public SimpleMessageConverter getSimpleMessageConverter() {
		return new SimpleMessageConverter() ;
	}
	
	// ============================== 生产者 ==========================
	// 定义JmsTemplate的Queue类型
	//@Bean
	public JmsTemplate getJmsQueueTemplate(CachingConnectionFactory factory) {
		JmsTemplate jmsTemplate = new JmsTemplate(factory) ;
		// 非pub/sub模型（发布/订阅），即队列模式
		jmsTemplate.setPubSubDomain(false);
		return jmsTemplate ;
	}
	
	// 定义JmsTemplate的Topic类型
	@Bean(name="jmsTopicTemplate")
	public JmsTemplate getJmsTopicTemplate(CachingConnectionFactory factory) {
		JmsTemplate jmsTemplate = new JmsTemplate(factory) ;
		// pub/sub模型（发布/订阅）
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate ;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ActiveMQApplication.class, args) ;
	}
}
