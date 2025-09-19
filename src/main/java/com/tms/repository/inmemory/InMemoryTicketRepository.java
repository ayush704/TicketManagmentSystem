package com.tms.repository.inmemory;


import com.tms.model.Ticket;
import com.tms.repository.TicketRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTicketRepository implements TicketRepository {
    private final Map<UUID, Ticket> map = new ConcurrentHashMap<>();

    @Override
    public void save(Ticket ticket) {
        map.putIfAbsent(ticket.getId(), ticket);
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(map.values());
    }
}

