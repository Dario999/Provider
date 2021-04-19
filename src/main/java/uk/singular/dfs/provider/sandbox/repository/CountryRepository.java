package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country,Integer> {

    Country findByName(String name);

}
