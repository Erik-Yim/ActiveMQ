package cn.itcast.activemq.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.itcast.activemq.domain.Client;
import cn.itcast.activemq.service.PushService;
@Service("clientPushService")
public class ClientPushServiceImpl implements PushService {
	
	@Autowired
	private JmsTemplate jmsTemplate ;
	
	@Autowired
	@Qualifier("clientServiceQueue")
	private Destination destination ;

	/**
	 * 发送消息
	 * @param info
	 * zhangtian
	 */
	@Override
	public void push(final Object info) {
		pushExecutor.execute(new Runnable() {
			@Override
			public void run() {
				jmsTemplate.send(destination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						Client p = (Client)info ;
						return session.createTextMessage(JSONObject.toJSONString(p));
					}
				});
			}
		});
	}
}
