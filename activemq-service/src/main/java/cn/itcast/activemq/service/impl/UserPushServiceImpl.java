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

import cn.itcast.activemq.domain.User;
import cn.itcast.activemq.service.PushService;
@Service("userPushService")
public class UserPushServiceImpl implements PushService {

	@Autowired
	private JmsTemplate jmsTemplate;
	
	/**
	 * 这里是根据MQ配置文件定义的queue来注入的，也就是这里将会把不同的内容推送到不同的queue中
	 */
	@Autowired
	@Qualifier("userServiceQueue")
	private Destination destination;
	
	@Override
	public void push(final Object info) {
		pushExecutor.execute(new Runnable() {
			@Override
			public void run() {
				jmsTemplate.send(destination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						User p = (User)info ;
						return session.createTextMessage(JSONObject.toJSONString(p));
					}
				});
			}
		});
	}
}
