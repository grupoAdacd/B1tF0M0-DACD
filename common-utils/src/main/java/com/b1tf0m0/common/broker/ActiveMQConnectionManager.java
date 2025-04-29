package com.b1tf0m0.common.broker;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ActiveMQConnectionManager {

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin";
    private static String brokerUrl;
    private static String username;
    private static String password;

    private static Connection connection;
    private static Session session;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        brokerUrl = getEnvOrDefault("ACTIVEMQ_BROKER_URL", DEFAULT_BROKER_URL);
        username = getEnvOrDefault("ACTIVEMQ_USERNAME", DEFAULT_USERNAME);
        password = getEnvOrDefault("ACTIVEMQ_PASSWORD", DEFAULT_PASSWORD);

        try (InputStream input = ActiveMQConnectionManager.class.getClassLoader().getResourceAsStream("/home/d4rk/IdeaProjects/B1tF0M0-DACD/src/main/resources/broker.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                if (System.getenv("ACTIVEMQ_BROKER_URL") == null) {
                    brokerUrl = prop.getProperty("broker.url", brokerUrl);
                }
                if (System.getenv("ACTIVEMQ_USERNAME") == null) {
                    username = prop.getProperty("broker.username", username);
                }
                if (System.getenv("ACTIVEMQ_PASSWORD") == null) {
                    password = prop.getProperty("broker.password", password);
                }
                System.out.println("✅ Configuración cargada:");
                System.out.println("   BROKER_URL = " + brokerUrl);
                System.out.println("   USERNAME = " + username);
                System.out.println("   (Valores pueden provenir de variables de entorno o broker.properties)");
            } else {
                System.out.println("⚠️ broker.properties no encontrado, usando configuración actual");
            }
        } catch (IOException ex) {
            System.err.println("❌ Error cargando broker.properties: " + ex.getMessage());
            System.out.println("   Usando configuración actual");
        }
    }

    public static synchronized Connection getConnection() throws JMSException {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    public static synchronized Session getSession() throws JMSException {
        if (session == null || connection == null) {
            createConnection();
        }
        return session;
    }

    private static void createConnection() throws JMSException {
        System.out.println("🌐 Intentando conectar a ActiveMQ Broker en " + brokerUrl);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        System.out.println("✅ Conexión a ActiveMQ establecida correctamente.");
    }

    public static void closeConnection() {
        try {
            if (session != null) {
                session.close();
                session = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
            System.out.println("✅ Conexión a ActiveMQ cerrada correctamente.");
        } catch (JMSException e) {
            System.err.println("❌ Error al cerrar la conexión con ActiveMQ: " + e.getMessage());
        }
    }

    private static String getEnvOrDefault(String envName, String defaultValue) {
        String value = System.getenv(envName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}