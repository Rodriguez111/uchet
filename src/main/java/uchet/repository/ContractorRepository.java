package uchet.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uchet.models.Contractor;

import java.util.List;

@Repository
public interface ContractorRepository extends CrudRepository<Contractor, Integer> {

    Contractor findByName(String name);

    Contractor findByContractorCode(String contractorCode);

    @Query("SELECT s FROM Contractor s WHERE s.id <> :id AND name = :name")
    Contractor findByNameForUpdate(@Param("name") String name, @Param("id") int id);

    List<Contractor> findAllByIsSupplierOrderByName(boolean isSupplier);

    List<Contractor> findAllByIsCustomerOrderByName(boolean isCustomer);
}
