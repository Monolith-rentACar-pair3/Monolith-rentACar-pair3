package com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteDemandedAdditionalServiceForRentals {
    @NotNull
    private int id;
}
