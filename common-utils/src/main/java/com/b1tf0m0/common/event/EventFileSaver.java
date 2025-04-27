package com.b1tf0m0.common.event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventFileSaver {

    private static final String BASE_EVENTSTORE_PATH = "src/main/eventstore/";

    public void saveEvent(String topic, String sourceSource, String eventJson) {
        try {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String directoryPath = BASE_EVENTSTORE_PATH + topic + "/" + sourceSource;
            String filePath = directoryPath + "/" + date + ".events";

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(filePath);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(eventJson + "\n");
            }

            System.out.println("Evento guardado en: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error guardando el evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
