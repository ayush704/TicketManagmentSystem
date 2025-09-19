package com.tms.service;



import com.tms.model.Status;

public interface StateMachine {
    boolean canTransit(Status from, Status to);
    boolean isTerminal(Status s);
}

