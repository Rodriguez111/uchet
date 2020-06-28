package uchet.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uchet.models.InDoc;

import java.util.List;


public interface InDocRepository extends CrudRepository<InDoc, Long>, JpaSpecificationExecutor<InDoc> {


    @Query("select indoc from InDoc indoc left join fetch indoc.contractor contr left join fetch indoc.owner ow " +
            "WHERE contr.id = contractor_id AND ow.id = owner_id AND indoc.docDate > ADD_DATE(current_date(), -1, MONTH) ORDER BY indoc.docDate desc")
    List<InDoc> getAllInLastMonth();


    @Query("select MAX(docNumber) from InDoc indoc")
    int findMaxDocNumber();

    InDoc findByDocNumber(String docNumber);



}
