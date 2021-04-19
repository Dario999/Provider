package uk.singular.dfs.provider.sandbox.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.singular.dfs.provider.sandbox.model.dto.EventMarketsDto;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.services.MarketService;

import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    private final MarketService marketService;

    private ObjectMapper mapper = new ObjectMapper();

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all markets")
    public List<Market> findAll(){
        return marketService.findAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find market by id")
    public ResponseEntity<String> findMarketById(@ApiParam(value = "Id of the market",required = true)
                                                      @PathVariable(name = "id") Integer id)  {
        try{
            Market market = marketService.findById(id).orElse(null);
            if(market != null){
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(market);
                return ResponseEntity.status(HttpStatus.OK).body(json);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not found");
            }
        }catch (JsonProcessingException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Exception in processing json file");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add new markets")
    public ResponseEntity<String> addMarket(@RequestBody EventMarketsDto eventMarketsDto) {
        if(eventMarketsDto != null && eventMarketsDto.getMarkets() != null && eventMarketsDto.getEventId() != null){
            marketService.saveAll(eventMarketsDto.getMarkets(), eventMarketsDto.getEventId());
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid input");
    }

}
