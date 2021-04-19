package uk.singular.dfs.provider.sandbox.model.metadata;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import uk.singular.dfs.provider.sandbox.enums.EventStatus;
import uk.singular.dfs.provider.sandbox.model.market.Market;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "event")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NamedQuery(name = "Event.findAllEvents",
            query = "select e from Event e")
public class Event extends ModelParentable implements Serializable {

    @NotNull(message = "Date can't be null")
    @JsonAlias({"sd","startDate"})
    @DateTimeFormat
    private Date startDate;

    @NotNull
    private EventStatus status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_id",nullable = false,insertable = false,updatable = false)
    private League league;

    @JsonManagedReference
    @OneToMany(mappedBy = "event")
    private List<Market> markets;

    @Override
    public String toString() {
        return "\t \t \t " + getName();
    }

}
