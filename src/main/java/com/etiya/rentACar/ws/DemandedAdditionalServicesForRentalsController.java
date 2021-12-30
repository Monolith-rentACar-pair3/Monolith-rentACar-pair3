package com.etiya.rentACar.ws;

import com.etiya.rentACar.business.abstracts.DemandedAdditionalServicesForRentalsService;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.CreateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.DeleteDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.UpdateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.etiya.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/demandedAdditionalServicesForRentals")
public class DemandedAdditionalServicesForRentalsController {
    private DemandedAdditionalServicesForRentalsService demandedAdditionalServicesForRentalsService;

    @Autowired
    public DemandedAdditionalServicesForRentalsController(DemandedAdditionalServicesForRentalsService demandedAdditionalServicesForRentalsService) {
        this.demandedAdditionalServicesForRentalsService = demandedAdditionalServicesForRentalsService;
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid CreateDemandedAdditionalServiceForRentals createDemandedAdditionalServiceForRentals) {
        return this.demandedAdditionalServicesForRentalsService.save(createDemandedAdditionalServiceForRentals);
    }
    @DeleteMapping("delete")
    public Result delete(@RequestBody @Valid DeleteDemandedAdditionalServiceForRentals deleteDemandedAdditionalServiceForRentals) {
        return this.demandedAdditionalServicesForRentalsService.delete(deleteDemandedAdditionalServiceForRentals);
    }
    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateDemandedAdditionalServiceForRentals updateDemandedAdditionalServiceForRentals) {
        return this.demandedAdditionalServicesForRentalsService.update(updateDemandedAdditionalServiceForRentals);
    }
}
