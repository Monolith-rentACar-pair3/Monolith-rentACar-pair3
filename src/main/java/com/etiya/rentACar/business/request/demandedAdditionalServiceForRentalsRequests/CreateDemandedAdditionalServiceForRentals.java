package com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDemandedAdditionalServiceForRentals {
    @JsonIgnore
    private int id;

    @NotNull
    private int rentalId;

    @NotNull
    private int serviceId;
}
