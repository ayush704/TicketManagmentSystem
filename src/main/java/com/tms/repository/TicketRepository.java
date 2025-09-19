package com.tms.repository;

import com.tms.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    void save(Ticket ticket);
    Optional<Ticket> findById(UUID id);
    List<Ticket> findAll();
}

