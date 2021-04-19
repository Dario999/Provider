package uk.singular.dfs.provider.sandbox.model.dto;

import lombok.Data;
import uk.singular.dfs.provider.sandbox.model.market.Market;

import java.io.Serializable;
import java.util.List;

@Data
public class EventMarketsDto implements Serializable {

    private Integer eventId;

    private List<Market> markets;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("EventMarket: id: " + eventId + "\n");
        for (Market m : markets){
            sb.append(m);
        }
        return sb.toString();
    }

}
