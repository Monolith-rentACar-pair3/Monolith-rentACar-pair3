package com.etiya.rentACar.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="rentals")
public class Rental {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	
	@Column(name="rental_id")
	private int rentalId;
	
	@Column(name="rent_date")
	private Date rentDate;

	@Column(name="return_date") 
	private Date returnDate;

	@Column(name="rent_city")
	private int rentCity;

	@Column(name="return_city")
	private int returnCity;
	
	@ManyToOne
	@JoinColumn(name="car_id")
	private Car car;
	
	@ManyToOne (cascade = CascadeType.DETACH)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(mappedBy = "rental")
	private RentingBill rentingBill;

	@OneToMany(mappedBy = "rental")
	private List<DemandedAdditionalServicesForRentals> demandedAdditionalServicesForRentals;
}


