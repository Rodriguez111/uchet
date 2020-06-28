package uchet.service.sku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uchet.models.*;
import uchet.repository.*;
import uchet.service.daoutils.EntityFieldsUpdater;
import uchet.service.daoutils.FieldsUpdater;
import uchet.service.filter.FilterService;
import uchet.service.filter.FilterServiceImpl;
import uchet.service.filter.LocalFilterSpecifConstructor;
import uchet.service.filter.SpecificationConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class SkuServiceImpl implements SkuService {

    private static final int NUMBER_OF_DIGITS_IN_CODE = 6;


    @Value("#{new Double('${value-added-tax}')}")
    private double vatValue;


    private SkuRepository skuRepository;

    private ContractorRepository contractorRepository;

    private UnitRepository unitRepository;


    private Map<String, BiConsumer<Sku, String>> getFieldsUpdateDispatcher() {
        Map<String, BiConsumer<Sku, String>> fieldsUpdateDispatcher = new HashMap<>();
        fieldsUpdateDispatcher.put("name", Sku::setName);
        fieldsUpdateDispatcher.put("quantityInSecondaryPackaging", (sku, parameter) -> {
            int pkg = Integer.parseInt(parameter);
            sku.setQuantityInSecondaryPackaging(pkg);
        });
        fieldsUpdateDispatcher.put("isActive", (user, parameter) -> {
            boolean isActive = parameter.equals("true");
            user.setActive(isActive);
        });
        fieldsUpdateDispatcher.put("contractor", (sku, parameter) -> {
            Contractor contractor = contractorRepository.findByContractorCode(parameter);
            sku.setContractor(contractor);
        });
        fieldsUpdateDispatcher.put("unit", (sku, parameter) -> {
            Unit unit = unitRepository.findByUnit(parameter);
            sku.setUnit(unit);
        });
        return fieldsUpdateDispatcher;
    }

    @Override
    public List<Sku> getAll() {
        return skuRepository.findAll();
    }


    @Override
    public List<Sku> getListOfSkuByContractorId(int id) {
        List<Sku> lst = skuRepository.findByContractorIdOrderByName(id);
        lst.stream().forEach(e -> e.setVatValue(vatValue));
        return lst;
    }

    @Override
    public List<Sku> findByFilter(Map<String, String> filterParams) {
        Function<Specification<Sku>, List<Sku>> function = spc -> skuRepository.findAll(spc);
        SpecificationConstructor<Sku> constructor = new LocalFilterSpecifConstructor<>();
        FilterService<Sku> filterService = new FilterServiceImpl<>(function, constructor);
        return filterService.findEntriesByFilter(filterParams);
    }

    @Transactional
    @Override
    public Map<String, String> addSku(Sku sku) {
        String resultMessage = "OK";
        String code = generateSkuCode();
        String name = sku.getName();
        Contractor contractor = contractorRepository.findByName(sku.getContractor().getName());
        Unit unit = unitRepository.findByUnit(sku.getUnit().getUnit());
        int quantityInSecondaryPackaging = sku.getQuantityInSecondaryPackaging();
        boolean isActive = sku.isActive();

        if (skuNameExists(name)) {
            resultMessage = "Товар " + name + " уже существует";
        } else {
            Sku newSku = new Sku(code, name, unit, contractor, quantityInSecondaryPackaging, isActive);
            skuRepository.save(newSku);
        }
        Map<String, String> result = new HashMap<>();
        result.put("addResult", resultMessage);
        return result;
    }



    private boolean skuNameExists(String skuName) {
        boolean result = false;
        Sku existingSku = skuRepository.findByName(skuName);
        if (existingSku != null) {
            result = true;
        }
        return result;
    }


    private boolean skuNameExistsForUpdate(int id, String skuName) {
        boolean result = false;
        Sku existingSku = skuRepository.findByNameForUpdate(skuName, id);
        if (existingSku != null) {
            result = true;
        }
        return result;
    }


    @Transactional
    @Override
    public Map<String, String> updateSku(Sku sku) {
        String resultMessage = "OK";
        Optional<Sku> optionalSku = skuRepository.findById(sku.getId());
        if (optionalSku.isPresent()) {
            Sku existingSku = optionalSku.get();
            Sku updatedSku = getPreparedCandidateForUpdate(existingSku, sku);
            if (existingSku.equals(updatedSku)) {
                resultMessage = "Нет полей для обновления";
            } else if (skuNameExistsForUpdate(existingSku.getId(), updatedSku.getName())) {
                resultMessage = "Товар \"" + updatedSku.getName() + "\" уже существует";
            } else {
                skuRepository.save(updatedSku);
            }
        }
        Map<String, String> result = new HashMap<>();
        result.put("updateResult", resultMessage);
        return result;
    }

    private Sku getPreparedCandidateForUpdate(Sku existingSku, Sku skuFromClient) {
        EntityFieldsUpdater<Sku> updater = new FieldsUpdater<>(getFieldsUpdateDispatcher());
        Map<String, String> fieldsToUpdate = updater.getMapOfFields(skuFromClient);
        fieldsToUpdate.remove("code");
        fieldsToUpdate.remove("vatValue");
        fieldsToUpdate.put("contractor", skuFromClient.getContractor().getContractorCode());
        fieldsToUpdate.put("unit", skuFromClient.getUnit().getUnit());
        return updater.update(existingSku, fieldsToUpdate);
    }

    @Transactional
    @Override
    public Map<String, String> deleteSku(String id) {
        String resultMessage = "Невозможно удалить карточку товара, так как существуют зависимые записи";
        int skuId = Integer.parseInt(id);
//        if (!checkDependency(userId)) {
        Optional<Sku> optionalSku = skuRepository.findById(skuId);
        optionalSku.ifPresent(sku -> skuRepository.delete(sku));
        resultMessage = "OK";
//        }
        Map<String, String> result = new HashMap<>();
        result.put("deleteResult", resultMessage);
        return result;
    }

    private String generateSkuCode() {
        int number = skuRepository.findMaxSkuCode();
        String lastNumber = String.valueOf(number + 1);
        StringBuilder sb = new StringBuilder(lastNumber);
        for (int i = NUMBER_OF_DIGITS_IN_CODE; i > lastNumber.length(); i--) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    @Autowired
    public void setSkuRepository(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    @Autowired
    public void setContractorRepository(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    @Autowired
    public void setUnitRepository(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }
}
