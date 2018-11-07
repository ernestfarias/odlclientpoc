package hello;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlockingFlowRepository extends PagingAndSortingRepository<BlockingFlow, String> {
//findByIpDestinationMatch();
// BlockingFlow findOne(String id);


}

