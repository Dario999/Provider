package uk.singular.dfs.provider.sandbox.model.metadata;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "country")
public class Country extends ModelParentable implements Serializable {

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",nullable = false,insertable = false,updatable = false)
    private Sport sport;

    @JsonManagedReference
    @OneToMany(mappedBy = "country")
    private List<League> leagues = new ArrayList<>();

    @Override
    public String toString() {
        return "\t " + getName();
    }

}
