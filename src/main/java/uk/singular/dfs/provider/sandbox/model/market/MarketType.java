package uk.singular.dfs.provider.sandbox.model.market;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import uk.singular.dfs.provider.sandbox.model.metadata.BaseModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Data
@Entity
@Table(name = "market_type")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MarketType extends BaseModel implements Serializable {

    private boolean isVariant;

    @Type(type = "json")
    @Column(columnDefinition = "json", name = "specifier")
    private Map<String,String> specifier;

    public MarketType(){}
}
