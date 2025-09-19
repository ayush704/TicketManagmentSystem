package com.tms.repository;


import com.tms.model.Sprint;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface SprintRepository {
    Sprint getCurrent();
    void setCurrent(Sprint sprint);

    void addStoryToCurrent(UUID storyTicketId);
    void removeStoryFromCurrent(UUID storyTicketId);
    Set<UUID> listStoryIdsInCurrent();
    void addSprint(Sprint sprint);
}

