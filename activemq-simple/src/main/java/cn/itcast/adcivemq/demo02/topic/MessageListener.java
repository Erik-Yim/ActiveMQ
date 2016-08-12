package cn.itcast.adcivemq.demo02.topic;

import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
// 消息监听器
public class MessageListener implements javax.jms.MessageListener {
	@Override
	public void onMessage(Message msg) {
		try {
			if(msg.getClass().isAssignableFrom(TextMessage.class)) {
				TextMessage message = (TextMessage) msg;
                System.out.println("------Received TextMessage------");
                System.out.println(message.getText());
			} else if(msg.getClass().isAssignableFrom(MapMessage.class)) {
				MapMessage message = (MapMessage) msg;
                System.out.println("------Received MapMessage------");
                System.out.println(message.getLong("long"));
                System.out.println(message.getBoolean("boolean"));
                System.out.println(message.getShort("short"));
                System.out.println(message.getString("MapMessage"));
                System.out.println("------Received MapMessage for while------");
                Enumeration enumer = message.getMapNames();
                while (enumer.hasMoreElements()) {
                    Object obj = enumer.nextElement();
                    System.out.println(message.getObject(obj.toString()));
                }
			} else if(msg.getClass().isAssignableFrom(StreamMessage.class)) {
				StreamMessage message = (StreamMessage) msg;
                System.out.println("------Received StreamMessage------");
                System.out.println(message.readString());
                System.out.println(message.readBoolean());
                System.out.println(message.readLong());
			} else if(msg.getClass().isAssignableFrom(ObjectMessage.class)) {
				System.out.println("------Received ObjectMessage------");
                ObjectMessage message = (ObjectMessage) msg;
                MqMessage mqMessage = (MqMessage) message.getObject();
                System.out.println("name: " + mqMessage.getName());
			} else if (msg.getClass().isAssignableFrom(BytesMessage.class)) {
				System.out.println("------Received BytesMessage------");
                BytesMessage message = (BytesMessage) msg;
                byte[] byteContent = new byte[1024];
                int length = -1;
                StringBuffer content = new StringBuffer();
                while ((length = message.readBytes(byteContent)) != -1) {
                    content.append(new String(byteContent, 0, length));
                }
                System.out.println(content.toString());
			} else {
				 System.out.println(msg);
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
