package hello;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;

@Data
@Entity
public class Flow {
    private int cookie;

    //private @Id @GeneratedValue Long id_persist;

    private  @Id @GeneratedValue String id;

    @JsonProperty("idle-timeout")
    private int idletimeout;

    private boolean barrier;

    @JsonProperty("hard-timeout")
    private int hardtimeout;

    private Instructions instructions;

    private int priority;

    private boolean installHw;

    private int cookie_mask;
    @Column
    private int table_id;

     private Match match;

    private boolean strict;
@JsonProperty("flow-name")
    private String flowname;

    public int getCookie ()
    {
        return cookie;
    }

    public void setCookie (int cookie)
    {
        this.cookie = cookie;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public int getIdletimeout ()
    {
        return idletimeout;
    }

    public void setIdletimeout (int idletimeout)
    {
        this.idletimeout = idletimeout;
    }

    public boolean getBarrier ()
    {
        return barrier;
    }

    public void setBarrier (boolean barrier)
    {
        this.barrier = barrier;
    }

    public int getHardtimeout ()
    {
        return hardtimeout;
    }

    public void setHardtimeout (int hardtimeout)
    {
        this.hardtimeout = hardtimeout;
    }

    @Embedded
    public Instructions getInstructions() {
        return instructions;
    }


    public void setInstructions(Instructions instructions) {
        this.instructions = instructions;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getInstallHw ()
    {
        return installHw;
    }

    public void setInstallHw (boolean installHw)
    {
        this.installHw = installHw;
    }

    public int getCookie_mask ()
    {
        return cookie_mask;
    }

    public void setCookie_mask (int cookie_mask)
    {
        this.cookie_mask = cookie_mask;
    }

    public int getTable_id ()
    {
        return table_id;
    }

    public void setTable_id (int table_id)
    {
        this.table_id = table_id;
    }

    @Embedded
    public Match getMatch ()
    {
        return match;
    }

    public void setMatch (Match match)
    {
        this.match = match;
    }

    public boolean getStrict ()
    {
        return strict;
    }

    public void setStrict (boolean strict)
    {
        this.strict = strict;
    }

    public String getFlowname ()
    {
        return flowname;
    }

    public void setFlowname (String flowname)
    {
        this.flowname = flowname;
    }

    //constructor


    @Override
    public String toString()
    {
        return "ClassPojo [cookie = "+cookie+", id = "+id+", idle-timeout = "+idletimeout+", barrier = "+barrier+", hard-timeout = "+hardtimeout+", instructions = "+instructions+", priority = "+priority+", installHw = "+installHw+", cookie_mask = "+cookie_mask+", table_id = "+table_id+", match = "+match+", strict = "+strict+", flow-name = "+flowname+"]";
    }
}
