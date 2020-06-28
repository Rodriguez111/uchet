package uchet.service.indocdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uchet.models.InDocDetail;
import uchet.repository.InDocDetailRepository;
import uchet.service.filter.FilterService;
import uchet.service.filter.FilterServiceImpl;
import uchet.service.filter.LocalFilterSpecifConstructor;
import uchet.service.filter.SpecificationConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class InDocDetailServiceImpl implements InDocDetailsService {

    private InDocDetailRepository detailRepository;

    @Override
    public List<InDocDetail> getAllInLastMonth() {
        List<InDocDetail> details = detailRepository.getAllInLastMonth();
        return removeBackReferences(details);
    }

    @Override
    public List<InDocDetail> findByFilter(Map<String, String> filterParams) {
        Function<Specification<InDocDetail>, List<InDocDetail>> function = spc -> detailRepository.findAll(spc);
        SpecificationConstructor<InDocDetail> constructor = new LocalFilterSpecifConstructor<>();
        FilterService<InDocDetail> filterService = new FilterServiceImpl<>(function, constructor);
        return removeBackReferences(filterService.findEntriesByFilter(filterParams));
    }

    @Override
    public Map<String, String> addInDocDetail(InDocDetail detail) {
        return null;
    }

    @Override
    public Map<String, String> updateInDocDetail(InDocDetail detail) {
        return null;
    }

    @Override
    public Map<String, String> deleteInDocDetail(String id) {
        return null;
    }



    private List<InDocDetail> removeBackReferences(List<InDocDetail> details) {
        details.stream().forEach(each -> each.getInDoc().setDetails(null));
                return details;
    }

    @Autowired
    public void setDetailRepository(InDocDetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }
}
