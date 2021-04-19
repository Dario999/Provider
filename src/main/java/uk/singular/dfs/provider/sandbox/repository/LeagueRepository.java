package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.metadata.League;

@Repository
public interface LeagueRepository extends JpaRepository<League,Integer> {

    League findByName(String name);

}
