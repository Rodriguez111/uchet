package uchet.service.indoctypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uchet.models.InDocType;
import uchet.repository.InDocTypeRepository;
import java.util.List;


@Service
public class InDocTypeServiceImpl implements InDocTypeService {

    private InDocTypeRepository inDocTypeRepository;

    @Override
    public List<InDocType> getAll() {
        return inDocTypeRepository.getAll();
    }

    @Autowired
    public void setInDocTypeRepository(InDocTypeRepository inDocTypeRepository) {
        this.inDocTypeRepository = inDocTypeRepository;
    }
}
