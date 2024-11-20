package com.aclib.aclib_deploy.Repository;

import com.aclib.aclib_deploy.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    User findByUsername(String username);

    User save(User user);

    List<User> findAllByRole(User.UserRole role);

    User findByRegistrationId(String registrationId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
