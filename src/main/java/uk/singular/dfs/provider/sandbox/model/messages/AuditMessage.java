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
@Table(name = "audit_message")
public class AuditMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer eventId;

    @Lob
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @ColumnTransformer(read = "UNCOMPRESS(content)", write = "COMPRESS(?)")
    private byte[] content;

    @CreationTimestamp
    @Column(name= "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name= "update_date")
    private LocalDateTime updateDate;

    @Transient
    private Message contentUncompressed;

    public AuditMessage(){}

}
