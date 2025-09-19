package com.tms.model;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Ticket {
    private final UUID id = UUID.randomUUID();
    private String title;
    private String description;
    private final TicketType type;
    private Status status = Status.OPEN;
    private final List<Comment> comments = new ArrayList<>();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = createdAt;

    public Ticket(String title, String description, TicketType type) {
        this.title = title;
        this.description = description;
        this.type = type;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TicketType getType() { return type; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public List<Comment> getComments() { return comments; }
    public void addComment(Comment c) { comments.add(c); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", title='" + title + '\'' +
               ", type=" + type + ", status=" + status + '}';
    }
}
