package uchet.repository;

import org.springframework.data.repository.CrudRepository;
import uchet.models.Position;
import uchet.models.Unit;


public interface UnitRepository extends CrudRepository<Unit, Integer> {

    Unit findByUnit(String unit);

}
