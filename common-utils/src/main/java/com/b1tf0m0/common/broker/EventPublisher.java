package com.b1tf0m0.common.broker;

import com.b1tf0m0.common.json.JSONParse;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.JMSException;
import java.time.Instant;

public class EventPublisher {

    public static void publishEvent(String topicName, String eventJsonString, String sourceSystem) {
        Session session;

        try {
            System.out.println("🚀 Intentando publicar evento en Topic [" + topicName + "]...");

            JSONParse jsonObject = new JSONParse(eventJsonString);

            if (!jsonObject.parseObject().has("ts")) {
                jsonObject.parseObject().put("ts", Instant.now().toString());
            }

            if (!jsonObject.parseObject().has("ss")) {
                jsonObject.parseObject().put("ss", sourceSystem);
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

    public static void publishEvent(String topicName, JSONParse eventData, String sourceSystem) {
        if (!eventData.parseObject().has("ts")) {
            eventData.parseObject().put("ts", Instant.now().toString());
        }
        if (!eventData.parseObject().has("ss")) {
            eventData.parseObject().put("ss", sourceSystem);
        }
        publishEvent(topicName, eventData.toString(), sourceSystem);
    }
}