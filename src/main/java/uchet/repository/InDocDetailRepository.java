package uchet.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uchet.models.InDoc;
import uchet.models.InDocDetail;

import java.util.List;


public interface InDocDetailRepository extends CrudRepository<InDocDetail, Long>, JpaSpecificationExecutor<InDocDetail> {

    @Query("select details from InDocDetail details left join fetch details.inDoc document " +
            "WHERE document.id = in_doc_id AND document.docDate > ADD_DATE(current_date(), -1, MONTH) ORDER BY document.docDate desc")
    List<InDocDetail> getAllInLastMonth();

}
