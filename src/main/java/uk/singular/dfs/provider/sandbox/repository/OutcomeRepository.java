package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.market.Outcome;

import java.util.List;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {

    List<Outcome> findAllByMarket(Market market);

}
