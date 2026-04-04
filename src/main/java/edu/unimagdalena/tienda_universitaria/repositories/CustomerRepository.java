package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Customer;
import edu.unimagdalena.tienda_universitaria.entities.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByStatus(CustomerStatus status);

    Optional<Customer> findByIdentificationNumber(String identificationNumber);

    @Query("""
            SELECT o.customer.id, o.customer.fullName, SUM(o.total)
            FROM Order o
            WHERE o.status <> 'CANCELLED'
            GROUP BY o.customer.id, o.customer.fullName
            ORDER BY SUM(o.total) DESC
            """)
    List<Object[]> findTopByCustomer();
}