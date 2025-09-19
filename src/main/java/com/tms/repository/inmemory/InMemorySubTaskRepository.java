package com.tms.repository.inmemory;


import com.tms.model.SubTask;
import com.tms.repository.SubTaskRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemorySubTaskRepository implements SubTaskRepository {
    private final Map<UUID, SubTask> subTaskByIdMap = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> parentToSubTaskIdsMap = new ConcurrentHashMap<>();

    @Override
    public SubTask add(SubTask subTask) {
        subTaskByIdMap.put(subTask.getId(), subTask);
        parentToSubTaskIdsMap.computeIfAbsent(subTask.getParentTicketId(), k -> new HashSet<>())
                             .add(subTask.getId());
        return subTask;
    }

    @Override
    public Optional<SubTask> findById(UUID subTaskId) {
        return Optional.ofNullable(subTaskByIdMap.get(subTaskId));
    }

    @Override
    public List<SubTask> findByParentTicketId(UUID parentTicketId) {
        return parentToSubTaskIdsMap.getOrDefault(parentTicketId, Collections.emptySet())
                                    .stream().map(subTaskByIdMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID subTaskId) {
        SubTask removed = subTaskByIdMap.remove(subTaskId);
        if (removed != null) {
            Set<UUID> set = parentToSubTaskIdsMap.get(removed.getParentTicketId());
            if (set != null) { set.remove(subTaskId); if (set.isEmpty()) parentToSubTaskIdsMap.remove(removed.getParentTicketId()); }
        }
    }

    @Override
    public void update(SubTask subTask) {
        subTaskByIdMap.put(subTask.getId(), subTask);
    }
}

