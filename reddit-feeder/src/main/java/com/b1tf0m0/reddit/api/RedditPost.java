package com.b1tf0m0.reddit.api;

public class RedditPost {
    private String SubredditName;
    private String PostText;
    private String Title;
    private String Author;
    private String PostUrl;
    private String AttachedUrl;
    private long CreatedDateUtc;
    private int SubredditSubscribers;
    private int NumberOfComments;

    public RedditPost(String subredditName,
                      String postText,
                      String title,
                      String postUrl,
                      String attachedUrl,
                      int subredditSubscribers,
                      int numberOfComments,
                      long createdDateUtc,
                      String author) {
        SubredditName = subredditName;
        PostText = postText;
        Title = title;
        PostUrl = postUrl;
        AttachedUrl = attachedUrl;
        SubredditSubscribers = subredditSubscribers;
        NumberOfComments = numberOfComments;
        CreatedDateUtc = createdDateUtc;
        Author = author;
    }

    public String getSubredditName() {
        return SubredditName;
    }

    public void setSubredditName(String subredditName) {
        SubredditName = subredditName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getPostUrl() {
        return PostUrl;
    }

    public void setPostUrl(String postUrl) {
        PostUrl = postUrl;
    }

    public String getAttachedUrl() {
        return AttachedUrl;
    }

    public void setAttachedUrl(String attachedUrl) {
        AttachedUrl = attachedUrl;
    }

    public long getCreatedDateUtc() {
        return CreatedDateUtc;
    }

    public void setCreatedDateUtc(long createdDateUtc) {
        CreatedDateUtc = createdDateUtc;
    }

    public int getSubredditSubscribers() {
        return SubredditSubscribers;
    }

    public void setSubredditSubscriber(int subredditSubscribers) {
        SubredditSubscribers = subredditSubscribers;
    }

    public int getNumberOfComments() {
        return NumberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        NumberOfComments = numberOfComments;
    }
}