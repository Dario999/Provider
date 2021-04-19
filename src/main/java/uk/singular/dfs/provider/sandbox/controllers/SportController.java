package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.services.SportService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sport")
public class SportController {

    private final SportService sportService;

    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Finds all Sports in the database",
            response = Sport[].class)
    public List<Sport> getAllSports(){
        return sportService.findAllSports();
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add a new sport")
    public ResponseEntity<String> addSport(@Valid @RequestBody Sport sport){
        if(sportService.findByName(sport.getName()) != null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sport with name " + sport.getName() + " already exists!");
        }else if(sportService.containsEntityWithId(sport.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Duplicate id");
        }else{
            sportService.addSport(sport);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds Sport with id",
            notes = "Provide an id to look up specific sport",
            response = Sport.class)
    public Optional<Sport> findSportById(@ApiParam(value = "id value to search in sports",required = true)
            @PathVariable(name = "id") int id){
        return sportService.findSportById(id);
    }

    @PostMapping("/addSports")
    @ApiOperation(value = "Add multiple sports at once")
    public ResponseEntity<String> addMultipleSports(@RequestBody List<@Valid Sport> sports){
        StringBuilder sb = new StringBuilder();
        for(Sport sport : sports){
            if(sportService.findByName(sport.getName()) != null){
                sb.append("Sport with name " + sport.getName() + " already exists!\n");
            }else if(sportService.containsEntityWithId(sport.getId())) {
                sb.append("Duplicate id " + sport.getId() + " for sport " + sport.getName() + "\n");
            }else{
                sportService.addSport(sport);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(sb.toString().isEmpty() ? "Success" : sb.toString());
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search sport by name")
    public Sport searchSportByName(@ApiParam(value = "Sport name",required = true) @RequestParam(name = "name") String name){
        return sportService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete sport by id")
    public ResponseEntity<String> deleteById(@ApiParam(value = "Id of the sport you want to delete",required = true)
                                @PathVariable(value = "id") int id){
        try {
            sportService.deleteSport(id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update sport")
    public Sport updateSport(@Valid @RequestBody Sport sport){
        return sportService.updateSport(sport);
    }

}
