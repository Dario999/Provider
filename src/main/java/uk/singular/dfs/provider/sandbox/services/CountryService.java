package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.metadata.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    Country addCountry(Country country);
    List<Country> addCountries(List<Country> countries);
    List<Country> findAllCountries();
    Optional<Country> findCountryById(int id);
    void deleteCountry(int id);
    boolean containsEntityWithId(int id);
    boolean parentIsValid(int parentId);
    Country updateCountry(Country country);
    Country findByName(String name);

}
