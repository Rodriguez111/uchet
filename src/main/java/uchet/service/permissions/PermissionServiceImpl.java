package uchet.service.permissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uchet.models.Permission;
import uchet.models.Position;
import uchet.repository.PermissionRepository;
import uchet.repository.PositionRepository;
import uchet.service.positions.PositionService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAll() {
        return (List<Permission>) permissionRepository.findAll();
    }

    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
}
