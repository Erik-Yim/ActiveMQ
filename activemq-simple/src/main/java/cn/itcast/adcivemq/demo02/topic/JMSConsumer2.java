package cn.itcast.adcivemq.demo02.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

// 发布订阅 消息消费者  需要消费者提前订阅   否则复发获取发布的消息  可以有多个
public class JMSConsumer2 {
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;// 默认的连接的用户名
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;// 默认的连接密码
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认的连接地址
	private static final int SENDNUM = 10;// 发送消息的数量
	
	public static void main(String[] args) {
		ConnectionFactory connectionFactory;// 连接工厂
		Connection connection = null;// 连接
		Session session;// 会话 发送或者接受消息的线程
		Destination destination;// 消息的目的地
		MessageConsumer consumer;

		connectionFactory = new ActiveMQConnectionFactory(JMSConsumer2.USERNAME, JMSConsumer2.PASSWORD,
				JMSConsumer2.BROKEURL);
		try {
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE );//创建消息队列
			destination = session.createTopic("FirstTopic");//创建主题
			consumer = session.createConsumer(destination);
			/*通过receive方法接收消息：一般不用receive ，而是用监听
			while(true){
				TextMessage message =  (TextMessage) consumer.receive(100000);//100秒获取一次
				if(message.getText()!=null){
					System.out.println("收到的消息："+message.getText());
				}else{
					break;
				}
			}*/
			//为消息消费者注册监听器
			consumer.setMessageListener(new MessageListener());
			
			
		} catch (JMSException e) {
			e.printStackTrace();
		} 
	}
}
