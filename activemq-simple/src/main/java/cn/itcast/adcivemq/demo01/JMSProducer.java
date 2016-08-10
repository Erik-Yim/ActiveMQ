package cn.itcast.adcivemq.demo01;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

// 点对点 消息生产者
public class JMSProducer {
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;// 默认的连接的用户名
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;// 默认的连接密码
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认的连接地址
	private static final int SENDNUM = 10;//发送消息的数量
	
	public static void main(String[] args) {
		ConnectionFactory connectionFactory;// 连接工厂
		Connection connection =null;// 连接
		Session session;// 会话 发送或者接受消息的线程
		Destination destination;// 消息的目的地
		MessageProducer messageProducer;// 消息生产者
		// 实例化连接工厂
		connectionFactory = new ActiveMQConnectionFactory(JMSProducer.USERNAME, JMSProducer.PASSWORD,JMSProducer.BROKEURL);
	
		try {
			// 通过连接工厂获取连接
			connection = connectionFactory.createConnection();
			// 启动连接
			connection.start();
			// 创建session,第一个参数代表是否启用事务，第二参数代表接收的方式
			/*
			 * Session.AUTO_ACKNOWLEDGE:当客户成功的从receive方法返回的时候，
			 * 或者从MessageListener. onMessage()方法成功返回后，会话会自动确认客户收到信息。
			 * 
			 * Session.CLIENT_ACKNOWLEDGE:客户通过消息的acknowledge方法确认消息。需要注意的是，在这种模式中
			 * ， 确认是在会话层上进行 ；确认一个被消费的消息将自动确认所有已被会话消费的消息。 例如：如果一个消息消费者消费
			 * 了10个消息，然后确认第5个消息，那么所有10个消息都会被确认
			 * 
			 * Session.DUPS_OK_ACKNOWLEDGE：该选择只是会话迟钝地确认消息的提提交。如果JMSprovider失败，
			 * 那么可能会导致一些重复的消息。
			 * 如果是重复的消息，那么JMSprovider必须把消息头的JMSRedelivered字段设置为true
			 */
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("FirstQuene");//创建消息队列
			messageProducer = session.createProducer(destination);//创建消息生产者
			sendMessage(session, messageProducer, MsgTypeEnum.TEXT);
			//由于设置添加事务，这里需要使用提交才能将数据发送出去
			session.commit();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			if(connection!=null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//发送消息
	public static void sendMessage(Session session,MessageProducer messageProducer,MsgTypeEnum msgTypeEnum){
		try {
			for(int i=0;i<JMSProducer.SENDNUM;i++){
				Object msgObject = "发送消息：Active MQ发送消息"+i;
				System.out.println("发送消息：Active MQ发送消息");
				 switch (msgTypeEnum) {
			        // 发送字节消息
			        case BYTES:
			            BytesMessage msg2 = session.createBytesMessage();
			            msg2.writeBytes(msgObject.toString().getBytes());
			            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			            messageProducer.send(msg2);
			            break;
			        // 发送Map消息
			        case MAP:
			            MapMessage msg = session.createMapMessage();
			            msg.setBoolean("boolean", true);
			            msg.setShort("short", (short) 0);
			            msg.setLong("long", 123456);
			            msg.setString("MapMessage", "ActiveMQ Map Message!");
			            messageProducer.send(msg);
			            break;
			        // 发送对象消息
			        case OBJECT:
			            ObjectMessage msg12 = session.createObjectMessage();
			            msg12.setObject((Serializable) msgObject);
			            messageProducer.send(msg12);
			            break;
			        // 发送流消息
			        case STREAM:
			            StreamMessage msg1 = session.createStreamMessage();
			            msg1.writeBoolean(false);
			            msg1.writeLong(1234567890);
			            messageProducer.send((StreamMessage) msg1);
			            break;
			        // 发送文本消息
			        case TEXT:
			            TextMessage msg11 = session.createTextMessage();
			            msg11.setText((String) msgObject);
			            messageProducer.send(msg11);
			            break;
			        default:
			            break;
			 	}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
