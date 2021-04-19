package uk.singular.dfs.provider.sandbox.model.metadata;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    @Id
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @NotNull(message = "Name can't be null")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;


}
