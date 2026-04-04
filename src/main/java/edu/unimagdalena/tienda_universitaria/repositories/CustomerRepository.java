package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("""
            SELECT o.customer.id, o.customer.fullName, SUM(o.total)
            FROM Order o
            WHERE o.status <> 'CANCELLED'
            GROUP BY o.customer.id, o.customer.fullName
            ORDER BEY SUM(o.total) DESC
            """)
    List<Object[]> findTopByCustomer();
}
