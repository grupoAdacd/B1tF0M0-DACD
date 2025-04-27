package com.b1tf0m0.reddit.database;

import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.common.database.EventInserter;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class RedditEventInserter implements EventInserter {

    // TODO change the date to the real one
    @Override
    public void insertEvent(JSONObject event) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.getDatabaseUrl())) {
            JSONObject attributes = event.getJSONObject("atributos");

            String subreddit = attributes.getString("subreddit");
            String author = attributes.getString("author");
            String title = attributes.getString("title");
            String selftext = attributes.optString("selftext", "");
            String linkToSite = attributes.getString("url");
            int numComments = attributes.getInt("num_comments");
            int subscribers = attributes.getInt("subreddit_subscribers");

            long createdUtc = attributes.getLong("created_utc");
            LocalDateTime createdDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(createdUtc), ZoneOffset.UTC);
            String createdAt = createdDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String sql = "INSERT INTO RedditEvents (subreddit, author, title, selftext, linkToSite, numComments, subscribers, sentiment, createdAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, subreddit);
                pstmt.setString(2, author);
                pstmt.setString(3, title);
                pstmt.setString(4, selftext);
                pstmt.setString(5, linkToSite);
                pstmt.setInt(6, numComments);
                pstmt.setInt(7, subscribers);
                pstmt.setString(8, "");
                pstmt.setString(9, createdAt);

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
