package uchet.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uchet.models.InDocType;
import uchet.models.Unit;

import java.util.List;


public interface InDocTypeRepository extends CrudRepository<InDocType, Integer> {

    InDocType findByDocType(String type);

    @Query("SELECT i FROM InDocType i ORDER BY docType")
    List<InDocType> getAll();


}
