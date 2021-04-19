package uk.singular.dfs.provider.sandbox.model.market;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import uk.singular.dfs.provider.sandbox.model.metadata.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "outcome_type")
public class OutcomeType extends BaseModel implements Serializable {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "market_type_id",referencedColumnName = "id")
    private MarketType marketType;

    @JsonIgnore
    @OneToMany(mappedBy = "outcomeType")
    private List<Outcome> outcomes;

}
