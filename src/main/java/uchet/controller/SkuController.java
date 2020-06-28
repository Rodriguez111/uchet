package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import uchet.models.Sku;
import uchet.service.sku.SkuService;


import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sku_controller")
public class SkuController {

    @Value("${value-added-tax}")
    private String vatValue;


    private SkuService skuService;


    @GetMapping("/all")
    public List<Sku> listOfAllSku() {
        return skuService.getAll();
    }


    @GetMapping("/get_by_contractor_id/{id}")
    public List<Sku> listOfSkuByContractorId(@PathVariable String id) {
        int contractorId = Integer.parseInt(id);
        return skuService.getListOfSkuByContractorId(contractorId);
    }


    @PostMapping
    @RequestMapping("/local_filter")
    public List<Sku> listOfFilteredSku(@RequestBody Map<String, String> params) {
        return skuService.findByFilter(params);
    }

    @PostMapping
    public Map<String, String> create(@Valid @RequestBody Sku newSku){
        return skuService.addSku(newSku);
    }

    @PutMapping
    public Map<String, String> update(@Valid @RequestBody Sku updatedSku){
        return skuService.updateSku(updatedSku);
    }

    @DeleteMapping("{skuId}") //id передается в заголовке запроса
    public Map<String, String>  delete(@PathVariable String skuId) {
        return skuService.deleteSku(skuId);
    }


    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }


}
