package uchet.service.contractors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uchet.models.Contractor;
import uchet.models.User;
import uchet.repository.PositionRepository;
import uchet.repository.ContractorRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class ContractorServiceImpl implements ContractorService {

    private ContractorRepository contractorRepository;

    private PositionRepository positionRepository;

    private PasswordEncoder passwordEncoder;

    private final Map<String, BiConsumer<User,String>> fieldsUpdateDispatcher = new HashMap<>();



    @Override
    public List<Contractor> getAll() {
        return (List<Contractor>) contractorRepository.findAll();
    }

    @Override
    public List<Contractor> getSuppliers() {
        return contractorRepository.findAllByIsSupplierOrderByName(true);
    }

    @Override
    public List<Contractor> getCustomers() {
        return contractorRepository.findAllByIsCustomerOrderByName(true);
    }

    @Transactional
    @Override
    public Map<String, String> addContractor(Map<String, String> user) {
        return null;
    }

    @Transactional
    @Override
    public Map<String, String> updateContractor(String id, Map<String, String> user) {
        return null;
    }

    @Transactional
    @Override
    public Map<String, String> deleteContractor(String id) {
        return null;
    }

    private User setCurrentTimeStampForCreateDate(User user) {
        String currentTimeStamp = generateCurrentTimeStamp();
        user.setCreateDate(currentTimeStamp);
        return user;
    }

    private User setCurrentTimeStampForUpdateDate(User user) {
        String currentTimeStamp = generateCurrentTimeStamp();
        user.setLastUpdateDate(currentTimeStamp);
        return user;
    }

    private String generateCurrentTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setContractorRepository(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }
}
