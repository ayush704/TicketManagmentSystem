package com.tms.service.flow;



import com.tms.model.Status;
import com.tms.service.StateMachine;

public class EpicFlow implements StateMachine {
    @Override
    public boolean canTransit(Status from, Status to) {
        return (from == Status.OPEN && to == Status.IN_PROGRESS) ||
               (from == Status.IN_PROGRESS && to == Status.COMPLETED);
    }
    @Override
    public boolean isTerminal(Status s) { return s == Status.COMPLETED; }
}

