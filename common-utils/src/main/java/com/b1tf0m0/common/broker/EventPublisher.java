package com.b1tf0m0.common.broker;

import org.json.JSONObject;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.JMSException;
import java.time.Instant;

public class EventPublisher {

    public static void publishEvent(String topicName, String eventJson, String sourceSystem) {
        Connection connection = null;
        Session session = null;

        try {
            System.out.println("🚀 Intentando publicar evento en Topic [" + topicName + "]...");

            JSONObject jsonObject = new JSONObject(eventJson);

            if (!jsonObject.has("ts")) {
                jsonObject.put("ts", Instant.now().toString());
            }

            if (!jsonObject.has("ss")) {
                jsonObject.put("ss", sourceSystem);
            }
            session = ActiveMQConnectionManager.getSession();

            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);

            TextMessage message = session.createTextMessage(jsonObject.toString());
            producer.send(message);

            System.out.println("✅ Evento publicado en Topic [" + topicName + "]: " + jsonObject.toString());
            producer.close();

        } catch (JMSException e) {
            System.err.println("❌ Error publicando evento en ActiveMQ: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error procesando el JSON del evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void publishEvent(String topicName, JSONObject eventData, String sourceSystem) {
        if (!eventData.has("ts")) {
            eventData.put("ts", Instant.now().toString());
        }
        if (!eventData.has("ss")) {
            eventData.put("ss", sourceSystem);
        }
        publishEvent(topicName, eventData.toString(), sourceSystem);
    }
}