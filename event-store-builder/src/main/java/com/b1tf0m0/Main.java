package com.b1tf0m0.eventstore;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final String EVENT_STORE_BASE_PATH = "src/main/eventstore";
    private static final List<EventStoreBuilder> builders = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Event Store Builder...");
        List<String> topics = Arrays.asList("Weather", "CryptoPrice", "RedditPost");
        for (String topic : topics) {
            EventStoreBuilder builder = new EventStoreBuilder(topic, EVENT_STORE_BASE_PATH);
            builder.start();
            builders.add(builder);
            System.out.println("✅ EventStoreBuilder iniciado para topic: " + topic);
        }
        System.out.println("Event Store Builder en ejecución. Presiona Ctrl+C para detener.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("⚠️ Cerrando conexiones...");
            for (EventStoreBuilder builder : builders) {
                builder.stop();
            }
            System.out.println("👋 Event Store Builder detenido correctamente.");
        }));
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("⚠️ Aplicación interrumpida.");
        }
    }
}