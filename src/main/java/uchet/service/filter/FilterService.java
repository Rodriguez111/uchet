package uchet.service.filter;

import uchet.models.User;

import java.util.List;
import java.util.Map;

public interface FilterService<E> {

    List<E> findEntriesByFilter(Map<String, String> filterParams);
}
