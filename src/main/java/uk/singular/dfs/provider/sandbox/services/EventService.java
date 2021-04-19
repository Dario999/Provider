package uk.singular.dfs.provider.sandbox.services;

import uk.singular.dfs.provider.sandbox.model.metadata.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    Event addEvent(Event event);
    List<Event> addEvents(List<Event> events);
    List<Event> findAllEvents();
    Event findEventById(int id);
    void deleteById(int id);
    Event update(Event event);
    List<Event> findAllEventFromLeague(int id);
    boolean containsEntityWithId(int id);
    boolean parentIsValid(int parentId);

}
