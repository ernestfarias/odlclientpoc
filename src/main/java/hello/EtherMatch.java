package hello;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class EtherMatch {

    @JsonProperty("ethernet-type")
    private EtherType ethernettype = null;

    public EtherMatch() {
    }

    public EtherMatch(EtherType ethernettype) {
        this.ethernettype = ethernettype;
    }

    public EtherType getEthernettype ()
    {
        return ethernettype;
    }

    public void setEthernettype (EtherType ethernettype)
    {
        this.ethernettype = ethernettype;
    }

    public boolean hasEthernetType(){
        return ethernettype != null;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ethernet-type = "+ethernettype+"]";
    }
}
