package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.Contractor;
import uchet.service.contractors.ContractorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contractors_controller")
public class ContractorsController {
    private ContractorService contractorService;

    @GetMapping
    @RequestMapping("/all")
    public List<Contractor> listOfAllUsers() {
        return contractorService.getAll();
    }

    @GetMapping
    @RequestMapping("/suppliers")
    public List<Contractor> listOfSuppliers() {
        return contractorService.getSuppliers();
    }

    @GetMapping
    @RequestMapping("/customers")
    public List<Contractor> listOfCustomers() {
        return contractorService.getCustomers();
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> newUser){
        return contractorService.addContractor(newUser);
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> userToUpdate){
        System.out.println("PutMapping");
        return contractorService.updateContractor(id, userToUpdate);
    }

    @DeleteMapping("{userId}") //id передается в заголовке запроса
    public Map<String, String>  delete(@PathVariable String userId) {
        return contractorService.deleteContractor(userId);
    }


    @Autowired
    public void setContractorService(ContractorService contractorService) {
        this.contractorService = contractorService;
    }
}
