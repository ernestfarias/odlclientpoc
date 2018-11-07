package hello.nouse;

import hello.Flow;
import org.springframework.data.repository.CrudRepository;
//The IF repository extends Spring Data Commons' CrudRepository and plugs in the type of the domain
// object and its primary key

public interface FlowRepository extends CrudRepository<Flow, String> {
}
