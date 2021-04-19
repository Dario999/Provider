package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market,Integer> {

    List<Market> findAllByEvent(Event event);

    // native query
    @Query(value = "SELECT * FROM Market m WHERE m.event_id = ?1",nativeQuery = true)
    List<Market> findAllByEventId(Integer id);

    // magic query
    List<Market> findAllByEventIdEquals(Integer id);

}
