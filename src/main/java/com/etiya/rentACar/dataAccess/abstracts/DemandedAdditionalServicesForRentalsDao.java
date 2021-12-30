package com.etiya.rentACar.dataAccess.abstracts;

import com.etiya.rentACar.entities.DemandedAdditionalServicesForRentals;
import com.etiya.rentACar.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandedAdditionalServicesForRentalsDao extends JpaRepository<DemandedAdditionalServicesForRentals, Integer> {
    List<DemandedAdditionalServicesForRentals> getAllByRental(Rental rental);
}
