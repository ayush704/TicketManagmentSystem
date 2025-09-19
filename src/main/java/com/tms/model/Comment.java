package com.tms.model;

import java.time.Instant;


public class Comment {
    private final String author;
    private final String text;
    private final Instant createdAt = Instant.now();

    public Comment(String author, String text) {
        this.author = author;
        this.text = text;
    }
    public String getAuthor() { return author; }
    public String getText() { return text; }
    public Instant getCreatedAt() { return createdAt; }
}
