package uk.singular.dfs.provider.sandbox.model.metadata;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class ModelParentable extends BaseModel implements Serializable {

    @JsonAlias({"sportId","leagueId","countryId"})
    @NotNull(message = "Parent id can't be null")
    @Column(name = "parent_id")
    private Integer parentId;

}
