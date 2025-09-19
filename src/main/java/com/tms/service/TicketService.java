package com.tms.service;


import com.tms.exception.BusinessRuleException;
import com.tms.exception.InvalidStatusTransitionException;
import com.tms.exception.NotFoundException;
import com.tms.model.*;
import com.tms.repository.SubTaskRepository;
import com.tms.repository.TicketRepository;
import com.tms.service.factory.TicketFlowFactory;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class TicketService {
    private final TicketRepository tickets;
    private final SubTaskRepository subTasks;
    private final ReentrantLock subTaskUpdateLock = new ReentrantLock();
    private final ReentrantLock ticketUpdateLock = new ReentrantLock();

    public TicketService(TicketRepository tickets, SubTaskRepository subTasks) {
        this.tickets = tickets;
        this.subTasks = subTasks;
    }

    public Ticket createTicket(String title, String desc, TicketType type) {
        Ticket t = new Ticket(title, desc, type);
        tickets.save(t);
        return t;
    }

    public Ticket getTicket(UUID id) {
        return tickets.findById(id).orElseThrow(() -> new NotFoundException("Ticket not found: " + id));
    }

    public List<Ticket> listAllTickets() { return tickets.findAll(); }

    public synchronized void addComment(UUID ticketId, String author, String text) {
        getTicket(ticketId).addComment(new Comment(author, text));
    }

    public void updateStatus(UUID ticketId, Status newStatus) {
        try {
            Ticket t = getTicket(ticketId);
            StateMachine sm = TicketFlowFactory.getFlowFor(t.getType());
            ticketUpdateLock.lock();
            if (!sm.canTransit(t.getStatus(), newStatus)) {
                throw new InvalidStatusTransitionException(
                        "Invalid transition for " + t.getType() + ": " + t.getStatus() + " -> " + newStatus);
            }
            if (t.getType() == TicketType.STORY && sm.isTerminal(newStatus)) {
                boolean anyOpen = subTasks.findByParentTicketId(ticketId).stream()
                                          .anyMatch(st -> !sm.isTerminal(st.getStatus()));
                if (anyOpen) throw new BusinessRuleException("Cannot deploy Story: all sub-tasks must be terminal.");
            }
            t.setStatus(newStatus);
            tickets.save(t);
        } finally {
            ticketUpdateLock.unlock();
        }
    }

    public SubTask createSubTask(UUID ticketId, String title) {
        Ticket parent = getTicket(ticketId);
        SubTask st = new SubTask(parent.getId(), title);
        return subTasks.add(st);
    }

    public List<SubTask> listSubTasks(UUID ticketId) {
        getTicket(ticketId);
        return subTasks.findByParentTicketId(ticketId);
    }

    public void updateSubTaskStatus(UUID ticketId, UUID subTaskId, Status newStatus) {
        subTaskUpdateLock.lock();
        try {
            Ticket parent = getTicket(ticketId);
            StateMachine ticketStateMachine = TicketFlowFactory.getFlowFor(parent.getType());
            SubTask st = subTasks.findById(subTaskId)
                                 .orElseThrow(() -> new NotFoundException("Sub-task not found: " + subTaskId));
            if (Boolean.FALSE.equals(st.getParentTicketId().equals(ticketId))) {
                throw new BusinessRuleException("Sub-task does not belong to ticket: " + ticketId);
            }
            subTaskUpdateLock.lock();
            if (Boolean.FALSE.equals(ticketStateMachine.canTransit(st.getStatus(), newStatus))) {
                throw new InvalidStatusTransitionException(
                        "Invalid sub-task transition (" + parent.getType() + " flow): " + st.getStatus() + " -> " +
                        newStatus);
            }
            st.setStatus(newStatus);
            subTasks.update(st);
        } finally {
            subTaskUpdateLock.unlock();
        }
    }

    public synchronized void deleteSubTask(UUID ticketId, UUID subTaskId) {
        getTicket(ticketId);
        subTasks.delete(subTaskId);
    }
}
