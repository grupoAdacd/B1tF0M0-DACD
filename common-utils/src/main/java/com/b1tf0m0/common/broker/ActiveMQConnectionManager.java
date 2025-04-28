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
        try (InputStream input = ActiveMQConnectionManager.class.getClassLoader().getResourceAsStream("/home/d4rk/IdeaProjects/B1tF0M0-DACD/src/main/resources/broker.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                brokerUrl = prop.getProperty("broker.url", DEFAULT_BROKER_URL);
                username = prop.getProperty("broker.username", DEFAULT_USERNAME);
                password = prop.getProperty("broker.password", DEFAULT_PASSWORD);
                System.out.println("✅ broker.properties cargado correctamente:");
                System.out.println("   BROKER_URL = " + brokerUrl);
                System.out.println("   USERNAME = " + username);
            } else {
                System.err.println("⚠️ broker.properties no encontrado, usando valores por defecto");
                brokerUrl = DEFAULT_BROKER_URL;
                username = DEFAULT_USERNAME;
                password = DEFAULT_PASSWORD;
            }
        } catch (IOException ex) {
            System.err.println("❌ Error cargando broker.properties: " + ex.getMessage());
            brokerUrl = DEFAULT_BROKER_URL;
            username = DEFAULT_USERNAME;
            password = DEFAULT_PASSWORD;
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
}