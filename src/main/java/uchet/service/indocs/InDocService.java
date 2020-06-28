package uchet.service.indocs;

import uchet.models.InDoc;
import uchet.service.filter.SpecificationConstructor;

import java.util.List;
import java.util.Map;

public interface InDocService {
    List<InDoc> getAllInLastMonth();

    Map<String, String> addInDoc(InDoc inDoc);

    Map<String, String> updateInDoc(InDoc inDoc);

    Map<String, String> deleteInDoc(String id);

    InDoc getByInDocNumber(String inDocNumber);

    List<InDoc> findByFilter(Map<String, String> filterParams, SpecificationConstructor<InDoc> constructor);


}
