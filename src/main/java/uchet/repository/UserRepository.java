package uchet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uchet.models.User;
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
