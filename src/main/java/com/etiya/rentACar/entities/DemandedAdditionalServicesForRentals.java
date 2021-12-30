package com.etiya.rentACar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "demanded_additional_services_for_rentals")
public class DemandedAdditionalServicesForRentals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "service_id")
    AdditionalService additionalService;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    Rental rental;
}
