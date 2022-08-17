package tech.theraven.cloudrender.repository;

import org.springframework.data.repository.CrudRepository;
import tech.theraven.cloudrender.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
