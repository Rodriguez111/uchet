package uchet.service.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public class StringQueryBuilder<T> extends AbstractQueryBuilder<T>{



    public Specification<T> stringLike(String fieldName, String partOfTheRequest) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate[] predicates;
                if (fieldName.contains("AND")) {
                    predicates =  getCriteriaForCompoundStringField(fieldName, criteriaBuilder, partOfTheRequest, root);
                } else {
                    predicates = new Predicate[1];
                    Path path = getPath(fieldName, root);
                    Predicate newPredicate = criteriaBuilder.like(path, partOfTheRequest);
                    predicates[0] = newPredicate;
                }

                return criteriaBuilder.or(predicates);
            }
        };
    }

    public Specification<T> stringEquals(String fieldName, String partOfTheRequest) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                    Path path = getPath(fieldName, root);
                return criteriaBuilder.equal(path, partOfTheRequest);
            }
        };
    }



    private Predicate[] getCriteriaForCompoundStringField(String fieldName, CriteriaBuilder criteriaBuilder, String partOfTheRequest, Root<T> root) {
        String[] names = StringUtils.split(fieldName, "AND");
        Predicate[] predicates = new Predicate[names.length];
        for (int i = 0; i < names.length; i++) {
            Path path = getPath(names[i], root);
            Predicate newPredicate = criteriaBuilder.like(path, partOfTheRequest);
            predicates[i] = newPredicate;
        }
        return predicates;

    }

}
