package com.gws.crm.authentication.repository;

import com.gws.crm.authentication.dto.UserDetailsDTO;
import com.gws.crm.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u  " +
            "LEFT JOIN FETCH u.roles " +
            "LEFT JOIN FETCH u.privileges " +
            "WHERE u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u  " +
            "LEFT JOIN FETCH u.roles " +
            "LEFT JOIN FETCH u.privileges " +
            "WHERE u.id = :id")
    Optional<User> findById(long id);

    boolean existsByUsername(String username);

}
