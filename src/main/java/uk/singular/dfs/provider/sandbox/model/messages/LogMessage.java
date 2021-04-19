package uk.singular.dfs.provider.sandbox.model.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class LogMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    @Lob
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @ColumnTransformer(read = "UNCOMPRESS(content)", write = "COMPRESS(?)")
    private byte[] content;

    @Transient
    private String contentUncompressed;

    public LogMessage(){ }

}
