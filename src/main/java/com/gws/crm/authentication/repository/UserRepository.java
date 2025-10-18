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

    boolean existsByUsername(String username);

    @Query("SELECT new com.gws.crm.authentication.dto.UserDetailsDTO(" +
            "u.id,u.name,u.username,u.password,u.locked, u.enabled, u.deleted, u.accountNonExpired, u" +
            ".credentialsNonExpired, r," +
            " " +
            "p) " +
            "FROM User u " +
            "LEFT JOIN u.roles r " +
            "LEFT JOIN u.privileges p " +
            "WHERE u.id = :id")
    Optional<UserDetailsDTO> findUserDetailById(long id);


    @Query("SELECT new com.gws.crm.authentication.dto.UserDetailsDTO(" +
            "u.id,u.name,u.username,u.password,u.locked, u.enabled, u.deleted, u.accountNonExpired, u.credentialsNonExpired, r, " +
            "p) " +
            "FROM User u " +
            "LEFT JOIN u.roles r " +
            "LEFT JOIN u.privileges p " +
            "WHERE u.username = :username")
    Optional<UserDetailsDTO> findDTOByUsername(String username);

    @Query("SELECT new com.gws.crm.authentication.dto.UserDetailsDTO(" +
            "u.id,u.name,u.username,u.password,u.locked, u.enabled, u.deleted, u.accountNonExpired, u.credentialsNonExpired, r, " +
            "p) " +
            "FROM User u " +
            "LEFT JOIN u.roles r " +
            "LEFT JOIN u.privileges p " +
            "WHERE u.id = :id")
    Optional<UserDetailsDTO> findDTOById(long id);
}
