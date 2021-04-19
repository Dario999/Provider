package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.services.EventPagingAndSortingService;


@RestController
@RequestMapping("/api/pagination")
public class PagingAndSortingController {

    private final EventPagingAndSortingService eventPagingAndSortingService;

    public PagingAndSortingController(EventPagingAndSortingService eventPagingAndSortingService) {
        this.eventPagingAndSortingService = eventPagingAndSortingService;
    }

    @GetMapping("/find")
    @ApiOperation(value = "Find event by name",response = Event[].class)
    public Page<Event> findEventByNamePageable(@RequestParam String name){
        return eventPagingAndSortingService.findAllByName(name,0,1);
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all events on page with page size",response = Event[].class)
    public Page<Event> findAll(@ApiParam(value = "Page number",required = true)
                               @RequestParam Integer pageNumber,
                               @ApiParam(value = "Page size",required = true)
                               @RequestParam Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        return eventPagingAndSortingService.findAll(pageable);
    }

    @GetMapping("/all/sorted")
    @ApiOperation(value = "Find all events on page with page size and sorted",response = Event[].class)
    public Page<Event> findAllSorted(@ApiParam(value = "Page number",required = true)
                                     @RequestParam Integer pageNumber,
                                     @ApiParam(value = "Page size",required = true)
                                     @RequestParam Integer pageSize,
                                     @ApiParam(value = "Sort asc or desc",required = true)
                                     @RequestParam String sort){
        Pageable pageable = PageRequest.of(pageNumber,pageSize, sort.equals("asc") ? Sort.by("name").ascending() : Sort.by("name").descending());
        return eventPagingAndSortingService.findAll(pageable);
    }

}

