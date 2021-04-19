package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.market.Outcome;
import uk.singular.dfs.provider.sandbox.model.ticket.Ticket;
import uk.singular.dfs.provider.sandbox.model.ticket.TicketOutcome;
import uk.singular.dfs.provider.sandbox.model.ticket.TicketStatus;
import uk.singular.dfs.provider.sandbox.repository.OutcomeRepository;
import uk.singular.dfs.provider.sandbox.repository.TicketRepository;
import uk.singular.dfs.provider.sandbox.services.TicketService;

import javax.annotation.PostConstruct;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private OutcomeRepository outcomeRepository;

    @Override
    public Integer payTicket(Ticket ticket) {
        Double income = ticket.getPayment();
        for (Integer outcomeId : ticket.getOutcomeIds()) {
            Outcome outcome = outcomeRepository.findById(outcomeId).orElse(null);
            if (outcome != null) {
                Market market = outcome.getMarket();
                Double currentOdds = outcome.getOdds();
                TicketOutcome ticketOutcome = new TicketOutcome();
                ticketOutcome.setOutcomeId(outcomeId);
                ticketOutcome.setCurrentOdds(currentOdds);
                ticketOutcome.setOutcomeName(outcome.getName());
                ticketOutcome.setMarketName(market.getName());
                ticketOutcome.setTicket(ticket);
                ticket.getOutcomes().add(ticketOutcome);
                ticket.setStatus(TicketStatus.InProgress);
                income = income * currentOdds;
            }
        }
        ticket.setIncome(income);
        Ticket ticketSaved = ticketRepository.save(ticket);
        return ticketSaved.getId();
    }

    @Override
    public Ticket findById(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public TicketStatus getTicketStatus(Integer id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null){
            return ticket.getStatus();
        }
        return null;
    }
}
