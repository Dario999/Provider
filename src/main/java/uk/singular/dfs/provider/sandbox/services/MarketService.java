package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.market.Outcome;

import java.util.List;
import java.util.Optional;

public interface MarketService {

    Market save(Market market);

    void save(Market market,Integer eventId);

    void saveAll(List<Market> markets,Integer eventId);

    void saveOutcome(Outcome outcome, Integer marketId);

    List<Market> findAll();

    Optional<Market> findById(Integer id);

}
