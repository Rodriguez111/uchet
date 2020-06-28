package uchet.service.indocs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uchet.models.*;
import uchet.repository.*;
import uchet.service.filter.*;
import uchet.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class InDocServiceImpl implements InDocService {

    private static final int NUMBER_OF_DIGITS_IN_DOC_NUMBER = 10;

    @Value("#{new Double('${value-added-tax}')}")
    private double vatValue;

    private InDocRepository inDocRepository;

    private InDocDetailRepository inDocDetailRepository;

    private SkuRepository skuRepository;

    private ContractorRepository contractorRepository;

    private InDocTypeRepository inDocTypeRepository;

    private UserRepository userRepository;


    @Override
    public List<InDoc> getAllInLastMonth() {
        List<InDoc> documents = inDocRepository.getAllInLastMonth();
        return removeBackReferences(documents);
    }

    @Override
    public List<InDoc> findByFilter(Map<String, String> filterParams, SpecificationConstructor<InDoc> constructor) {
        Function<Specification<InDoc>, List<InDoc>> function = spc -> inDocRepository.findAll(spc);
        FilterService<InDoc> filterService = new FilterServiceImpl<>(function, constructor);
        return removeBackReferences(filterService.findEntriesByFilter(filterParams));
    }


    @Override
    public InDoc getByInDocNumber(String inDocNumber) {
        InDoc inDoc = inDocRepository.findByDocNumber(inDocNumber);
        List<InDoc> documents = List.of(inDoc);
        return removeBackReferences(documents).get(0);
    }

    @Transactional
    @Override
    public Map<String, String> addInDoc(InDoc inDoc) {
        String resultMessage = "OK";
        String docNumber = generateDocNumber();
        String docDate = Util.generateCurrentTimeStamp();
        InDocType type = inDocTypeRepository.findByDocType(inDoc.getType().getDocType());
        Contractor contractor = contractorRepository.findByName(inDoc.getContractor().getName());
        User owner = userRepository.findByLogin(inDoc.getOwner().getLogin());
        InDoc newInDoc = new InDoc(docNumber, contractor, owner, type);
        newInDoc.setDocDate(docDate);
        inDocRepository.save(newInDoc);
        InDoc savedInDoc = inDocRepository.findByDocNumber(docNumber);
        if (savedInDoc != null) {
            List<InDocDetail> details = getDetailsList(inDoc);
            addDetails(details, savedInDoc);
        } else {
            resultMessage = "Возникла непредвиденная ошибка сохранения";
        }
        Map<String, String> result = new HashMap<>();
        result.put("addResult", resultMessage);
        return result;
    }

    @Transactional
    @Override
    public Map<String, String> updateInDoc(InDoc inDoc) {
        return null;
    }

    @Transactional
    @Override
    public Map<String, String> deleteInDoc(String id) {
        return null;
    }


    private List<InDocDetail> getDetailsList(InDoc inDoc) {
        List<InDocDetail> details = new ArrayList<>();
        List<InDocDetail> detailsFromClient = inDoc.getDetails();
        for (InDocDetail eachDetail: detailsFromClient) {
            InDocDetail inDocDetail = new InDocDetail();
            Sku sku = skuRepository.findByCode(eachDetail.getSku().getCode());
            inDocDetail.setSku(sku);
            inDocDetail.setQty(eachDetail.getQty());
            inDocDetail.setPrice(eachDetail.getPrice());
            inDocDetail.setVatPrice(eachDetail.getPrice() * this.vatValue);
            inDocDetail.setSerial(eachDetail.getSerial());
            if (!eachDetail.getExpireDate().equals("")) {
                inDocDetail.setExpireDate(Util.formatExpireDate(eachDetail.getExpireDate()));
            }
            details.add(inDocDetail);
        }
    return details;
    }

    private void addDetails(List<InDocDetail> details, InDoc inDoc) {
        for (InDocDetail eachDetail: details) {
            eachDetail.setInDoc(inDoc);
            inDocDetailRepository.save(eachDetail);
        }
    }


    private String generateDocNumber() {
       int number = inDocRepository.findMaxDocNumber();
       String lastNumber = String.valueOf(number + 1);
        StringBuilder sb = new StringBuilder(lastNumber);
        for (int i = NUMBER_OF_DIGITS_IN_DOC_NUMBER; i > lastNumber.length(); i--) {
            sb.insert(0, "0");
        }
       return sb.toString();
    }

    private List<InDoc> removeBackReferences(List<InDoc> documents) {
        documents.stream().forEach(each -> {
           List<InDocDetail> details = each.getDetails();
            details.stream().forEach(eachDetail -> eachDetail.setInDoc(null));
        });
        return documents;
    }

    @Autowired
    public void setInDocRepository(InDocRepository inDocRepository) {
        this.inDocRepository = inDocRepository;
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
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setInDocTypeRepository(InDocTypeRepository inDocTypeRepository) {
        this.inDocTypeRepository = inDocTypeRepository;
    }

    @Autowired
    public void setInDocDetailRepository(InDocDetailRepository inDocDetailRepository) {
        this.inDocDetailRepository = inDocDetailRepository;
    }
}
