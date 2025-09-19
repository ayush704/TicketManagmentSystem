package com.tms;



import com.tms.model.*;
import com.tms.repository.SprintRepository;
import com.tms.repository.SubTaskRepository;
import com.tms.repository.TicketRepository;
import com.tms.repository.inmemory.InMemorySprintRepository;
import com.tms.repository.inmemory.InMemorySubTaskRepository;
import com.tms.repository.inmemory.InMemoryTicketRepository;
import com.tms.service.TicketService;
import com.tms.service.SprintService;

import java.util.List;

public class App {
    private static final TicketRepository ticketRepo   = new InMemoryTicketRepository();
    private static final SubTaskRepository subTaskRepo  = new InMemorySubTaskRepository();
    private static final SprintRepository sprintRepo   = new InMemorySprintRepository();

    private static final TicketService ticketService = new TicketService(ticketRepo, subTaskRepo);
    private static final SprintService sprint = new SprintService(sprintRepo, ticketRepo);

    public static void main(String[] args) {
        test_1();
        test_2();
    }

    private static void test_2() {

    }

    private static void test_1() {
        // 1) Create Tickets
        Ticket story = ticketService.createTicket("Implement login feature", "…", TicketType.STORY);
        Ticket epic  = ticketService.createTicket("User authentication", "…", TicketType.EPIC);
        Ticket oncall= ticketService.createTicket("Fix production bug", "…", TicketType.ON_CALL);

        // 2) Update Ticket Status
        ticketService.updateStatus(story.getId(), Status.IN_PROGRESS);

        // 3) Sprint Management
        sprint.addStoryToCurrentSprint(story.getId());
        sprint.removeStoryFromCurrentSprint(story.getId());

        // 4) Sub-task Management
        SubTask st1 = ticketService.createSubTask(story.getId(), "Design login UI");
        // open -> in_progress
        ticketService.updateSubTaskStatus(story.getId(), st1.getId(), Status.IN_PROGRESS);
        // delete sub-task
        ticketService.deleteSubTask(story.getId(), st1.getId());

        // A fuller happy path for Story with subtasks:
        SubTask impl = ticketService.createSubTask(story.getId(), "Implement controller");
        SubTask tests = ticketService.createSubTask(story.getId(), "Add tests");
        ticketService.updateSubTaskStatus(story.getId(), impl.getId(), Status.IN_PROGRESS);
        ticketService.updateSubTaskStatus(story.getId(), impl.getId(), Status.TESTING);
        ticketService.updateSubTaskStatus(story.getId(), impl.getId(), Status.IN_REVIEW);
        ticketService.updateSubTaskStatus(story.getId(), impl.getId(), Status.DEPLOYED);

        ticketService.updateSubTaskStatus(story.getId(), tests.getId(), Status.IN_PROGRESS);
        ticketService.updateSubTaskStatus(story.getId(), tests.getId(), Status.TESTING);
        ticketService.updateSubTaskStatus(story.getId(), tests.getId(), Status.IN_REVIEW);
        ticketService.updateSubTaskStatus(story.getId(), tests.getId(), Status.DEPLOYED);

        // Move parent Story across its flow
        ticketService.updateStatus(story.getId(), Status.TESTING);
        ticketService.updateStatus(story.getId(), Status.IN_REVIEW);   // to IN_REVIEW
        ticketService.updateStatus(story.getId(), Status.DEPLOYED);    // allowed because all subtasks terminal

        // Print state
        System.out.println("All tickets:");
        for (Ticket t : ticketService.listAllTickets()) System.out.println("  " + t);

        System.out.println("\nCurrent sprint stories: " + sprint.listStoryIdsInSprint());

        System.out.println("\nSubtasks for story:");
        List<SubTask> subs = ticketService.listSubTasks(story.getId());
        for (SubTask s : subs) System.out.println("  " + s);
    }
}
