package uchet.service.contractors;

import uchet.models.Contractor;

import java.util.List;
import java.util.Map;

public interface ContractorService {
    List<Contractor> getAll();

    List<Contractor> getSuppliers();

    List<Contractor> getCustomers();

    Map<String, String> addContractor(Map<String, String> user);

    Map<String, String> updateContractor(String id, Map<String, String> user);

    Map<String, String> deleteContractor(String id);



}
