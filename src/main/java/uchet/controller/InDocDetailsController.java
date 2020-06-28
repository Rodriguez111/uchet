package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.InDocDetail;
import uchet.service.indocdetails.InDocDetailsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sku_in_controller")
public class InDocDetailsController {

    private InDocDetailsService detailsService;


    @GetMapping("/all")
    public List<InDocDetail> listOfAllInDocDetails() {
        return detailsService.getAllInLastMonth();
    }

    @PostMapping
    @RequestMapping("/local_filter")
    public List<InDocDetail> listOfFilteredSku(@RequestBody Map<String, String> params) {
        return detailsService.findByFilter(params);
    }



//    @GetMapping("/all/{id}")
//    public List<InDocDetail> listOfAllInDocDetails(@PathVariable("id") long id) {
//        InDocDetail d1 = new InDocDetail();
//        d1.setPrice(15);
//        InDocDetail d2 = new InDocDetail();
//        d2.setPrice(20);
//        List<InDocDetail> list = new ArrayList<>();
//        if (id == 1) {
//            list.add(d1);
//        }
//        if (id == 2) {
//            list.add(d2);
//        }
//
//        return list;
//    }



    @Autowired
    public void setDetailsService(InDocDetailsService detailsService) {
        this.detailsService = detailsService;
    }

}
