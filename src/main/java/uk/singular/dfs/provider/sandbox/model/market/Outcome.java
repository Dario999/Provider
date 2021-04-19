package uk.singular.dfs.provider.sandbox.model.market;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import uk.singular.dfs.provider.sandbox.enums.OutcomeStatus;
import uk.singular.dfs.provider.sandbox.enums.Settlement;
import uk.singular.dfs.provider.sandbox.model.metadata.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "outcome")
public class Outcome extends BaseModel implements Serializable{

    private Double odds;

    private OutcomeStatus status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Settlement settlement;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Transient
    @JsonProperty("outcomeTypeId")
    private Integer outcomeTypeId;

    @ManyToOne
    @JoinColumn(name = "outcome_type_id",referencedColumnName = "id")
    private OutcomeType outcomeType;

    @DateTimeFormat
    private Date messageTimestamp;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\t \tOutcome: id: " + getId() + " Name: " + getName() + " Odds: " + odds + " Status: " + status + " Settlement: " + settlement + "\n");
        return sb.toString();
    }

}
