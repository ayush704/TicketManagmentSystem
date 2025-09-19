package com.tms.model;

import java.util.UUID;

public class SubTask {
    private final UUID id = UUID.randomUUID();
    private final UUID parentTicketId;
    private String title;
    private Status status = Status.OPEN;

    public SubTask(UUID parentTicketId, String title) {
        this.parentTicketId = parentTicketId;
        this.title = title;
    }

    public UUID getId() { return id; }
    public UUID getParentTicketId() { return parentTicketId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "SubTask{" + "id=" + id + ", title='" + title + '\'' +
               ", status=" + status + '}';
    }
}
