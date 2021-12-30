package com.etiya.rentACar.business.concretes;

import com.etiya.rentACar.business.abstracts.AdditionalServiceService;
import com.etiya.rentACar.business.abstracts.DemandedAdditionalServicesForRentalsService;
import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.abstracts.RentalService;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.CreateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.DeleteDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.business.request.demandedAdditionalServiceForRentalsRequests.UpdateDemandedAdditionalServiceForRentals;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.*;
import com.etiya.rentACar.dataAccess.abstracts.DemandedAdditionalServicesForRentalsDao;
import com.etiya.rentACar.entities.DemandedAdditionalServicesForRentals;
import com.etiya.rentACar.entities.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandedAdditionalServiceForRentalsManager implements DemandedAdditionalServicesForRentalsService {
    private ModelMapperService modelMapperService;
    private DemandedAdditionalServicesForRentalsDao demandedAdditionalServicesForRentalsDao;
    private RentalService rentalService;
    private AdditionalServiceService additionalServiceService;
    private MessageService messageService;

    @Autowired
    public DemandedAdditionalServiceForRentalsManager(ModelMapperService modelMapperService,
                                                      DemandedAdditionalServicesForRentalsDao demandedAdditionalServicesForRentalsDao,
                                                      @Lazy RentalService rentalService, AdditionalServiceService additionalServiceService,
                                                      MessageService messageService) {
        this.modelMapperService = modelMapperService;
        this.demandedAdditionalServicesForRentalsDao = demandedAdditionalServicesForRentalsDao;
        this.rentalService = rentalService;
        this.additionalServiceService = additionalServiceService;
        this.messageService = messageService;
    }

    @Override
    public Result save(CreateDemandedAdditionalServiceForRentals createDemandedAdditionalServiceForRentals) {
        Result result = BusinessRules.run(rentalService.checkIfRentalIdExists(createDemandedAdditionalServiceForRentals.getRentalId()),
                additionalServiceService.checkIfAdditionalServiceIdExists(createDemandedAdditionalServiceForRentals.getServiceId()));
        if (result != null){
            return result;
        }
        DemandedAdditionalServicesForRentals demandedAdditionalServicesForRentals = modelMapperService.forRequest().map(createDemandedAdditionalServiceForRentals,
                DemandedAdditionalServicesForRentals.class);
        this.demandedAdditionalServicesForRentalsDao.save(demandedAdditionalServicesForRentals);
        return new SuccessResult(messageService.getMessage(Messages.addDemandedAdditional));
    }

    @Override
    public Result delete(DeleteDemandedAdditionalServiceForRentals deleteDemandedAdditionalServiceForRentals) {
        Result result = BusinessRules.run(checkIfIdExist(deleteDemandedAdditionalServiceForRentals.getId()));
        if (result != null){
            return result;
        }
        DemandedAdditionalServicesForRentals demandedAdditionalServicesForRentals = modelMapperService.forRequest().map(deleteDemandedAdditionalServiceForRentals,
                DemandedAdditionalServicesForRentals.class);
        this.demandedAdditionalServicesForRentalsDao.delete(demandedAdditionalServicesForRentals);
        return new SuccessResult(messageService.getMessage(Messages.deleteDemandedAdditional));
    }

    @Override
    public Result update(UpdateDemandedAdditionalServiceForRentals updateDemandedAdditionalServiceForRentals) {
        Result result = BusinessRules.run(checkIfIdExist(updateDemandedAdditionalServiceForRentals.getId()));
        if (result != null){
            return result;
        }
        DemandedAdditionalServicesForRentals demandedAdditionalServicesForRentals = modelMapperService.forRequest().map(updateDemandedAdditionalServiceForRentals,
                DemandedAdditionalServicesForRentals.class);
        this.demandedAdditionalServicesForRentalsDao.save(demandedAdditionalServicesForRentals);
        return new SuccessResult(messageService.getMessage(Messages.updateDemandedAdditional));
    }

    @Override
    public DataResult<List<DemandedAdditionalServicesForRentals>> getAllByRental(Rental rental) {
        return new SuccessDataResult<List<DemandedAdditionalServicesForRentals>>(this.demandedAdditionalServicesForRentalsDao.getAllByRental(rental), "");
    }

    private Result checkIfIdExist(int id){
        if(this.demandedAdditionalServicesForRentalsDao.existsById(id)){
            return new SuccessResult();
        }
        return new ErrorResult(messageService.getMessage(Messages.demandedAdditionalDoesNotExist));
    }

}
