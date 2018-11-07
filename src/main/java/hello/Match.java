package hello;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Match {

    @JsonProperty("ethernet-match")
    private EtherMatch ethernetmatch;
    @JsonProperty("ipv4-destination")
    private String ipv4destination;


    public EtherMatch getEthernetmatch ()
    {
        return ethernetmatch;
    }

    public void setEthernetmatch (EtherMatch ethernetmatch)
    {
        this.ethernetmatch = ethernetmatch;
    }

    public String getIpv4destination ()
    {
        return ipv4destination;
    }

    public void setIpv4destination (String ipv4destination)
    {
        this.ipv4destination = ipv4destination;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ethernet-match = "+ethernetmatch+", ipv4-destination = "+ipv4destination+"]";
    }
}
