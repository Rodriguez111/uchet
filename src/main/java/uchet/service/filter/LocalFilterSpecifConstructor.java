package uchet.service.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;


public class LocalFilterSpecifConstructor<T> implements SpecificationConstructor<T> {

     NumericQueryBuilder<T> numericQueryBuilder = new NumericQueryBuilder<>();

     NumericQueryBuilder<T> countNumericQueryBuilder = new CountNumericQueryBuilder<>();

     final StringQueryBuilder<T> stringQueryBuilder = new StringQueryBuilder<>();

     final BooleanQueryBuilder<T> booleanQueryBuilder = new BooleanQueryBuilder<>();

     final DateQueryBuilder<T> dateQueryBuilder = new DateQueryBuilder<>();





    @Override
    public List<Specification<T>> constructSpecifications(Map<String, String> filterParams) {
        filterParams = clearEmpty(filterParams);
        Map<String, BiFunction<String, String, Specification<T>>> numericConditions = numericQueryBuilder.getConditionsForNumericFields();
        Map<String, BiFunction<String, String, Specification<T>>> dateConditions = dateQueryBuilder.getConditionsForDateFields();
        Map<String, BiFunction<String, String, Specification<T>>> countNumericConditions = countNumericQueryBuilder.getConditionsForNumericFields();
        List<Specification<T>> specificationList = new ArrayList<>();
        for (Map.Entry<String, String> eachEntry : filterParams.entrySet()) {
            if (isFieldNumeric(eachEntry.getKey())) {
                String fieldName = removeNumericPrefix(eachEntry.getKey());
                specificationList.add(selectSpecification(numericConditions, fieldName, eachEntry.getValue()));
            } else if (isFieldBoolean(eachEntry.getKey())) {
                String fieldName = removeBooleanPrefix(eachEntry.getKey());
                specificationList.add(booleanQueryBuilder.booleanField(fieldName, eachEntry.getValue()));
            } else if (isFieldDate(eachEntry.getKey())) {
                String fieldName = removeDatePrefix(eachEntry.getKey());
                specificationList.add(selectSpecification(dateConditions, fieldName, eachEntry.getValue()));
            } else if (isFieldCountNumeric(eachEntry.getKey())) {
                String fieldName = removeCountNumericPrefix(eachEntry.getKey());
                specificationList.add(selectSpecification(countNumericConditions, fieldName, eachEntry.getValue()));
            } else {
                specificationList.add(stringQueryBuilder.stringLike(eachEntry.getKey(), eachEntry.getValue()));
            }
        }
        return specificationList;
    }



    private Specification<T> selectSpecification(
            Map<String, BiFunction<String, String, Specification<T>>> conditions,
            String fieldName, String partOfTheRequest) {
        partOfTheRequest = partOfTheRequest.trim();
        Specification<T> specification = null;
        for (Map.Entry<String, BiFunction<String, String, Specification<T>>> eachEntry : conditions.entrySet()) {
            if (partOfTheRequest.matches(eachEntry.getKey())) {
                specification = eachEntry.getValue().apply(fieldName, partOfTheRequest);
                break;
            }
        }
        return specification;
    }


     Map<String, String> clearEmpty(Map<String, String> filterParams) {
        filterParams.entrySet().removeIf(e -> e.getValue().isBlank());
        return filterParams;
    }



     boolean isFieldNumeric(String fieldName) {
        return fieldName.startsWith("_numeric_");
    }

     String removeNumericPrefix(String fieldName) {
        return fieldName.replaceAll("^_numeric_", "");
    }

     boolean isFieldCountNumeric(String fieldName) {
        return fieldName.startsWith("_count_");
    }

     String removeCountNumericPrefix(String fieldName) {
        return fieldName.replaceAll("^_count_", "");
    }

     boolean isFieldBoolean(String fieldName) {
        return fieldName.startsWith("_boolean_");
    }

     String removeBooleanPrefix(String fieldName) {
        return fieldName.replaceAll("^_boolean_", "");
    }

     boolean isFieldDate(String fieldName) {
        return fieldName.startsWith("_date_");
    }

     String removeDatePrefix(String fieldName) {
        return fieldName.replaceAll("^_date_", "");
    }


}
