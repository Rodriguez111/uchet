package uchet.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import uchet.models.Position;
import uchet.models.Sku;

import java.util.List;


public interface PositionRepository extends CrudRepository<Position, Integer>, JpaSpecificationExecutor<Position> {

    Position findByPosition(String position);

    Position findByPositionDescription(String positionDescription);

    @Query("select p from Position p WHERE p.position <> 'admin' ORDER BY p.position")
    List<Position> getAll();

    @Query("SELECT p FROM Position p WHERE p.id <> :id AND position = :position")
    Position findByPositionForUpdate(@Param("position") String code, @Param("id") int id);

    @Query("SELECT p FROM Position p WHERE p.id <> :id AND positionDescription = :description")
    Position findByPositionDescriptionForUpdate(@Param("description") String name, @Param("id") int id);

}
