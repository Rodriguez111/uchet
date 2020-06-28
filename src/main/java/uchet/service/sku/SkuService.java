package uchet.service.sku;

import uchet.models.Sku;

import java.util.List;
import java.util.Map;

public interface SkuService {
    List<Sku> getAll();

    Map<String, String> addSku(Sku sku);

    Map<String, String> updateSku(Sku sku);

    Map<String, String> deleteSku(String id);

    List<Sku> getListOfSkuByContractorId(int id);

    List<Sku> findByFilter(Map<String, String> filterParams);

}
