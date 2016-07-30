package cn.itcast.activemq.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import cn.itcast.activemq.constant.QueueDefination;
import cn.itcast.activemq.domain.User;

@Component
public class SpringQueueReceiver extends MessageListenerAdapter {
	@Override
	@JmsListener(destination=QueueDefination.TEST_QUEUE,concurrency="5-10")
	public void onMessage(Message message, Session session) {
		try {
			User user = (User) getMessageConverter().fromMessage(message) ;
			System.out.println(user.getUsername());
			System.out.println(user.getAge());
			System.out.println(user.isOk());
			System.out.println(session);
			System.out.println("=========================");
			message.acknowledge();
		} catch (MessageConversionException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
