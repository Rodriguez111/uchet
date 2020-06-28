package uchet.service.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;


import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public class DateQueryBuilder<T> {

    private static final String DATE_TIME_FORMAT = "^[0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2}$";

    private static final String DATE_EQUAL = "^=[0-9]{4}-[0-9]{2}-[0-9]{2}$|^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_NOT_EQUAL = "^!=[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_GREATER_THAN = "^>[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_GREATER_OR_EQUAL_THAN = "^>=[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_LESS_THAN = "^<[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_LESS_OR_EQUAL_THAN = "^<=[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_BETWEEN = "^>=[0-9]{4}-[0-9]{2}-[0-9]{2}&<=[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_GREATER_OR_EQUAL_AND_LESS_THAN = "^>=[0-9]{4}-[0-9]{2}-[0-9]{2}&<[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_GREATER_AND_LESS_THAN_OR_EQUAL = "^>[0-9]{4}-[0-9]{2}-[0-9]{2}&<=[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String DATE_GREATER_THAN_AND_LESS_THAN = "^>[0-9]{4}-[0-9]{2}-[0-9]{2}&<[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    private static final String EXTRA_CHARACTERS = ">|<|=|!";


    private final Map<String, BiFunction<String, String, Specification<T>>> conditionsForDateFields = new HashMap<>();

    public DateQueryBuilder() {
        initConditionsForDateFields();
    }

    private void initConditionsForDateFields() {
        conditionsForDateFields.put(DATE_EQUAL, this::dateEqual);
        conditionsForDateFields.put(DATE_NOT_EQUAL, this::dateNotEqual);
        conditionsForDateFields.put(DATE_GREATER_THAN, this::dateGreaterThan);
        conditionsForDateFields.put(DATE_GREATER_OR_EQUAL_THAN, this::dateGreaterThanOrEqualTo);
        conditionsForDateFields.put(DATE_LESS_THAN, this::dateLessThan);
        conditionsForDateFields.put(DATE_LESS_OR_EQUAL_THAN, this::dateLessThanOrEqualTo);
        conditionsForDateFields.put(DATE_BETWEEN, this::dateBetween);
        conditionsForDateFields.put(DATE_GREATER_OR_EQUAL_AND_LESS_THAN, this::dateGreaterOrEqualToAndLessThan);
        conditionsForDateFields.put(DATE_GREATER_AND_LESS_THAN_OR_EQUAL, this::dateGreaterAndLessThanOrEqualTo);
        conditionsForDateFields.put(DATE_GREATER_THAN_AND_LESS_THAN, this::dateGreaterThanAndLessThan);
    }

//    public Specification<T> dateBetweenForGlobalFilter(String fieldName, String[] partOfTheRequest) {
//        return new Specification<T>() {
//            @Override
//            public Predicate toPredicate(Root<T> root,
//                                         CriteriaQuery<?> criteriaQuery,
//                                         CriteriaBuilder criteriaBuilder) {
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date date1Begin = null;
//                Date date2End = null;
//                try {
//                    date1Begin = dateFormat.parse(partOfTheRequest[0]);
//                    date2End = dateFormat.parse(partOfTheRequest[1]);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Predicate from = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date1Begin);
//                Predicate to
//                        = criteriaBuilder.lessThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date2End);
//                return criteriaBuilder.and(from, to);
//            }
//        };
//    }

    public Specification<T> dateEqual(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dBegin = twentyFourHours[0];
        Date dEnd = twentyFourHours[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                String timeValue = extractSingleDateValue(partOfTheRequest);
                return criteriaBuilder.between(root.<Date>get(fieldName).as(Date.class), dBegin, dEnd);
            }
        };
    }

    public Specification<T> dateNotEqual(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dBegin = twentyFourHours[0];
        Date dEnd = twentyFourHours[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate lessThan
                        = criteriaBuilder.lessThan(root.<Date>get(fieldName).as(Date.class), dBegin);
                Predicate greaterThan
                        = criteriaBuilder.greaterThan(root.<Date>get(fieldName).as(Date.class), dEnd);
                return criteriaBuilder.or(lessThan, greaterThan);
            }
        };
    }

    public Specification<T> dateGreaterThan(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dEnd = twentyFourHours[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate greaterThan
                        = criteriaBuilder.greaterThan(root.<Date>get(fieldName).as(Date.class), dEnd);
                return criteriaBuilder.or(greaterThan);
            }
        };
    }

    public Specification<T> dateGreaterThanOrEqualTo(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dBegin = twentyFourHours[0];
        Date dEnd = twentyFourHours[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {

                Predicate equals = criteriaBuilder.between(root.<Date>get(fieldName).as(Date.class), dBegin, dEnd);
                Predicate greaterThan
                        = criteriaBuilder.greaterThan(root.<Date>get(fieldName).as(Date.class), dEnd);
                return criteriaBuilder.or(equals, greaterThan);
            }
        };
    }


    public Specification<T> dateLessThan(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dBegin = twentyFourHours[0];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate lessThan
                        = criteriaBuilder.lessThan(root.<Date>get(fieldName).as(Date.class), dBegin);
                return criteriaBuilder.or(lessThan);
            }
        };
    }

    public Specification<T> dateLessThanOrEqualTo(String fieldName, String partOfTheRequest) {
        Date[] twentyFourHours = getTwentyFourHours(partOfTheRequest);
        Date dBegin = twentyFourHours[0];
        Date dEnd = twentyFourHours[1];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {

                Predicate equals = criteriaBuilder.between(root.<Date>get(fieldName).as(Date.class), dBegin, dEnd);
                Predicate lessThan
                        = criteriaBuilder.lessThan(root.<Date>get(fieldName).as(Date.class), dBegin);
                return criteriaBuilder.or(lessThan, equals);
            }
        };
    }

    public Specification<T> dateBetween(String fieldName, String partOfTheRequest) {
        Date[] dates = getTwoTwentyFourHours(partOfTheRequest);
        Date date1Begin = dates[0];
        Date date2End = dates[3];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate from = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date1Begin);
                Predicate to
                        = criteriaBuilder.lessThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date2End);
                return criteriaBuilder.and(from, to);
            }
        };
    }

    public Specification<T> dateGreaterOrEqualToAndLessThan(String fieldName, String partOfTheRequest) {
        Date[] dates = getTwoTwentyFourHours(partOfTheRequest);
        Date date1Begin = dates[0];
        Date date2Begin = dates[2];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate from = criteriaBuilder.greaterThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date1Begin);
                Predicate to
                        = criteriaBuilder.lessThan(root.<Date>get(fieldName).as(Date.class), date2Begin);
                return criteriaBuilder.and(from, to);
            }
        };
    }

    public Specification<T> dateGreaterAndLessThanOrEqualTo(String fieldName, String partOfTheRequest) {
        Date[] dates = getTwoTwentyFourHours(partOfTheRequest);
        Date date1End = dates[1];
        Date date2End = dates[3];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate from = criteriaBuilder.greaterThan(root.<Date>get(fieldName).as(Date.class), date1End);
                Predicate to
                        = criteriaBuilder.lessThanOrEqualTo(root.<Date>get(fieldName).as(Date.class), date2End);
                return criteriaBuilder.and(from, to);
            }
        };
    }


    public Specification<T> dateGreaterThanAndLessThan(String fieldName, String partOfTheRequest) {
        Date[] dates = getTwoTwentyFourHours(partOfTheRequest);
        Date date1End = dates[1];
        Date date2Begin = dates[2];
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Predicate from = criteriaBuilder.greaterThan(root.<Date>get(fieldName).as(Date.class), date1End);
                Predicate to
                        = criteriaBuilder.lessThan(root.<Date>get(fieldName).as(Date.class), date2Begin);
                return criteriaBuilder.and(from, to);
            }
        };
    }

    private String extractSingleDateValue(String partOfTheRequest) {
        partOfTheRequest = partOfTheRequest.replaceAll(EXTRA_CHARACTERS, "");
        return partOfTheRequest;
    }

    private Date[] getTwentyFourHours(String partOfTheRequest) {
        Date[] result = new Date[2];
        String timeValue = extractSingleDateValue(partOfTheRequest);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            result[0] = dateFormat.parse(timeValue + " 00:00:00");
            result[1] = dateFormat.parse(timeValue + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String[] extractTwoDateValues(String partOfTheRequest) {
        partOfTheRequest = partOfTheRequest.replaceAll("[^0-9-]", "");
        String[] values = new String[2];
        values[0] = partOfTheRequest.substring(0, 10);
        values[1] = partOfTheRequest.substring(10, 20);
        return values;
    }

    private Date[] getTwoTwentyFourHours(String partOfTheRequest) {
        Date[] result = new Date[4];
        String[] timeValues = extractTwoDateValues(partOfTheRequest);
        String date1 = timeValues[0];
        String date2 = timeValues[1];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            result[0] = dateFormat.parse(date1 + " 00:00:00");
            result[1] = dateFormat.parse(date1 + " 23:59:59");
            result[2] = dateFormat.parse(date2 + " 00:00:00");
            result[3] = dateFormat.parse(date2 + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Map<String, BiFunction<String, String, Specification<T>>> getConditionsForDateFields() {
        return conditionsForDateFields;
    }
}
