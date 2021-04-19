package uk.singular.dfs.provider.sandbox.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.metadata.Country;
import uk.singular.dfs.provider.sandbox.model.metadata.Sport;
import uk.singular.dfs.provider.sandbox.services.CountryService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Finds all countries in the database",
            response = Country[].class)
    public List<Country> getAllCountries(){
        return countryService.findAllCountries();
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add a new country")
    public ResponseEntity<String> addCountry(@Validated @RequestBody Country country){
        if(!countryService.parentIsValid(country.getParentId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Parent id not valid");
        }else if(countryService.findByName(country.getName()) != null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Country with name " + country.getName() + " already exists!");
        }else if(countryService.containsEntityWithId(country.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Duplicate id");
        }else{
            countryService.addCountry(country);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Finds Country with id",notes = "Provide an id to look up specific country",response = Country.class)
    public Optional<Country> findCountryById(@ApiParam(value = "id value to search in countries",required = true)
                @PathVariable(name = "id") int id){
        return countryService.findCountryById(id);
    }

    @PostMapping("/addCountries")
    @ApiOperation(value = "Add multiple countries at once")
    public ResponseEntity<String> addMultipleCountries(@RequestBody List<@Valid Country> countries){
        StringBuilder sb = new StringBuilder();
        for(Country country : countries){
            if(countryService.findByName(country.getName()) != null){
                sb.append("Country with name " + country.getName() + " already exists!\n");
            }else if(countryService.containsEntityWithId(country.getId())) {
                sb.append("Duplicate id " + country.getId() + " for country " + country.getName() + "\n");
            }else{
                countryService.addCountry(country);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(sb.toString().isEmpty() ? "Success" : sb.toString());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete country by id")
    public ResponseEntity<String> deleteById(@ApiParam(value = "Id of the country you want to delete",required = true)
                                  @PathVariable(value = "id") int id){
        try{
            countryService.deleteCountry(id);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search country by name")
    public Country searchSportByName(@ApiParam(value = "Country name",required = true) @RequestParam(name = "name") String name){
        return countryService.findByName(name);
    }

    @PutMapping("/update")
    @ApiOperation(value = "Update country")
    public Country updateSport(@RequestBody Country country){
        return countryService.updateCountry(country);
    }
}
