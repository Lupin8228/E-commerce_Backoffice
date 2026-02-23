package com.ecommerce.backoffice.domain.admin.repository;

import com.ecommerce.backoffice.domain.admin.entity.Admin;
import com.ecommerce.backoffice.domain.admin.enums.AdminRole;
import com.ecommerce.backoffice.domain.admin.enums.AdminStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String newEmail, Long adminId);

    Optional<Admin> findByEmail(String email);

    // 관리자 리스트 조회 전용
    @Query("""
                select p from Admin p
                where p.deletedAt is null
                  and (:status is null or p.status = :status)
                  and (:role is null or p.role = :role)
                  and (:keyword is null or :keyword = ''
                       or lower(p.name)  like lower(concat('%', :keyword, '%'))
                       or lower(p.email) like lower(concat('%', :keyword, '%')))
            """)
    Page<Admin> findAllAdmins(
            @Param("keyword") String keyword,
            @Param("role") AdminRole role,
            @Param("status") AdminStatus status,
            Pageable pageable
    );

    Long countByDeletedFalse();

    Long countByStatusAndDeletedFalse(AdminStatus adminStatus);
}
