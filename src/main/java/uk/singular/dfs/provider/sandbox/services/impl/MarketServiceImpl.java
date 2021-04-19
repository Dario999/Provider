package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.market.MarketType;
import uk.singular.dfs.provider.sandbox.model.market.Outcome;
import uk.singular.dfs.provider.sandbox.model.market.OutcomeType;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.repository.*;
import uk.singular.dfs.provider.sandbox.services.MarketService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final OutcomeRepository outcomeRepository;
    private final MarketTypeRepository marketTypeRepository;
    private final OutcomeTypeRepository outcomeTypeRepository;
    private final EventRepository eventRepository;

    public MarketServiceImpl(MarketRepository marketRepository, OutcomeRepository outcomeRepository,
                             MarketTypeRepository marketTypeRepository,
                             OutcomeTypeRepository outcomeTypeRepository, EventRepository eventRepository) {
        this.marketRepository = marketRepository;
        this.outcomeRepository = outcomeRepository;
        this.marketTypeRepository = marketTypeRepository;
        this.outcomeTypeRepository = outcomeTypeRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void saveAll(List<Market> markets, Integer eventId) {
        Event event = eventRepository.findEventById(eventId);
        if(event != null){
            for (Market market : markets) {
                this.save(market,eventId);
            }
        }
    }

    @Override
    public List<Market> findAll() {
        return marketRepository.findAll();
    }

    @Override
    public Optional<Market> findById(Integer id) {
        return marketRepository.findById(id);
    }

    @Override
    public Market save(Market market) {
        return marketRepository.save(market);
    }

    @Override
    public void save(Market market, Integer eventId) {
        Event event = eventRepository.findEventById(eventId);

        if(event != null){

            // return if name and market type are not provided in the data
            if(market.getName() == null || market.getMarketTypeId() == null){
                return;
            }

            Integer marketTypeId = market.getMarketTypeId();
            MarketType marketType = marketTypeRepository.findById(marketTypeId).orElse(null);
            if (marketType == null) {
                marketType = new MarketType();
                marketType.setId(marketTypeId);
                marketType.setVariant(market.isVariant());
                marketType.setName(market.getName());

                // for single specifier
                if (market.getSpecifier() != null) {
                    Map<String,String> specifierMap = new HashMap<>();
                    try {
                        Double.parseDouble(market.getSpecifier());
                        specifierMap.put(market.getName(),"Double");
                    } catch (NumberFormatException e) {
                        specifierMap.put(market.getName(),"String");
                    }
                    marketType.setSpecifier(specifierMap);
                }

                // for multiple specifiers
                if(market.getSpecifiers() != null){
                    Map<String,String> specifiersInput = market.getSpecifiers();
                    Map<String,String> marketTypeMap = new HashMap<>();
                    for(Map.Entry<String,String> entry : specifiersInput.entrySet()){
                        try{
                            Double.parseDouble(entry.getValue());
                            marketTypeMap.put(entry.getKey(),"Double");
                        }catch (NumberFormatException numberFormatException){
                            marketTypeMap.put(entry.getKey(),"String");
                        }
                    }
                    marketType.setSpecifier(marketTypeMap);
                }
                marketTypeRepository.save(marketType);
            }

            // if one specifier then map it and set it in the market
            if(market.getSpecifier() != null){
                Map<String,String> marketSpecifierMap = new HashMap<>();
                marketSpecifierMap.put(market.getName(),market.getSpecifier());
                market.setSpecifier_map(marketSpecifierMap);
            }

            // if multiple specifiers set the map from the json input to the market in db
            if(market.getSpecifiers() != null){
                market.setSpecifier_map(market.getSpecifiers());
            }

            for (Outcome o : market.getOutcomes()) {

                if(o.getName() == null || o.getId() == null || o.getOdds() == null){
                    continue;
                }

                if (!market.isVariant()) {
                    Integer outcomeTypeId = o.getOutcomeTypeId();
                    OutcomeType outcomeType = new OutcomeType();
                    if (outcomeTypeId != null && !checkOutcomeTypeExists(outcomeTypeId)) {
                        outcomeType.setId(outcomeTypeId);
                        outcomeType.setName(o.getName());
                        outcomeType.setMarketType(marketType);
                        outcomeTypeRepository.save(outcomeType);
                    } else {
                        if(outcomeTypeId != null)
                            outcomeType = outcomeTypeRepository.findById(outcomeTypeId).orElse(null);
                    }
                    if(outcomeTypeId != null)
                        o.setOutcomeType(outcomeType);
                }
                o.setMarket(null);
                outcomeRepository.save(o);
            }

            market.setEvent(event);
            market.setMarketType(marketType);

            marketRepository.save(market);
            for (Outcome outcome : market.getOutcomes()) {
                outcome.setMarket(market);
                outcomeRepository.save(outcome);
            }
        }
    }

    @Override
    public void saveOutcome(Outcome outcome,Integer marketId){
        if(outcome.getName() == null || outcome.getId() == null ||
                    outcome.getOdds() == null || outcome.getOutcomeTypeId() == null || marketId == null){
            return;
        }
        Market market = marketRepository.findById(marketId).orElse(null);
        if (!market.isVariant()) {
            Integer outcomeTypeId = outcome.getOutcomeTypeId();
            OutcomeType outcomeType = new OutcomeType();
            if (!checkOutcomeTypeExists(outcomeTypeId)) {
                outcomeType.setId(outcomeTypeId);
                outcomeType.setName(outcome.getName());
                outcomeType.setMarketType(market.getMarketType());
                outcomeTypeRepository.save(outcomeType);
            } else {
                outcomeType = outcomeTypeRepository.findById(outcomeTypeId).orElse(null);
            }
            outcome.setOutcomeType(outcomeType);
        }
        outcome.setMarket(market);
        outcomeRepository.save(outcome);
    }

    public boolean checkOutcomeTypeExists(Integer id){
        return outcomeTypeRepository.existsById(id);
    }


}
