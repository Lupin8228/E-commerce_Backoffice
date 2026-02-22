package com.ecommerce.backoffice.domain.customer.repository;

import com.ecommerce.backoffice.domain.customer.entity.Customer;
import com.ecommerce.backoffice.domain.customer.enums.CustomerStatus;
import com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findAll(Pageable pageable);
    Optional<Customer> findByIdAndDeletedFalse(Long id);

    Long countByDeletedFalse();

    Long countByStatusAndDeletedFalse(CustomerStatus customerStatus);

    @Query("""
        SELECT new com.ecommerce.backoffice.domain.dashboard.dto.chart.DashboardChartItem(
            CAST(c.status as string),
            COUNT(c)
        )
        FROM Customer c
        WHERE c.deleted = false
        GROUP BY c.status
        ORDER BY c.status
        """)
    List<DashboardChartItem> countStatusDistribution();
}
