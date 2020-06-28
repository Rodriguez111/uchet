package uchet.service.daoutils;

import uchet.models.CloneableEntity;

import java.util.Map;

public interface EntityFieldsUpdater<E extends CloneableEntity> {

    E update(E e, Map<String, String> params);

    Map<String, String> getMapOfFields(E entity);
}
