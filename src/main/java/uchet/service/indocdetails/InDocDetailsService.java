package uchet.service.indocdetails;

import uchet.models.InDoc;
import uchet.models.InDocDetail;

import java.util.List;
import java.util.Map;

public interface InDocDetailsService {


    List<InDocDetail> getAllInLastMonth();

    Map<String, String> addInDocDetail(InDocDetail detail);

    Map<String, String> updateInDocDetail(InDocDetail detail);

    Map<String, String> deleteInDocDetail(String id);

    List<InDocDetail> findByFilter(Map<String, String> filterParams);

}
