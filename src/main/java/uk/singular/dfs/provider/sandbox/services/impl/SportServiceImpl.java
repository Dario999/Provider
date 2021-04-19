package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.repository.EventRepository;
import uk.singular.dfs.provider.sandbox.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.services.SportService;


import java.util.List;
import java.util.Optional;

@Service
public class SportServiceImpl implements SportService {

    private SportRepository sportRepository;

    @Autowired
    public void setSportRepository(SportRepository sportRepository){
        this.sportRepository = sportRepository;
    }

    @Override
    public Sport addSport(Sport sport) {
        return sportRepository.save(sport);
    }

    @Override
    public List<Sport> addSports(List<Sport> sports) {
        return sportRepository.saveAll(sports);
    }

    @Override
    public List<Sport> findAllSports() {
        return sportRepository.findAll();
    }

    @Override
    public Optional<Sport> findSportById(int id) {
        return sportRepository.findById(id);
    }

    @Override
    public Sport findByName(String name) {
        return sportRepository.findSportByName(name);
    }

    @Override
    public void deleteSport(int id) {
        sportRepository.deleteById(id);
    }

    @Override
    public boolean containsEntityWithId(int id) {
        return sportRepository.existsById(id);
    }

    @Override
    public Sport updateSport(Sport sport) {
        return sportRepository.save(sport);
    }

}
