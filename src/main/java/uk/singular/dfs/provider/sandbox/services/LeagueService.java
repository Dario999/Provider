package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.metadata.League;

import java.util.List;
import java.util.Optional;

public interface LeagueService {

    League addLeague(League league);
    List<League> addLeagues(List<League> leagues);
    List<League> findAllLeagues();
    Optional<League> findLeagueById(int id);
    void deleteLeague(int id);
    boolean containsEntityWithId(int id);
    boolean parentIsValid(int parentId);
    League updateLeague(League league);
    League findByName(String name);

}
