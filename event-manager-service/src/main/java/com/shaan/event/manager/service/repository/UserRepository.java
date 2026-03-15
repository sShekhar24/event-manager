package com.shaan.event.manager.service.repository;

import com.shaan.event.manager.service.entity.User;
import com.shaan.event.manager.service.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Page<User> findByRole(Role role, Pageable pageable);
    
    Page<User> findByFirstNameContainingOrLastNameContainingOrEmailContaining(
        String firstName, 
        String lastName, 
        String email, 
        Pageable pageable
    );
    
    Long countByRole(Role role);
}
