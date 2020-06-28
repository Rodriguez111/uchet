package uchet.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import uchet.models.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

    Permission findByPermissionDescription(String permissionDescription);

}
