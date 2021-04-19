package uk.singular.dfs.provider.sandbox.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.dto.EventMarketsDto;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.model.metadata.League;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.repository.CountryRepository;
import uk.singular.dfs.provider.sandbox.repository.EventRepository;
import uk.singular.dfs.provider.sandbox.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.repository.SportRepository;
import uk.singular.dfs.provider.sandbox.services.IDataImportService;
import uk.singular.dfs.provider.sandbox.services.MarketService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class ImportDataServiceImpl implements IDataImportService {

    private static final Logger LOG = LoggerFactory.getLogger(ImportDataServiceImpl.class);

    private final SportRepository sportRepository;
    private final CountryRepository countryRepository;
    private final LeagueRepository leagueRepository;
    private final EventRepository eventRepository;
    private final MarketService marketService;

    private ObjectMapper objectMapper;

    public ImportDataServiceImpl(SportRepository sportRepository, CountryRepository countryRepository,
                                 LeagueRepository leagueRepository, EventRepository eventRepository, MarketService marketService){
        this.sportRepository = sportRepository;
        this.countryRepository = countryRepository;
        this.leagueRepository = leagueRepository;
        this.eventRepository = eventRepository;
        this.marketService = marketService;
        objectMapper = new ObjectMapper();
   }

   @PostConstruct
   public void init(){
        importData();
   }

   public void importData() {

        try {
            // importing initial data in sports,countries,leagues and events
            List<Sport> sports = Arrays.stream(objectMapper.readValue(loadFile("data/Sports.json"), Sport[].class)).collect(Collectors.toList());
            sportRepository.saveAll(sports);

            List<Country> countries = Arrays.stream(objectMapper.readValue(loadFile("data/Countries.json"), Country[].class)).collect(Collectors.toList());
            countryRepository.saveAll(countries);

            List<League> leagues = Arrays.stream(objectMapper.readValue(loadFile("data/Leagues.json"), League[].class)).collect(Collectors.toList());
            leagueRepository.saveAll(leagues);

            List<Event> events = Arrays.stream(objectMapper.readValue(loadFile("data/Events.json"), Event[].class)).collect(Collectors.toList());
            eventRepository.saveAll(events);

            EventMarketsDto eventMarketsDto = objectMapper.readValue(loadFile("data/Markets.json"), EventMarketsDto.class);
            marketService.saveAll(eventMarketsDto.getMarkets(),eventMarketsDto.getEventId());

        }catch (FileNotFoundException e){
            LOG.error("File not found exception");
        }catch (IOException e){
            LOG.error("IOException");
        }catch (URISyntaxException e){
            LOG.error("URISyntaxException");
        }

    }

    private File loadFile(String name) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(name);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            return new File(resource.toURI());
        }
    }

}
