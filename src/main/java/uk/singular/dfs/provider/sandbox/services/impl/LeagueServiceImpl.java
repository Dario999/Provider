package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;
import uk.singular.dfs.provider.sandbox.model.metadata.League;
import uk.singular.dfs.provider.sandbox.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.services.LeagueService;

import java.util.List;
import java.util.Optional;

@Service
public class LeagueServiceImpl implements LeagueService {

    private final LeagueRepository leagueRepository;
    private final CountryRepository countryRepository;

    public LeagueServiceImpl(LeagueRepository leagueRepository, CountryRepository countryRepository) {
        this.leagueRepository = leagueRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    public League addLeague(League league) {
        return leagueRepository.save(league);
    }

    @Override
    public List<League> addLeagues(List<League> leagues) {
        return leagueRepository.saveAll(leagues);
    }

    @Override
    public List<League> findAllLeagues() {
        return leagueRepository.findAll();
    }

    @Override
    public Optional<League> findLeagueById(int id) {
        return leagueRepository.findById(id);
    }

    @Override
    public void deleteLeague(int id) {
        leagueRepository.deleteById(id);
    }

    @Override
    public boolean containsEntityWithId(int id) {
        return leagueRepository.existsById(id);
    }

    @Override
    public boolean parentIsValid(int parentId) {
        List<Country> countries = countryRepository.findAll();
        Country country = countries.stream()
                .filter(x -> x.getId() == parentId)
                .findFirst()
                .orElse(null);
        if(country == null){
            return false;
        }
        return true;
    }

    @Override
    public League updateLeague(League league) {
        return leagueRepository.save(league);
    }

    @Override
    public League findByName(String name) {
        return leagueRepository.findByName(name);
    }
}
