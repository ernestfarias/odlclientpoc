package hello.nouse;

import hello.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowDBservice {

    //private Flow vflow;

    @Autowired
    private FlowRepository repository;

    public void Update(Flow flow) {
      //  this.vflow = flow;

        repository.save(flow);
    }
}

