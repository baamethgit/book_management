package ba.ameth.projects.bookManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ba.ameth.projects.bookManagement.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User>  findByEmail(String username);

    boolean existsByEmail(String email);
}
