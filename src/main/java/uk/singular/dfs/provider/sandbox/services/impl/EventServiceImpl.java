package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.model.metadata.League;
import uk.singular.dfs.provider.sandbox.repository.EventRepository;
import uk.singular.dfs.provider.sandbox.repository.LeagueRepository;
import uk.singular.dfs.provider.sandbox.services.EventService;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LeagueRepository leagueRepository;

    public EventServiceImpl(EventRepository eventRepository, LeagueRepository leagueRepository) {
        this.eventRepository = eventRepository;
        this.leagueRepository = leagueRepository;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> addEvents(List<Event> events) {
        return eventRepository.saveAll(events);
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAllEvents();
    }

    @Override
    public Event findEventById(int id) {
        return eventRepository.findEventById(id);
    }

    @Override
    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllEventFromLeague(int id) {
        return eventRepository.findAllByParentIdEquals(id);
    }

    @Override
    public boolean containsEntityWithId(int id) {
        return eventRepository.existsById(id);
    }

    @Override
    public boolean parentIsValid(int parentId) {
        List<League> leagues = leagueRepository.findAll();
        League league = leagues.stream()
                .filter(x -> x.getId() == parentId)
                .findFirst()
                .orElse(null);
        if(league == null){
            return false;
        }
        return true;
    }
}
