package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;
import uk.singular.dfs.provider.sandbox.model.metadata.League;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.services.LeagueService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/league")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all leagues in the database",response = League[].class)
    public List<League> getAllLeagues(){
        return leagueService.findAllLeagues();
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add a new league")
    public ResponseEntity<String> addLeague(@Valid @RequestBody League league){
        if(!leagueService.parentIsValid(league.getParentId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Parent id not valid");
        }else if(leagueService.containsEntityWithId(league.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Duplicate id");
        }else if(leagueService.findByName(league.getName()) != null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("League with name " + league.getName() + " already exists!");
        }else{
            leagueService.addLeague(league);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds league with id",
            notes = "Provide an id to look up specific league",
            response = League.class)
    public Optional<League> findLeagueById(@ApiParam(value = "id value to search in leagues",required = true)
                @PathVariable(name = "id") int id){
        return leagueService.findLeagueById(id);
    }

    @PostMapping("/addLeagues")
    @ApiOperation(value = "Add multiple leagues at once")
    public ResponseEntity<String> addMultipleLeagues(@RequestBody List<League> leagues){
        StringBuilder sb = new StringBuilder();
        for(League league : leagues){
            if(leagueService.findByName(league.getName()) != null){
                sb.append("League with name " + league.getName() + " already exists!\n");
            }else if(leagueService.containsEntityWithId(league.getId())) {
                sb.append("Duplicate id " + league.getId() + " for league " + league.getName() + "\n");
            }else{
                leagueService.addLeague(league);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(sb.toString().isEmpty() ? "Success" : sb.toString());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete league by id")
    public ResponseEntity<String> deleteById(@ApiParam(value = "Id of the league you want to delete",required = true)
                                 @PathVariable(value = "id") int id){
        try {
            leagueService.deleteLeague(id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search league by name")
    public League searchSportByName(@ApiParam(value = "League name",required = true) @RequestParam(name = "name") String name){
        return leagueService.findByName(name);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update league")
    public League updateSport(@RequestBody League league){
        return leagueService.updateLeague(league);
    }

}
