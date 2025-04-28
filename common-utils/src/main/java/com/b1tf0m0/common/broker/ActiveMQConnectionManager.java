package com.b1tf0m0.common.broker;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ActiveMQConnectionManager {

    private static String BROKER_URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        try (InputStream input = ActiveMQConnectionManager.class.getClassLoader().getResourceAsStream("broker.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                BROKER_URL = prop.getProperty("broker.url");
                USERNAME = prop.getProperty("broker.username");
                PASSWORD = prop.getProperty("broker.password");
                System.out.println("✅ broker.properties cargado correctamente:");
                System.out.println("   BROKER_URL = " + BROKER_URL);
                System.out.println("   USERNAME = " + USERNAME);
                // Nunca muestres PASSWORD real en consola en producción
            } else {
                System.err.println("❌ broker.properties no encontrado en resources.");
                throw new RuntimeException("No se encontró el archivo broker.properties");
            }
        } catch (IOException ex) {
            System.err.println("❌ Error cargando broker.properties: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static Connection createConnection() throws JMSException {
        System.out.println("🌐 Intentando conectar a ActiveMQ Broker en " + BROKER_URL);
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        System.out.println("✅ Conexión a ActiveMQ establecida correctamente.");
        return connection;
    }
}
