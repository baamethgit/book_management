package ba.ameth.projects.bookManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ba.ameth.projects.bookManagement.entities.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
}
