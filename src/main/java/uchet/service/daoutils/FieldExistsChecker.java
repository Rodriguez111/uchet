package uchet.service.daoutils;

import uchet.models.CloneableEntity;

import java.util.Map;
import java.util.function.Function;

public interface FieldExistsChecker<E> {

    boolean fieldExists(E e);
}
