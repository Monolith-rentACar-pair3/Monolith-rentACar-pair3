package com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDemandedAdditionalServiceForRentals {
    @NotNull
    private int id;

    @NotNull
    private int rentalId;

    @NotNull
    private int serviceId;
}
