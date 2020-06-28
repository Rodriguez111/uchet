package uchet.service.filter;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class GlobalFilterSpecifConstructor<T> extends LocalFilterSpecifConstructor<T> {


    @Override
    public List<Specification<T>> constructSpecifications(Map<String, String> filterParams) {
        filterParams = clearEmpty(filterParams);

        List<Specification<T>> specificationList = new ArrayList<>();
        for (Map.Entry<String, String> eachEntry : filterParams.entrySet()) {
            if (isFieldNumeric(eachEntry.getKey())) {
                String fieldName = removeNumericPrefix(eachEntry.getKey());
                int[] values = splitFieldValueByORSymbol(eachEntry.getValue());
                specificationList.add(numericQueryBuilder.integerEqualFewValuesForGlobalFilter(fieldName, values));
            } else if (isFieldBoolean(eachEntry.getKey())) {
                String fieldName = removeBooleanPrefix(eachEntry.getKey());
                specificationList.add(booleanQueryBuilder.booleanField(fieldName, eachEntry.getValue()));
            } else if (isFieldDate(eachEntry.getKey())) {
                String fieldName = removeDatePrefix(eachEntry.getKey());
                specificationList.add(dateQueryBuilder.dateBetween(fieldName, eachEntry.getValue()));
            } else if (isFieldCountNumeric(eachEntry.getKey())) {
//                String fieldName = removeCountNumericPrefix(eachEntry.getKey());
//                specificationList.add(selectSpecification(countNumericConditions, fieldName, eachEntry.getValue()));
            } else {
                specificationList.add(stringQueryBuilder.stringEquals(eachEntry.getKey(), eachEntry.getValue()));
            }
        }
        return specificationList;
    }

    private int[] splitFieldValueByORSymbol(String fieldValue) {
        String[] values = fieldValue.split("\\|\\|");
        return Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
    }


}
