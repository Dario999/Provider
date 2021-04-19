package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;
import uk.singular.dfs.provider.sandbox.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.services.CountryService;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final SportRepository sportRepository;

    public CountryServiceImpl(CountryRepository countryRepository, SportRepository sportRepository) {
        this.countryRepository = countryRepository;
        this.sportRepository = sportRepository;
    }

    @Override
    public Country addCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public List<Country> addCountries(List<Country> countries) {
        return countryRepository.saveAll(countries);
    }

    @Override
    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }

    @Override
    public Optional<Country> findCountryById(int id) {
        return countryRepository.findById(id);
    }

    @Override
    public void deleteCountry(int id) {
        countryRepository.deleteById(id);
    }

    @Override
    public boolean containsEntityWithId(int id) {
        return countryRepository.existsById(id);
    }

    @Override
    public boolean parentIsValid(int parentId) {
        return sportRepository.existsById(parentId);
    }

    @Override
    public Country updateCountry(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Country findByName(String name) {
        return countryRepository.findByName(name);
    }


}
