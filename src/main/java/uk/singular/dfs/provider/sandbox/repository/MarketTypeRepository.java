package uk.singular.dfs.provider.sandbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.market.MarketType;

@Repository
public interface MarketTypeRepository extends JpaRepository<MarketType,Integer> {

}
