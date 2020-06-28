package uchet.service.filter;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;



public class NumericQueryBuilder<T> extends AbstractQueryBuilder<T>{

    final Map<String, BiFunction<String, String, Specification<T>>> conditionsForNumericFields = new HashMap<>();


    NumericQueryBuilder() {
        initConditionsForNumericFields();
    }

    private void  initConditionsForNumericFields() {
        conditionsForNumericFields.put("^=\\d+|\\d+", this::integerEqual);
        conditionsForNumericFields.put("^!=\\d+", this::integerNotEqual);
        conditionsForNumericFields.put("^>\\d+", this::integerGreaterThan);
        conditionsForNumericFields.put("^>=\\d+", this::integerGreaterThanOrEqualTo);
        conditionsForNumericFields.put("^<\\d+", this::integerLessThan);
        conditionsForNumericFields.put("^<=\\d+", this::integerLessThanOrEqualTo);
        conditionsForNumericFields.put("^>=\\d+&<=\\d+$",this::integerBetween);
        conditionsForNumericFields.put("^>=\\d+&<\\d+$",this::integerGreaterOrEqualToAndLessThen);
        conditionsForNumericFields.put("^>\\d+&<=\\d+$",this::integerGreaterThanAndLessThenOrEqualTo);
        conditionsForNumericFields.put("^>\\d+&<\\d+$",this::integerGreaterThanAndLessThen);
    }


    public Specification<T> integerEqualFewValuesForGlobalFilter(String fieldName, int[] values) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                Predicate orAggregate = null;
                for (int eachValue : values) {
                    orAggregate =  criteriaBuilder.equal(path, eachValue);
                }
                Predicate result = criteriaBuilder.or(orAggregate);
                return result;
            }
        };
    }



    public Specification<T> integerEqual(String fieldName, String partOfTheRequest) {
       int value = extractSingleIntegerValue(partOfTheRequest);
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

    public Specification<T> integerNotEqual(String fieldName, String partOfTheRequest) {
        int value = extractSingleIntegerValue(partOfTheRequest);
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path path = getPath(fieldName, root);
                return criteriaBuilder.notEqual(path, value);
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
                return criteriaBuilder.greaterThanOrEqualTo(path, value);
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
                return criteriaBuilder.lessThanOrEqualTo(path, value);
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
                return criteriaBuilder.greaterThan(path, value);
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
                return criteriaBuilder.lessThan(path, value);
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
                return criteriaBuilder.between(path, min, max);
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
                        = criteriaBuilder.greaterThanOrEqualTo(path, min);
                Predicate lessThan
                        = criteriaBuilder.lessThan(path, max);
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
                        = criteriaBuilder.greaterThan(path, min);
                Predicate lessThanOrEqualTo
                        = criteriaBuilder.lessThanOrEqualTo(path, max);
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
                        = criteriaBuilder.greaterThan(path, min);
                Predicate lessThan
                        = criteriaBuilder.lessThan(path, max);
                return criteriaBuilder.and(predicateForGreaterThan, lessThan);
            }
        };
    }



    int extractSingleIntegerValue(String partOfTheRequest) {
        partOfTheRequest =  partOfTheRequest.replaceAll("[^0-9]", "");
        return Integer.parseInt(partOfTheRequest);
    }

    int[] extractTwoIntegerValues(String partOfTheRequest) {
        String[] str = partOfTheRequest.split("\\D+");
        int[] values = {Integer.parseInt(str[1]), Integer.parseInt(str[2])};
        return values;
    }




    public Map<String, BiFunction<String, String, Specification<T>>> getConditionsForNumericFields() {
        return conditionsForNumericFields;
    }







}
