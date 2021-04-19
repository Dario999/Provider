package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.model.metadata.League;
import uk.singular.dfs.provider.sandbox.services.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Finds all Events in the database",response = Event[].class)
    public ResponseEntity<List<Event>> getAllEvents(){
        return ResponseEntity.status(HttpStatus.OK).body(eventService.findAllEvents());
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new Event")
    public ResponseEntity<String> addEvent(@Valid @RequestBody Event event) {
        if(!eventService.parentIsValid(event.getParentId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Parent id not valid");
        }else  if(!eventService.containsEntityWithId(event.getId())){
            eventService.addEvent(event);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Duplicate id");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds Event with id",
            notes = "Provide an id to look up specific event",
            response = Event.class)
    public Event findEventById(@ApiParam(value = "ID value for the event",required = true)
                @PathVariable(name = "id") int id){
        return eventService.findEventById(id);
    }

    @PostMapping("/addEvents")
    @ApiOperation(value = "Add multiple Events at once")
    public ResponseEntity<String> addMultipleEvents(@RequestBody List<@Valid Event> events){
        StringBuilder sb = new StringBuilder();
        for(Event event : events){
            if(eventService.containsEntityWithId(event.getId())) {
                sb.append("Duplicate id " + event.getId() + " for event " + event.getName() + "\n");
            }else{
                eventService.addEvent(event);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(sb.toString().isEmpty() ? "Success" : sb.toString());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete event with id")
    public ResponseEntity<String> deleteById(@ApiParam(value = "Id of the event you want to remove")
                                @PathVariable(value = "id") int id){
        try {
            eventService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

 
    @PutMapping("/update")
    @ApiOperation(value = "Update event")
    public Event updateEvent(@Valid @RequestBody Event event){
        return eventService.update(event);
    }

    @GetMapping("/childOf/{id}")
    @ApiOperation(value = "Find all the events in league with league_id")
    public List<Event> getAllParentsWithParentId(@ApiParam(value = "League id",required = true) @PathVariable int id){
        return eventService.findAllEventFromLeague(id);
    }
}
