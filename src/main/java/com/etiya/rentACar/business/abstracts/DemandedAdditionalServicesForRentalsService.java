package com.etiya.rentACar.business.abstracts;

import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.CreateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.DeleteDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.UpdateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.core.utilities.results.DataResult;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.entities.DemandedAdditionalServicesForRentals;
import com.etiya.rentACar.entities.Rental;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

public interface DemandedAdditionalServicesForRentalsService {
    Result save(CreateDemandedAdditionalServiceForRentals createDemandedAdditionalServiceForRentals);
    Result delete(DeleteDemandedAdditionalServiceForRentals deleteDemandedAdditionalServiceForRentals);
    Result update(UpdateDemandedAdditionalServiceForRentals updateDemandedAdditionalServiceForRentals);
    DataResult<List<DemandedAdditionalServicesForRentals>> getAllByRental(Rental rental);
}
