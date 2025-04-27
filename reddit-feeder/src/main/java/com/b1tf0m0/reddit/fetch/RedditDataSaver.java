package com.b1tf0m0.reddit.fetch;

import com.b1tf0m0.common.json.JSONParse;
import com.b1tf0m0.common.save.DefaultFileSaver;
import com.b1tf0m0.reddit.api.RedditApi;

import java.io.File;
import java.io.FileWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class RedditDataSaver extends DefaultFileSaver {
    private static final String DATA_DIRECTORY = "src/main/DataLake/DataLake/reddit_data";
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

    public RedditDataSaver(String data, String where, LocalDateTime now, LocalDateTime name, String DataLakeDirectory) {
        super(data, where, now, name, DataLakeDirectory);
    }

    public static void insertOnFileRedditData(String fetchSpecification, String subredditName) {
        RedditApi redditApi = new RedditApi(fetchSpecification, "");
        String jsonResponse = redditApi.fetchInformation();
        JSONParse jsonParse = new JSONParse(jsonResponse);

        try {
            LocalDateTime now = LocalDateTime.now();
            String fileName = subredditName + "_" + now.format(FILE_FORMAT) + ".txt";

            String subredditDirectory = DATA_DIRECTORY + File.separator + subredditName;
            File subDir = new File(subredditDirectory);
            if (!subDir.exists()) {
                subDir.mkdirs();
            }

            File dataFile = new File(subredditDirectory + File.separator + fileName);

            if (jsonParse.parseObject() != null) {
                try (FileWriter writer = new FileWriter(dataFile)) {
                    writer.write("Reddit Data for r/" + subredditName + " fetched at " +
                            now.format(DISPLAY_FORMAT) + "\n\n");

                    String data = new String(String.valueOf(jsonParse.parseObject().getJSONObject("data")));
                    JSONParse children = new JSONParse(data);
                    children.parseArray().getJSONArray(4);
                    int postCount;
                    postCount = children.parseArray().getJSONArray(4).length();

                    writer.write("Total posts: " + postCount + "\n\n");

                    for (int i = 0; i < postCount; i++) {
                        String subreddit;
                        subreddit = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getString("subreddit");

                        String author;
                        author = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getString("author");

                        String title;
                        title = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getString("title");

                        String selftext;
                        selftext = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").optString("selftext", "");

                        String url;
                        url = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getString("url");

                        int numComments;
                        numComments = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getInt("num_comments");

                        int subscribers;
                        subscribers = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getInt("subreddit_subscribers");

                        long createdUtc;
                        createdUtc = children.parseArray().getJSONArray(4).getJSONObject(i).getJSONObject("data").getLong("created_utc");
                        LocalDateTime postTime = LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(createdUtc), ZoneOffset.UTC);

                        writer.write("Post #" + (i + 1) + "\n");
                        writer.write("Subreddit: r/" + subreddit + "\n");
                        writer.write("Author: u/" + author + "\n");
                        writer.write("Title: " + title + "\n");

                        if (!selftext.isEmpty()) {
                            writer.write("Content: " + selftext + "\n");
                        }

                        writer.write("URL: " + url + "\n");
                        writer.write("Comments: " + numComments + "\n");
                        writer.write("Subscribers: " + subscribers + "\n");
                        writer.write("Created: " + postTime.format(DISPLAY_FORMAT) + "\n");
                        writer.write("\n------------------------------\n\n");
                    }

                    System.out.println("Data saved to file: " + fileName +
                            " (" + postCount + " posts)");
                }
            } else {
                System.out.println("No valid data received for subreddit: " + subredditName);
            }
        } catch (Exception e) {
            System.err.println("Error processing data for " + subredditName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}