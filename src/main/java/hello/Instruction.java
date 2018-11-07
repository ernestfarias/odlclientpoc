package hello;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;

@Embeddable
public class Instruction {
    private int order;
@JsonProperty("apply-actions")
    private ApplyActions applyactions;

    public int getOrder ()
    {
        return order;
    }

    public void setOrder (int order)
    {
        this.order = order;
    }

    public ApplyActions getApplyactions ()
    {
        return applyactions;
    }

    public void setApplyactions (ApplyActions applyactions)
    {
        this.applyactions = applyactions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [order = "+order+", apply-actions = "+applyactions+"]";
    }
}
