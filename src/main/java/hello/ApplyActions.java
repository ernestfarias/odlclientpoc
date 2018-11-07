package hello;

import com.fasterxml.jackson.annotation.JsonClassDescription;

import java.util.List;


public class ApplyActions {
    private List<Action> action;

    public List<Action> getAction() {
        return action;
    }

    public void setAction(List<Action> action) {
        this.action = action;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [action = "+action+"]";
    }
}
