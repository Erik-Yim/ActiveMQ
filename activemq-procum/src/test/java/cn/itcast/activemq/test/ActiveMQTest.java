package cn.itcast.activemq.test;


import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.itcast.activemq.ActiveMQApplication;
import cn.itcast.activemq.constant.QueueDefination;
import cn.itcast.activemq.domain.User;
import cn.itcast.activemq.sender.QueueSender;
import cn.itcast.activemq.sender.TopicSender;

/**
 * 单元测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ActiveMQApplication.class})
@WebAppConfiguration("src/main/resources")
public class ActiveMQTest {
	
	@Autowired
	private QueueSender queueSender ;
	
	@Autowired
	private TopicSender topicSender ;
	
	@Test
	public void testSendQueue() {
		User user = new User() ;
		for(int i = 0; i < 100; i++) {
			user.setUsername("张田"+i);
			user.setPassword("123456");
			user.setAge(27);
			user.setOk(true) ;
			queueSender.send(QueueDefination.TEST_QUEUE, user);
		}
		
		try {
			System.in.read() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPublishTopic() {
		String message = "发布消息啦!!!" ;
		topicSender.publish(QueueDefination.TEST_QUEUE, message);
	}
}
