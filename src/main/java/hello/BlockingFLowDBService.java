package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BlockingFLowDBService {

    @Autowired
    BlockingFlowRepository repository;

    @Autowired
    public BlockingFLowDBService(BlockingFlowRepository repository) {
        this.repository = repository;
    }

    public void LoadFlow(String id,Instant ts, int table_id, int hardtimeout, int priority, String ip_destination_match, String flowname){
        repository.save(new BlockingFlow(id, Instant.now(),table_id,hardtimeout,priority,ip_destination_match,flowname));

    }

    public void LoadFlow(BlockingFlow blobkingflow){
         repository.save(blobkingflow);

    }

    public Iterable<BlockingFlow> findAll2(){
        return repository.findAll();

    }

    public List<BlockingFlow> findall3(){
        return (List<BlockingFlow>) repository.findAll();

    }

}
