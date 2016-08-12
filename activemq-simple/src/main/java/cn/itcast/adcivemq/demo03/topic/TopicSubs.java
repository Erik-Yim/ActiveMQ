package cn.itcast.adcivemq.demo03.topic;
/*
 * 订阅主题，注：如果在发布主题前，没有订阅，是收不到消息的，这跟点对点的队列模式不同 
 */

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubs {

    public static void main(String[] args) throws JMSException {
    	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");  
        Connection connection = factory.createConnection();  
        connection.setClientID("wm5920");
        connection.start();  
          
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
        Topic topic = session.createTopic("wm5920.topic");  
        
        //持久订阅方式，不会漏掉信息
        TopicSubscriber subs=session.createDurableSubscriber(topic, "wm5920");
        subs.setMessageListener(new MessageListener() {  
            public void onMessage(Message message) {  
                TextMessage tm = (TextMessage) message;  
                try {  
                    System.out.println("Received message: " + tm.getText());  
                } catch (JMSException e) {  
                    e.printStackTrace();  
                }  
            }  
        });
        
        //非持久订阅方式
//        MessageConsumer consumer = session.createConsumer(topic);  
//        consumer.setMessageListener(new MessageListener() {  
//            public void onMessage(Message message) {  
//                TextMessage tm = (TextMessage) message;  
//                try {  
//                    System.out.println("Received message: " + tm.getText());  
//                } catch (JMSException e) {  
//                    e.printStackTrace();  
//                }  
//            }  
//        }); 
//        session.commit();
    //  session.close();  
    //  connection.stop();  
    //  connection.close(); 
	}
}
