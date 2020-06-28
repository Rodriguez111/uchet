package uchet.service.filter;

import org.springframework.data.jpa.domain.Specification;
import uchet.models.InDocDetail;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public class CountNumericQueryBuilder<T> extends NumericQueryBuilder<T>{

    public Specification<T> integerEqual(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.equal(criteriaBuilder.size(path), value);
            }
        };
    }



    public Specification<T> integerNotEqual(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.notEqual(criteriaBuilder.size(path), value);
            }
        };
    }

    public Specification<T> integerGreaterThanOrEqualTo(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.size(path), value);
            }
        };
    }

    public Specification<T> integerLessThanOrEqualTo(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.size(path), value);
            }
        };
    }

    public Specification<T> integerGreaterThan(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.greaterThan(criteriaBuilder.size(path), value);
            }
        };
    }

    public Specification<T> integerLessThan(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.lessThan(criteriaBuilder.size(path), value);
            }
        };
    }

    public Specification<T> integerBetween(String fieldName, String partOfTheRequest) {
        int[] values = extractTwoIntegerValues(partOfTheRequest);
        int min = values[0];
        int max = values[1];

        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.between(criteriaBuilder.size(path), min, max);
            }
        };
    }

    public Specification<T> integerGreaterOrEqualToAndLessThen(String fieldName, String partOfTheRequest) {
        int[] values = extractTwoIntegerValues(partOfTheRequest);
        int min = values[0];
        int max = values[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);

                Predicate predicateForGreaterThanOrEqualTo
                        = criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.size(path), min);
                Predicate lessThan
                        = criteriaBuilder.lessThan(criteriaBuilder.size(path), max);
                return criteriaBuilder.and(predicateForGreaterThanOrEqualTo, lessThan);
            }
        };
    }

    public Specification<T> integerGreaterThanAndLessThenOrEqualTo(String fieldName, String partOfTheRequest) {
        int[] values = extractTwoIntegerValues(partOfTheRequest);
        int min = values[0];
        int max = values[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                Predicate predicateForGreaterThan
                        = criteriaBuilder.greaterThan(criteriaBuilder.size(path), min);
                Predicate lessThanOrEqualTo
                        = criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.size(path), max);
                return criteriaBuilder.and(predicateForGreaterThan, lessThanOrEqualTo);
            }
        };
    }

    public Specification<T> integerGreaterThanAndLessThen(String fieldName, String partOfTheRequest) {
        int[] values = extractTwoIntegerValues(partOfTheRequest);
        int min = values[0];
        int max = values[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                Predicate predicateForGreaterThan
                        = criteriaBuilder.greaterThan(criteriaBuilder.size(path), min);
                Predicate lessThan
                        = criteriaBuilder.lessThan(criteriaBuilder.size(path), max);
                return criteriaBuilder.and(predicateForGreaterThan, lessThan);
            }
        };
    }






    public Map<String, BiFunction<String, String, Specification<T>>> getConditionsForNumericFields() {
        return conditionsForNumericFields;
    }







}
