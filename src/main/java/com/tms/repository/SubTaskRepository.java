package com.tms.repository;


import com.tms.model.SubTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubTaskRepository {
    SubTask add(SubTask subTask);
    Optional<SubTask> findById(UUID subTaskId);
    List<SubTask> findByParentTicketId(UUID parentTicketId);
    void delete(UUID subTaskId);
    void update(SubTask subTask);
}
