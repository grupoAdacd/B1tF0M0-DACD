package com.b1tf0m0.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BrokerConnection {
    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";

    private String brokerUrl;
    private String username;
    private String password;

    private Connection connection;
    private Session session;

    public BrokerConnection() {
        loadProperties();
        initializeConnection();
    }

    private void loadProperties() {
        try (InputStream input = BrokerConnection.class.getClassLoader().getResourceAsStream("/home/d4rk/IdeaProjects/B1tF0M0-DACD/src/main/resources/broker.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                brokerUrl = prop.getProperty("broker.url", DEFAULT_BROKER_URL);
                username = prop.getProperty("broker.username", DEFAULT_USERNAME);
                password = prop.getProperty("broker.password", DEFAULT_PASSWORD);
                System.out.println("✅ Configuración de broker cargada correctamente");
            } else {
                System.out.println("⚠️ broker.properties no encontrado, usando valores por defecto");
                brokerUrl = DEFAULT_BROKER_URL;
                username = DEFAULT_USERNAME;
                password = DEFAULT_PASSWORD;
            }
        } catch (IOException e) {
            System.err.println("❌ Error cargando broker.properties: " + e.getMessage());
            brokerUrl = DEFAULT_BROKER_URL;
            username = DEFAULT_USERNAME;
            password = DEFAULT_PASSWORD;
        }
    }

    private void initializeConnection() {
        try {
            System.out.println("🔄 Conectando al broker ActiveMQ en " + brokerUrl);
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
            this.connection = connectionFactory.createConnection();
            this.connection.start();
            this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("✅ Conexión al broker establecida correctamente");
        } catch (JMSException e) {
            System.err.println("❌ Error al conectar con ActiveMQ: " + e.getMessage());
            throw new RuntimeException("Error creando o iniciando la conexión con ActiveMQ", e);
        }
    }

    public void reconnect() {
        closeConnection();
        initializeConnection();
    }

    public void closeConnection() {
        try {
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
            System.out.println("✅ Conexión con ActiveMQ cerrada correctamente");
        } catch (JMSException e) {
            System.err.println("❌ Error cerrando la conexión con ActiveMQ: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Session getSession() {
        return session;
    }
}