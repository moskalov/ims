package lv.notes.ims.repository;

import lv.notes.ims.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
