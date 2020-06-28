package uchet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uchet.models.Position;
import uchet.service.positions.PositionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/positions_controller")
public class PositionsController {

    private PositionService positionService;


    @GetMapping
    @RequestMapping("/all")
    public List<Position> listOfAllUsers() {
        return positionService.getAll();
    }

    @PostMapping
    @RequestMapping("/local_filter")
    public List<Position> listOfFilteredPositions(@RequestBody Map<String, String> params) {
        return positionService.findByFilter(params);
    }

    @PostMapping
    public Map<String, String> create(@Valid @RequestBody Position newPosition){
        return  positionService.addPosition(newPosition);
    }

    @PutMapping
    public Map<String, String> update(@Valid @RequestBody Position updatePosition){
        return positionService.updatePosition(updatePosition);
    }

    @DeleteMapping("{id}") //id передается в заголовке запроса
    public Map<String, String>  delete(@PathVariable String id) {
        return positionService.deletePosition(id);
    }


    @Autowired
    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }

}
