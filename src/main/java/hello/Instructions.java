package hello;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;


@Embeddable
public class Instructions {
    @Embedded
    private List<Instruction> instruction;

    public List<Instruction> getInstruction() {
        return instruction;
    }

    //constructor

    public void setInstruction(List<Instruction> instruction) {
        this.instruction = instruction;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [instruction = "+instruction+"]";
    }
}
