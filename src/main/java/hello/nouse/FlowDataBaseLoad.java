package hello.nouse;

import hello.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Component
public class FlowDataBaseLoad implements CommandLineRunner {

    private final FlowRepository repository;

    @Autowired
    public FlowDataBaseLoad(FlowRepository repository) {
        this.repository = repository;
    }

    public void Update(Flow flow) {
        //  this.vflow = flow;

        repository.save(flow);
    }
    @Override
    public void run(String... args) throws Exception {
//        this.repository.save(new Flow());

    }
}
