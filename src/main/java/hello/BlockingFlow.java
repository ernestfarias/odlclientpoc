package hello;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Data
@Entity
public class BlockingFlow {

    private  @Id String id;

    private Instant creationTime;

    private int table_id;

    private int hardtimeout;

    private int priority;

    private String ip_destination_match;

    private String flowname;

    public BlockingFlow() {
    }

    public BlockingFlow(String id, Instant creationTime, int table_id, int hardtimeout, int priority, String ip_destination_match, String flowname) {
        this.id = id;
        this.creationTime = creationTime;
        this.table_id = table_id;
        this.hardtimeout = hardtimeout;
        this.priority = priority;
        this.ip_destination_match = ip_destination_match;
        this.flowname = flowname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getHardtimeout() {
        return hardtimeout;
    }

    public void setHardtimeout(int hardtimeout) {
        this.hardtimeout = hardtimeout;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getIp_destination_match() {
        return ip_destination_match;
    }

    public void setIp_destination_match(String ip_destination_match) {
        this.ip_destination_match = ip_destination_match;
    }

    public String getFlowname() {
        return flowname;
    }

    public void setFlowname(String flowname) {
        this.flowname = flowname;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }
}
