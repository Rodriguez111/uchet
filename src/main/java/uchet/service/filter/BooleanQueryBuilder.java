package uchet.service.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;




public class BooleanQueryBuilder<T> extends AbstractQueryBuilder<T>{
    public Specification<T> booleanField(String fieldName, String partOfTheRequest) {
        boolean value = partOfTheRequest.toLowerCase().equals("да");
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.equal(path, value);
            }
        };
    }


}
