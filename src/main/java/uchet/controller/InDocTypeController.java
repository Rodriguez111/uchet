package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uchet.models.InDocType;
import uchet.service.indoctypes.InDocTypeService;

import java.util.List;

@RestController
@RequestMapping("/incoming_doc_type_controller")
public class InDocTypeController {
    private InDocTypeService inDocTypeService;

    @GetMapping
    @RequestMapping("/all")
    public List<InDocType> listOfAllTypes() {
        return inDocTypeService.getAll();
    }


    @Autowired
    public void setInDocTypeService(InDocTypeService inDocTypeService) {
        this.inDocTypeService = inDocTypeService;
    }
}
