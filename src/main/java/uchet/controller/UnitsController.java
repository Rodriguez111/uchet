package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uchet.models.Unit;
import uchet.service.units.UnitService;

import java.util.List;

@RestController
@RequestMapping("/units_controller")
public class UnitsController {
    private UnitService unitsService;

    @GetMapping
    @RequestMapping("/all")
    public List<Unit> listOfAllUnits() {
        return unitsService.getAll();
    }


    @Autowired
    public void setPositionService(UnitService unitsService) {
        this.unitsService = unitsService;
    }
}
