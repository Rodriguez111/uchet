package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.InDoc;
import uchet.service.filter.GlobalFilterSpecifConstructor;
import uchet.service.filter.LocalFilterSpecifConstructor;
import uchet.service.filter.SpecificationConstructor;
import uchet.service.indocs.InDocService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/incoming_docs_controller")
public class InDocController {

    private InDocService inDocService;





    @GetMapping("/all")
    public List<InDoc> listOfAllSkuInLastMonth() {
        return inDocService.getAllInLastMonth();
    }

    @PostMapping
    @RequestMapping("/local_filter")
    public List<InDoc> listOfInDocsByLocalFilter(@RequestBody Map<String, String> params) {
        SpecificationConstructor<InDoc> constructor = new LocalFilterSpecifConstructor<>();
        return inDocService.findByFilter(params, constructor);
    }

    @PostMapping
    @RequestMapping("/global_filter")
    public List<InDoc> listOfInDocsByGlobalFilter(@RequestBody Map<String, String> params) {
        SpecificationConstructor<InDoc> constructor = new GlobalFilterSpecifConstructor<>();
        return inDocService.findByFilter(params, constructor);
    }


    @PostMapping
    public Map<String, String> create(@Valid @RequestBody InDoc inDoc){
        return inDocService.addInDoc(inDoc);
    }


    @Autowired
    public void setInDocService(InDocService inDocService) {
        this.inDocService = inDocService;
    }


}
