package cn.itcast.activemq.sender;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

// 队列消息
@Component
public class QueueSender {
	// 通过@Qualifier修饰符来注入对应的bean
	@Autowired
	private JmsTemplate jmsQueueTemplate ;
	
	public void send(String destination, final Object message) {
		jmsQueueTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return jmsQueueTemplate.getMessageConverter().toMessage(message, session);
			}
		});
	}
}
