package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.metadata.Sport;

import java.util.List;
import java.util.Optional;

public interface SportService {

    Sport addSport(Sport sport);
    List<Sport> addSports(List<Sport> sports);
    List<Sport> findAllSports();
    Optional<Sport> findSportById(int id);
    Sport findByName(String name);
    void deleteSport(int id);
    boolean containsEntityWithId(int id);
    Sport updateSport(Sport sport);
}
