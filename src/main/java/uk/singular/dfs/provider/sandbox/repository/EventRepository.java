package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {

    // magic query
    List<Event> findAllByParentIdEquals(Integer id);

    // this is named query
    List<Event> findAllEvents();

    // native query
    @Query(value = "SELECT * FROM Event e WHERE e.id = ?1",nativeQuery = true)
    Event findEventById(Integer id);

}
