package lv.notes.ims.dao;

import lv.notes.ims.domain.Role;
import lv.notes.ims.domain.User;
import lv.notes.ims.repository.RoleRepository;
import lv.notes.ims.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDaoRepositoryIntegrationTest extends AbstractDaoRepositoryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    void shouldAddRoleToUserWhenRoleAlreadyExist() {
        var newRole = new Role().setName("VIEWER");
        var existedRole = transaction.execute(status -> roleRepository.save(newRole));
        var newUser = new User().setName("Bob")
                .setSurname("Marley")
                .addRole(existedRole);

        var savedUser = assertDoesNotThrow(() -> transaction.execute(status -> userRepository.save(newUser)));

        var userOfRole = transaction.execute(status -> roleRepository
                .findById(existedRole.getId())
                .map(Role::getUsers).orElseThrow()
                .stream().findFirst()
                .orElseThrow());

        assertThat(userOfRole.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void shouldThrowExceptionWhenTryToSaveUserWithNotExistenceRole() {
        var newRole = new Role().setName("VIEWER");
        var newUser = new User().setName("Bob")
                .setSurname("Marley")
                .addRole(newRole);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> transaction.execute(status -> userRepository.save(newUser)));
    }

}
