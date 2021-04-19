package uk.singular.dfs.provider.sandbox.model.market;

import com.fasterxml.jackson.annotation.*;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;
import uk.singular.dfs.provider.sandbox.model.metadata.BaseModel;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "market")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Market extends BaseModel implements Serializable{

    @Transient
    private String specifier;

    @Transient
    private Map<String,String> specifiers;

    @JsonProperty(value = "isVariant")
    private boolean isVariant;

    @JsonManagedReference
    @OneToMany(mappedBy = "market",fetch = FetchType.EAGER)
    private List<Outcome> outcomes;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    private Event event;

    @Transient
    @JsonProperty("marketTypeId")
    private Integer marketTypeId;

    @ManyToOne
    @JoinColumn(name = "market_type_id",referencedColumnName = "id")
    private MarketType marketType;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "specifier_map")
    private Map<String,String> specifier_map;

    @DateTimeFormat
    private Date messageTimestamp;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t" + getName());
        if(specifier != null){
            sb.append(String.format("  %.2f",specifier));
        }
        sb.append("\n ");
        if(!isVariant){
            sb.append("\t");
            for (Outcome o : outcomes){
                sb.append(String.format("%8s",o.getName()));
            }
            sb.append("\n\t");
            for (Outcome o : outcomes){
                sb.append(String.format("%8.2f",o.getOdds()));
            }
            sb.append("\n");
        }else{
            for (Outcome o : outcomes){
                sb.append(String.format("\t\t%-25s %8s\n",o.getName(),o.getOdds()));
            }
        }
        return sb.toString();
    }

}
