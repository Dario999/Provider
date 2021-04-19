package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.ticket.Ticket;
import uk.singular.dfs.provider.sandbox.model.ticket.TicketStatus;
import uk.singular.dfs.provider.sandbox.services.TicketService;


@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/pay")
    @ApiOperation(value = "Pay ticket")
    public ResponseEntity<String> addMultipleSports(@RequestBody Ticket ticket){
        Integer id = ticketService.payTicket(ticket);
        return ResponseEntity.status(HttpStatus.OK).body(id.toString());
    }

    @GetMapping("/status/{id}")
    @ApiOperation(value = "Get ticket status")
    public TicketStatus getTicketStatus(@PathVariable Integer id){
        return ticketService.getTicketStatus(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find by ticket id")
    public Ticket getTicket(@PathVariable Integer id){
        return ticketService.findById(id);
    }

}
