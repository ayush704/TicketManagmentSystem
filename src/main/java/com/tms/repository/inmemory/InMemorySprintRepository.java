package com.tms.repository.inmemory;

import com.tms.model.Sprint;
import com.tms.repository.SprintRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySprintRepository implements SprintRepository {
    private final Map<UUID, Sprint> sprints = new ConcurrentHashMap<>();
    private volatile UUID currentSprintId;

    public InMemorySprintRepository() {
        Sprint defaultSprint = new Sprint("Sprint-1");
        sprints.put(defaultSprint.getId(), defaultSprint);
        currentSprintId = defaultSprint.getId();
    }

    @Override
    public Sprint getCurrent() {
        return sprints.get(currentSprintId);
    }

    @Override
    public void setCurrent(Sprint sprint) {
        sprints.putIfAbsent(sprint.getId(), sprint);
        currentSprintId = sprint.getId();
    }

    @Override
    public void addStoryToCurrent(UUID storyTicketId) {
        Sprint current = getCurrent();
        if (current != null) {
            synchronized (current) {
                current.getStoryIds().add(storyTicketId);
            }
        }
    }

    @Override
    public void removeStoryFromCurrent(UUID storyTicketId) {
        Sprint current = getCurrent();
        if (current != null) {
            synchronized (current) {
                current.getStoryIds().remove(storyTicketId);
            }
        }
    }

    @Override
    public Set<UUID> listStoryIdsInCurrent() {
        Sprint current = getCurrent();
        if (current == null) return Collections.emptySet();
        synchronized (current) {
            return new HashSet<>(current.getStoryIds());
        }
    }

    @Override
    public void addSprint(Sprint sprint) {
        sprints.putIfAbsent(sprint.getId(), sprint);
    }

    public List<Sprint> listAllSprints() {
        return new ArrayList<>(sprints.values());
    }

    public Optional<Sprint> findById(UUID id) {
        return Optional.ofNullable(sprints.get(id));
    }
}
