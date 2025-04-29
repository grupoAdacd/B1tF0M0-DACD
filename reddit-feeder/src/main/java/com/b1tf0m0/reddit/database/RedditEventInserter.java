package com.b1tf0m0.reddit.database;

import com.b1tf0m0.common.database.DatabaseManager;
import com.b1tf0m0.reddit.api.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RedditEventInserter {

    public void insertEvent(RedditPost redditPost) {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.getDatabaseUrl())) {
            System.out.println(redditPost);
            String subreddit = redditPost.getSubredditName();
            String author = redditPost.getAuthor();
            String postUrl = redditPost.getPostUrl();
            int numberOfComments = redditPost.getNumberOfComments();
            int subredditSubscribers = redditPost.getSubredditSubscribers();
            long createdUtc = redditPost.getCreatedDateUtc();
            String postText = redditPost.getPostText();
            String title = redditPost.getTitle();

            Date created_date = new Date(createdUtc);

            String sql = "INSERT INTO RedditEvents (subreddit, author, title, post_text, post_link, number_of_comments, number_of_subscribers, sentiment, created_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, subreddit);
                pstmt.setString(2, author);
                pstmt.setString(3, title);
                pstmt.setString(4, postText);
                pstmt.setString(5, postUrl);
                pstmt.setInt(6, numberOfComments);
                pstmt.setInt(7, subredditSubscribers);
                pstmt.setString(8, "");
                pstmt.setDate(9, created_date);

                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
