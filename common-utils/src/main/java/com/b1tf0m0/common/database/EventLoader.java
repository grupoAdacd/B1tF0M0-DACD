package com.b1tf0m0.common.database;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class EventLoader {

    public static void loadEventsFromFile(File eventFile, EventInserter inserter) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.getDatabaseUrl());
             BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject event = new JSONObject(new JSONTokener(line));
                inserter.insertEvent(event);
            }

            System.out.println("Eventos cargados desde: " + eventFile.getName());

        } catch (Exception e) {
            System.err.println("Error procesando archivo " + eventFile.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
