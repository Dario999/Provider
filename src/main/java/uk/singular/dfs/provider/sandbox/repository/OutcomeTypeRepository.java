package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.market.OutcomeType;

@Repository
public interface OutcomeTypeRepository extends JpaRepository<OutcomeType,Integer> {
}
