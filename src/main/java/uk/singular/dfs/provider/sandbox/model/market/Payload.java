package uk.singular.dfs.provider.sandbox.model.market;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Payload implements Serializable {

    private Event event;

    private List<Market> markets;

}
