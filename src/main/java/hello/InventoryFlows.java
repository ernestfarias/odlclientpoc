package hello;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InventoryFlows {
    @JsonProperty("flow-node-inventory:flow")
    private List<Flow> flows;

    public List<Flow> getFlows() {
        return flows;
    }

    public void setFlows(List<Flow> flows) {
        this.flows = flows;
    }
}
