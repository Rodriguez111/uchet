package uchet.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uchet.models.User;
@Repository
public interface UserRepository extends CrudRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByLogin(String login);

    @Query("SELECT u FROM User u WHERE u.id <> :id AND login = :login")
    User findByLoginForUpdate(@Param("login") String login, @Param("id") int id);

//    @Query("SELECT u FROM User u WHERE u.position = :id LIMIT 1")
//    User findByPositionId(@Param("user_position_id") int id);
        User findTop1ByPositionId(int id);



}
