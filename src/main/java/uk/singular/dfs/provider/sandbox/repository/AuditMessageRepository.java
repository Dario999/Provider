package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.messages.AuditMessage;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditMessageRepository extends JpaRepository<AuditMessage,Integer> {

    List<AuditMessage> findAllByEventId(Integer id);

    Optional<AuditMessage> findById(Integer id);

    @Query(value ="SELECT m.id,m.event_id,m.create_date,m.update_date,uncompress(m.content) as content " +
            "FROM audit_message m WHERE m.id = ?1",nativeQuery = true)
    AuditMessage findByIdUncompressed(Integer id);

}
