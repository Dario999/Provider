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
@Table(name = "league")
public class League extends ModelParentable implements Serializable {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parent_id",nullable = false,insertable = false,updatable = false)
    private Country country;

    @JsonManagedReference
    @OneToMany(mappedBy = "league")
    private List<Event> events = new ArrayList<>();

    @Override
    public String toString() {
        return "\t \t " + getName();
    }

}
