package com.b1tf0m0.common.broker;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.JMSException;

public class EventPublisher {

    public static void publishEvent(String topicName, String eventJson) {
        Connection connection = null;
        Session session = null;

        try {
            System.out.println("🚀 Intentando publicar evento en Topic [" + topicName + "]...");

            connection = ActiveMQConnectionManager.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);

            TextMessage message = session.createTextMessage(eventJson);
            producer.send(message);

            System.out.println("Evento publicado en Topic [" + topicName + "]: " + eventJson);

        } catch (JMSException e) {
            System.err.println("Error publicando evento en ActiveMQ: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
