package uchet.service.units;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uchet.models.Unit;
import uchet.repository.UnitRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnitServiceImpl implements UnitService {

    private UnitRepository unitRepository;

    @Override
    public List<Unit> getAll() {
        List<Unit> units = (List<Unit>) unitRepository.findAll();
        return sortUnits(units);
    }

    private List<Unit> sortUnits(List<Unit> units) {
        return  units.stream().sorted(Comparator.comparing(Unit::getUnit)).collect(Collectors.toList());
    }

    @Autowired
    public void setUnitRepository(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }
}
