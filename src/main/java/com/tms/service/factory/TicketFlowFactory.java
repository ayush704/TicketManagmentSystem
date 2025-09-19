package com.tms.service.factory;

import com.tms.model.TicketType;
import com.tms.service.StateMachine;
import com.tms.service.flow.EpicFlow;
import com.tms.service.flow.OnCallFlow;
import com.tms.service.flow.StoryFlow;

import java.util.EnumMap;
import java.util.Map;

public class TicketFlowFactory {
    private static final Map<TicketType, StateMachine> flows = new EnumMap<>(TicketType.class);
    static {
        flows.put(TicketType.STORY, new StoryFlow());
        flows.put(TicketType.EPIC, new EpicFlow());
        flows.put(TicketType.ON_CALL, new OnCallFlow());
    }

    public static StateMachine getFlowFor(TicketType type) { return flows.get(type); }
}
