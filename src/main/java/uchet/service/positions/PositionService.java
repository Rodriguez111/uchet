package uchet.service.positions;

import uchet.models.Position;

import java.util.List;
import java.util.Map;

public interface PositionService {
    List<Position> getAll();

    Map<String, String> addPosition(Position newPosition);

    Map<String, String> updatePosition(Position position);

    Map<String, String> deletePosition(String id);

    List<Position> findByFilter(Map<String, String> filterParams);
}
