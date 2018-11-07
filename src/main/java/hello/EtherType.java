package hello;

import javax.persistence.Embeddable;

@Embeddable
public class EtherType {

    private int type;

    public int getType ()
    {
        return type;
    }

    public void setType (int type)
    {
        this.type = type;
    }

    public EtherType() {
    }

    public EtherType(int type) {
        this.type = type;
    }

    @Override

    public String toString()
    {
        return "ClassPojo [type = "+type+"]";
    }
}
