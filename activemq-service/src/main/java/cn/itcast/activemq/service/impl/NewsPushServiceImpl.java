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

import cn.itcast.activemq.domain.News;
import cn.itcast.activemq.service.PushService;

@Service("newsPushService")
public class NewsPushServiceImpl implements PushService{
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier("newsServiceQueue")
	private Destination destination;
	
	@Override
	public void push(final Object info) {
		pushExecutor.execute(new Runnable() {
			@Override
			public void run() {
				jmsTemplate.send(destination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						News p = (News)info ;
						return session.createTextMessage(JSONObject.toJSONString(p));
					}
				});
			}
		});
	}
}
