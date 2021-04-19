package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.messages.LogMessage;

@Repository
public interface LogMessagesRepository extends JpaRepository<LogMessage,Integer> {
}
