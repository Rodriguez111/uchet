package uchet.service.daoutils;

import org.springframework.data.repository.CrudRepository;
import uchet.models.Sku;

import java.util.function.Function;

public class FieldExistsCheckerImpl<E> implements FieldExistsChecker<E> {


    @Override
    public boolean fieldExists(E e) {
        boolean result = false;
        if (e != null) {
            result = true;
        }
        return result;
    }
}
