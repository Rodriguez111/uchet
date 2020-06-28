package uchet.service.positions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uchet.models.*;
import uchet.repository.PermissionRepository;
import uchet.repository.PositionRepository;
import uchet.repository.UserRepository;
import uchet.service.daoutils.*;
import uchet.service.daoutils.EntityFieldsUpdater;
import uchet.service.daoutils.FieldsUpdater;
import uchet.service.filter.FilterService;
import uchet.service.filter.FilterServiceImpl;
import uchet.service.filter.LocalFilterSpecifConstructor;
import uchet.service.filter.SpecificationConstructor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class PositionServiceImpl implements PositionService {

    private PositionRepository positionRepository;

    private PermissionRepository permissionRepository;

    private UserRepository userRepository;

    private FieldExistsChecker<Position> checker = new FieldExistsCheckerImpl<>();

    private FilterService<Position> filterService;

    private Map<String, BiConsumer<Position,String>> getFieldsUpdateDispatcher() {
        Map<String, BiConsumer<Position,String>> fieldsUpdateDispatcher = new HashMap<>();
        fieldsUpdateDispatcher.put("position", Position::setPosition);
        fieldsUpdateDispatcher.put("positionDescription", Position::setPositionDescription);
        return fieldsUpdateDispatcher;
    }




    @Override
    public List<Position> findByFilter(Map<String, String> filterParams) {
        Function<Specification<Position>, List<Position>> function = spc -> positionRepository.findAll(spc);
        SpecificationConstructor<Position> constructor = new LocalFilterSpecifConstructor<>();
        this.filterService = new FilterServiceImpl<>(function, constructor);
        return this.filterService.findEntriesByFilter(filterParams);
    }


    @Override
    public List<Position> getAll() {
        return positionRepository.getAll();
    }


    @Transactional
    @Override
    public Map<String, String> addPosition(Position newPosition) {
        String resultMessage = "OK";
        String position = newPosition.getPosition();
        String description = newPosition.getPositionDescription();
        boolean positionExists = checker.fieldExists(positionRepository.findByPosition(position));
        boolean descriptionExists = checker.fieldExists(positionRepository.findByPositionDescription(description));
        if (positionExists) {
            resultMessage = "Должность " + position + " уже существует";
        } else if (descriptionExists) {
            resultMessage = "Такое описание уже существует у другой должности";
        } else {
            Position positionToAdd = setPermissionListToPosition(newPosition);
            positionRepository.save(positionToAdd);
        }
        Map<String, String> result = new HashMap<>();
        result.put("addResult", resultMessage);
        return result;
    }


    @Transactional
    @Override
    public Map<String, String> updatePosition(Position position) {
        String resultMessage = "OK";
        Optional<Position> optionalPosition = positionRepository.findById(position.getId());
        if (optionalPosition.isPresent()) {
            Position existingPosition = optionalPosition.get();
            Position updatedPosition = getPreparedCandidateForUpdate(existingPosition, position);
            boolean positionNameExists = checker.fieldExists(positionRepository.findByPositionForUpdate(updatedPosition.getPosition(), existingPosition.getId()));
            boolean positionDescriptionExists = checker.fieldExists(positionRepository.findByPositionDescriptionForUpdate(updatedPosition.getPositionDescription(), existingPosition.getId()));
            if (existingPosition.equals(updatedPosition)) {
                resultMessage = "Нет полей для обновления";
            } else if (positionNameExists) {
                resultMessage = "Должность " + updatedPosition.getPosition() + " уже существует";
            } else if (positionDescriptionExists) {
                resultMessage = "Такое описание уже существует у другой должности";
            } else {
                positionRepository.save(updatedPosition);
            }
        }
        Map<String, String> result = new HashMap<>();
        result.put("updateResult", resultMessage);
        return result;
    }

    public Position setPermissionListToPosition(Position position) {
        List<Permission> rawPermissions = position.getPermissions();
        List<Permission> permissionsToAdd = new ArrayList<>();
        for (Permission eachPermission: rawPermissions) {
            Permission permission = permissionRepository.findByPermissionDescription(eachPermission.getPermissionDescription());
            permissionsToAdd.add(permission);
        }
        position.setPermissions(permissionsToAdd);
        return position;
    }

    @Transactional
    @Override
    public Map<String, String> deletePosition(String id) {
        String resultMessage = "Невозможно удалить должность, пока существуют зависимые записи";
        int posId = Integer.parseInt(id);
        if (!checkDependency(posId)) {
        Optional<Position> optionalPosition = positionRepository.findById(posId);
        optionalPosition.ifPresent(position -> positionRepository.delete(position));
        resultMessage = "OK";
        }
        Map<String, String> result = new HashMap<>();
        result.put("deleteResult", resultMessage);
        return result;
    }

    private Position getPreparedCandidateForUpdate(Position existingPosition, Position positionFromClient) {
        EntityFieldsUpdater<Position> updater = new FieldsUpdater<>(getFieldsUpdateDispatcher());
        Map<String, String> fieldsToUpdate = updater.getMapOfFields(positionFromClient);
        fieldsToUpdate.remove("permissions");
        Position position = updater.update(existingPosition, fieldsToUpdate);
        position = setPermissionListToPosition(positionFromClient);
        return position;
    }


    private boolean checkDependency(int id){
        FieldExistsChecker<User> checker = new FieldExistsCheckerImpl<>();
        return checker.fieldExists(userRepository.findTop1ByPositionId(id));
    }

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Autowired
    public PositionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
