package com.tms.service;

import com.tms.exception.BusinessRuleException;
import com.tms.exception.NotFoundException;
import com.tms.model.Sprint;
import com.tms.model.Ticket;
import com.tms.model.TicketType;
import com.tms.repository.SprintRepository;
import com.tms.repository.TicketRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SprintService {
    private final SprintRepository sprints;
    private final TicketRepository tickets;

    public SprintService(SprintRepository sprints, TicketRepository tickets) {
        this.sprints = sprints;
        this.tickets = tickets;
    }

    public synchronized Sprint getCurrentSprint() { return sprints.getCurrent(); }

    public synchronized void addStoryToCurrentSprint(UUID ticketId) {
        Ticket t = tickets.findById(ticketId).orElseThrow(() -> new NotFoundException("Ticket not found: " + ticketId));
        if (t.getType() != TicketType.STORY) throw new BusinessRuleException("Only Story tickets can be in a sprint.");
        sprints.addStoryToCurrent(ticketId);
    }

    public synchronized void removeStoryFromCurrentSprint(UUID ticketId) {
        sprints.removeStoryFromCurrent(ticketId);
    }

    public synchronized Set<UUID> listStoryIdsInSprint() { return sprints.listStoryIdsInCurrent(); }

    public synchronized List<Ticket> listStoriesInSprint() {
        return sprints.listStoryIdsInCurrent().stream()
                      .map(id -> tickets.findById(id).orElse(null))
                      .filter(t -> t != null)
                      .collect(Collectors.toList());
    }
}
