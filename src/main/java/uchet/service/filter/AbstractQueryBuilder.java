package uchet.service.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import uchet.models.User;

import javax.persistence.criteria.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;


public abstract class AbstractQueryBuilder<T> {

    Path getPath(String fieldName, Root<T> root) {
        Path expression = null;
        if(fieldName.contains("$")){
            String[] names = StringUtils.split(fieldName, "$");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        }else{
            expression = root.get(fieldName);
        }
        return expression;
    }


}
