package uchet.service.daoutils;

import uchet.models.CloneableEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FieldsUpdater<E extends CloneableEntity> implements EntityFieldsUpdater<E>, Cloneable {

    private final Map<String, BiConsumer<E,String>> fieldsUpdateDispatcher;

    public FieldsUpdater(Map<String, BiConsumer<E, String>> fieldsUpdateDispatcher) {
        this.fieldsUpdateDispatcher = fieldsUpdateDispatcher;
    }

    @Override
    public E update(E e, Map<String, String> params) {
        params.remove("id");
        E updatedEntity = (E) e.cloneEntity();
        for (Map.Entry<String, String> each : params.entrySet()) {
            if (validateField(each.getValue())) {
                fieldsUpdateDispatcher.get(each.getKey()).accept(updatedEntity, each.getValue());
            }
        }
        return updatedEntity;
    }


    public Map<String, String> getMapOfFields(E entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        Map<String, String> mapOfFields = new HashMap<>();
        for(Field f : fields) {
            f.setAccessible(true);
            try {
                mapOfFields.put(f.getName(),String.valueOf(f.get(entity)) );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
           return  mapOfFields;
    }


    private boolean validateField(String value) {
        return value != null && !value.equals("");
    }
}
