package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.singular.dfs.provider.sandbox.model.ticket.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
}
