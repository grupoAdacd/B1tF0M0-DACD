package com.b1tf0m0.eventstore;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.Properties;

public class EventStoreBuilder implements MessageListener {

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private final String topicName;
    private final String basePath;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    public EventStoreBuilder(String topicName, String basePath) {
        this.topicName = topicName;
        this.basePath = basePath;

        createBaseDirectory();
    }

    public void start() {
        try {
            System.out.println("🔄 Iniciando EventStoreBuilder para topic: " + topicName);

            String brokerUrl = DEFAULT_BROKER_URL;
            String username = DEFAULT_USERNAME;
            String password = DEFAULT_PASSWORD;

            try (InputStream input = getClass().getClassLoader().getResourceAsStream("broker.properties")) {
                if (input != null) {
                    Properties prop = new Properties();
                    prop.load(input);
                    brokerUrl = prop.getProperty("broker.url", DEFAULT_BROKER_URL);
                    username = prop.getProperty("broker.username", DEFAULT_USERNAME);
                    password = prop.getProperty("broker.password", DEFAULT_PASSWORD);
                    System.out.println("✅ Propiedades del broker cargadas correctamente");
                } else {
                    System.out.println("⚠️ broker.properties no encontrado, usando valores por defecto");
                }
            } catch (IOException e) {
                System.out.println("⚠️ Error al cargar broker.properties: " + e.getMessage());
            }

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);

            String clientId = "EventStoreBuilder-" + topicName + "-" + System.currentTimeMillis();

            connection = connectionFactory.createConnection();
            connection.setClientID(clientId);
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(topicName);
            consumer = session.createDurableSubscriber(topic, "Subscription-" + clientId);
            consumer.setMessageListener(this);

            System.out.println("✅ Suscripción establecida al topic: " + topicName);
        } catch (JMSException e) {
            System.err.println("❌ Error al iniciar EventStoreBuilder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String eventJson = textMessage.getText();
                processAndStoreEvent(eventJson);
            } else {
                System.out.println("⚠️ Mensaje recibido no es TextMessage: " + message.getClass().getName());
            }
        } catch (JMSException e) {
            System.err.println("❌ Error al procesar mensaje: " + e.getMessage());
        }
    }

    private void processAndStoreEvent(String eventJson) {
        try {
            JSONObject jsonObject = new JSONObject(eventJson);

            String ts = jsonObject.getString("ts");
            String ss = jsonObject.getString("ss");

            Instant instant = Instant.parse(ts);
            LocalDate date = instant.atZone(ZoneId.of("UTC")).toLocalDate();
            String fileName = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".events";

            String eventPath = basePath + File.separator + topicName + File.separator + ss;
            Path directoryPath = Paths.get(eventPath);
            Files.createDirectories(directoryPath);
            String filePath = eventPath + File.separator + fileName;
            saveEventToFile(filePath, eventJson);
            System.out.println("✅ Evento almacenado en: " + filePath);
        } catch (Exception e) {
            System.err.println("❌ Error al procesar y almacenar evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveEventToFile(String filePath, String eventJson) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(eventJson);
            writer.write("\n");
        }
    }

    private void createBaseDirectory() {
        try {
            Path eventsStorePath = Paths.get(basePath);
            if (!Files.exists(eventsStorePath)) {
                Files.createDirectories(eventsStorePath);
                System.out.println("✅ Directorio base creado: " + basePath);
            }
        } catch (IOException e) {
            System.err.println("❌ Error al crear directorio base: " + e.getMessage());
        }
    }

    public void stop() {
        try {
            if (consumer != null) {
                consumer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("✅ Suscripción detenida para topic: " + topicName);
        } catch (JMSException e) {
            System.err.println("❌ Error al detener la suscripción: " + e.getMessage());
        }
    }
}