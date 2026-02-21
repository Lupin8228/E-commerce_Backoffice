package com.ecommerce.backoffice.domain.admin.repository;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String newEmail, Long adminId);
    Optional<Admin> findByEmail(String email);

    Long countByDeletedFalse();

    Long countByStatusAndDeletedFalse(AdminStatus adminStatus);
}
