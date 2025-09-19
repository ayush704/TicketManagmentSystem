package com.tms.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Sprint {
    private final UUID id = UUID.randomUUID();
    private String name;
    private final Set<UUID> storyIds = new HashSet<>();

    public Sprint(String name) { this.name = name; }

    public UUID getId() { return id; }
    public Set<UUID> getStoryIds() { return storyIds; }
}

