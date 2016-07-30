package cn.itcast.activemq.sender;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {

	@Autowired
	private JmsTemplate jmsTopicTemplate ;
	
	/**
	   * 发送一条消息到指定的队列（目标）
	   * @param queueName 队列名称
	   * @param message 消息内容
	   */
	public void publish(String destination, final Object message) {
		jmsTopicTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return jmsTopicTemplate.getMessageConverter().toMessage(message, session) ;
			}
		});
	}
}
