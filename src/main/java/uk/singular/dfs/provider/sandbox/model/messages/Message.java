package uk.singular.dfs.provider.sandbox.model.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import uk.singular.dfs.provider.sandbox.enums.MessageType;
import uk.singular.dfs.provider.sandbox.model.market.Payload;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message implements Serializable {

    private Integer eventId;

    private Payload payload;

    private MessageType type;

}
