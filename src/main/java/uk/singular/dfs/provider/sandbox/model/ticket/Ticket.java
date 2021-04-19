package uk.singular.dfs.provider.sandbox.model.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "ticket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<TicketOutcome> outcomes;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> outcomeIds;

    private Double payment;

    private Double income;

    private TicketStatus status;

    public Ticket(){
        outcomes = new ArrayList<>();
    }

}
