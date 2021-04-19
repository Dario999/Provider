package uk.singular.dfs.provider.sandbox.model.ticket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TicketOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    private Integer outcomeId;

    private String outcomeName;

    private String marketName;

    private Double currentOdds;

    @ManyToOne
    @JsonBackReference
    private Ticket ticket;

}
