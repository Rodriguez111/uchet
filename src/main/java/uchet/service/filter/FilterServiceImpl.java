package uchet.service.filter;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FilterServiceImpl<E> implements FilterService<E> {

    private Function<Specification<E>, List<E>> function;

    private SpecificationConstructor<E> specificationConstructor;

    public FilterServiceImpl(Function<Specification<E>, List<E>> function, SpecificationConstructor<E> specificationConstructor) {
        this.function = function;
        this.specificationConstructor = specificationConstructor;
    }

    @Override
    public List<E> findEntriesByFilter(Map<String, String> filterParams) {
        List<Specification<E>> specificationList = specificationConstructor.constructSpecifications(filterParams);
        Specification<E> specification = null;
        if (!specificationList.isEmpty()) {
            specification =  specificationList.get(0);
        }
        for (int i = 1; i < specificationList.size(); i++) {
            specification = specification.and(specificationList.get(i));
        }
        List<E> filteredRegs = function.apply(specification);
        return filteredRegs;
    }
}