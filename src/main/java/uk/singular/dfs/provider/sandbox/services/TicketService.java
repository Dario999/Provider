package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.ticket.Ticket;
import uk.singular.dfs.provider.sandbox.model.ticket.TicketStatus;

public interface TicketService {

    Integer payTicket(Ticket ticket);
    Ticket findById(Integer id);
    TicketStatus getTicketStatus(Integer id);

}
