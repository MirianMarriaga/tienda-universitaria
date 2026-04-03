package edu.unimagdalena.tienda_universitaria.repositories;

import edu.unimagdalena.tienda_universitaria.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer_Id(Long customerId);
}
