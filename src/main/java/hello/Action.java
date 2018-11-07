package hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.util.Map;

public class Action {
    private int order;
@JsonProperty("drop-action")
    private Map<String, Object> dropaction;
 //   @JsonProperty("drop-action2")
//    private String dropaction2;

    public int getOrder ()
    {
        return order;
    }

    public void setOrder (int order)
    {
        this.order = order;
    }

    public Map<String, Object> getDropaction() {
        return dropaction;
    }

    public void setDropaction(Map<String, Object> dropaction) {
        this.dropaction = dropaction;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [order = "+order+", drop-action = "+dropaction+"]";
    }
}
