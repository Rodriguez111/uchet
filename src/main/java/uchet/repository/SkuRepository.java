package uchet.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;

import uchet.models.Sku;

import java.util.List;


public interface SkuRepository extends CrudRepository<Sku, Integer>, JpaSpecificationExecutor<Sku> {

    Sku findByCode(String code);

    Sku findByName(String name);

    List<Sku> findByContractorIdOrderByName(int contractorId);

    @Query("select MAX(code) from Sku sku")
    int findMaxSkuCode();

    @Query("select s from Sku s join fetch s.unit un join fetch s.contractor sup WHERE un.id = sku_unit_id AND sup.id = contractor_id ORDER BY s.name")
    @Override
    List<Sku> findAll();

    @Query("SELECT s FROM Sku s WHERE s.id <> :id AND code = :code")
    Sku findByCodeForUpdate(@Param("code") String code, @Param("id") int id);

    @Query("SELECT s FROM Sku s WHERE s.id <> :id AND name = :name")
    Sku findByNameForUpdate(@Param("name") String name, @Param("id") int id);


}
