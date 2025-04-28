package com.b1tf0m0.reddit.feeder;

import com.b1tf0m0.common.broker.EventPublisher;
import com.b1tf0m0.common.event.DefaultEventBuilder;
import com.b1tf0m0.common.event.EventFileSaver;
import com.b1tf0m0.common.feeder.Feeder;
import com.b1tf0m0.reddit.api.RedditApi;
import com.b1tf0m0.reddit.database.RedditEventInserter;
import org.json.JSONArray;
import org.json.JSONObject;

public class RedditFeeder implements Feeder {

    private final String subreddit;
    private static final String TOPIC_NAME = "RedditPost";
    private final String SOURCE_SYSTEM;

    public RedditFeeder(String subreddit) {
        this.subreddit = subreddit;
        this.SOURCE_SYSTEM = "RedditFeeder-" + subreddit;
    }

    @Override
    public void fetchAndSaveEvent() {
        System.out.println("🔥 Ejecutando fetchAndSaveEvent en RedditFeeder para r/" + subreddit + "...");
        try {
            // Obtener datos de la API de Reddit
            String apiUrl = String.format("https://www.reddit.com/r/%s/new.json?limit=100&t=all", subreddit);
            RedditApi redditApi = new RedditApi(apiUrl, "");
            String rawJson = redditApi.fetchInformation();

            // Procesar la respuesta JSON
            JSONObject fullResponse = new JSONObject(rawJson);
            JSONObject data = fullResponse.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            int publishedCount = 0;

            // Procesar cada post individual
            for (int i = 0; i < children.length(); i++) {
                JSONObject postData = children.getJSONObject(i).getJSONObject("data");

                // Construir el evento con la estructura requerida
                DefaultEventBuilder eventBuilder = new DefaultEventBuilder();
                String eventJsonString = eventBuilder.buildEvent(SOURCE_SYSTEM, postData.toString());

                if (eventJsonString != null) {
                    // Guardar evento en archivo local (funcionalidad del sprint anterior)
                    EventFileSaver fileSaver = new EventFileSaver();
                    fileSaver.saveEvent(TOPIC_NAME, SOURCE_SYSTEM, eventJsonString);

                    // Insertar en base de datos (funcionalidad del sprint anterior)
                    JSONObject eventJson = new JSONObject(eventJsonString);
                    RedditEventInserter inserter = new RedditEventInserter();
                    inserter.insertEvent(eventJson);

                    // Publicar evento en el broker ActiveMQ (nueva funcionalidad)
                    // Usamos el tercer parámetro para especificar la fuente (ss)
                    EventPublisher.publishEvent(TOPIC_NAME, eventJson, SOURCE_SYSTEM);
                    publishedCount++;
                }
            }

            System.out.println("✅ Eventos capturados de r/" + subreddit + ": " + children.length() +
                    " posts, publicados en topic " + TOPIC_NAME + ": " + publishedCount);

        } catch (Exception e) {
            System.err.println("❌ Error en RedditFeeder para r/" + subreddit + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}