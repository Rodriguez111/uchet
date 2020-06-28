package uchet.service.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationConstructor<T> {

    List<Specification<T>> constructSpecifications(Map<String, String> filterParams);
}
