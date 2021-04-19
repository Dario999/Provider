package uk.singular.dfs.provider.sandbox.model.metadata;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "sport")
public class Sport extends BaseModel implements Serializable {

    @JsonManagedReference
    @OneToMany(mappedBy = "sport",fetch = FetchType.LAZY)
    private List<Country> countries = new ArrayList<>();

    @Override
    public String toString() {
        return getName();
    }
}
