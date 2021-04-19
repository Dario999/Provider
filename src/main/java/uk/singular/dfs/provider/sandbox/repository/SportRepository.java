package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;

@Repository
public interface SportRepository extends JpaRepository<Sport,Integer> {

    Sport findSportByName(String name);

}
