package com.etiya.rentACar.business.concretes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.*;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.core.utilities.adapters.findeksServiceAdapter.FinancialDataService;
import com.etiya.rentACar.core.utilities.adapters.posServiceAdapter.PaymentApprovementService;
import com.etiya.rentACar.core.utilities.results.*;
import com.etiya.rentACar.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.dtos.RentalSearchListDto;
import com.etiya.rentACar.business.request.rentalRequests.CreateRentalRequest;
import com.etiya.rentACar.business.request.rentalRequests.DeleteRentalRequest;
import com.etiya.rentACar.business.request.rentalRequests.UpdateRentalRequest;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.dataAccess.abstracts.RentalDao;
import com.etiya.rentACar.business.constants.messages.Messages;

@Service
public class RentalManager implements RentalService {

	private RentalDao rentalDao;
	private ModelMapperService modelMapperService;
	private CarService carService;
	private MaintenanceService maintenanceService;
	private FinancialDataService financialDataService;
	private RentingBillService rentingBillService;
	private PaymentApprovementService paymentApprovementService;
	private CreditCardService creditCardService;
	private AdditionalServiceService additionalServiceService;
	private MessageService messageService;
	private DemandedAdditionalServicesForRentalsService demandedAdditionalServicesForRentalsService;
	
	@Autowired
	public RentalManager(RentalDao rentalDao, ModelMapperService modelMapperService, 
			CarService carService, FinancialDataService financialDataService,
			@Lazy MaintenanceService maintenanceService,RentingBillService rentingBillService,
						 PaymentApprovementService paymentApprovementService,
						 CreditCardService creditCardService,
						 AdditionalServiceService additionalServiceService, MessageService messageService,
						 DemandedAdditionalServicesForRentalsService demandedAdditionalServicesForRentalsService) {
		super();
		this.rentalDao = rentalDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
		this.maintenanceService = maintenanceService;
		this.financialDataService = financialDataService;
		this.rentingBillService = rentingBillService;
		this.paymentApprovementService = paymentApprovementService;
		this.creditCardService = creditCardService;
		this.additionalServiceService = additionalServiceService;
		this.messageService = messageService;
		this.demandedAdditionalServicesForRentalsService = demandedAdditionalServicesForRentalsService;
	}

	@Override
	public DataResult<List<RentalSearchListDto>> getAll() {
		List<Rental> result = this.rentalDao.findAll();
		List<RentalSearchListDto> response = result.stream().map(rental -> modelMapperService.forDto()
				.map(rental, RentalSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<RentalSearchListDto>>(response);
	}

	@Override
	public Result save(CreateRentalRequest createRentalRequest) {
		Result result = BusinessRules.run(carService.checkExistingCar(createRentalRequest.getCarId()),
				checkCarIsReturned(createRentalRequest.getCarId()),
				checkFindeksPointAcceptability(createRentalRequest.getCarId(),createRentalRequest.getUserId()),
				maintenanceService.checkIfCarIsOnMaintenance(createRentalRequest.getCarId()));
				//checkIfPaymentSuccessful(creditCardService.getById(3)),;
	
		if(result != null) {
			return result;
		}
		updateCityNameIfReturnCityIsDifferent(createRentalRequest);
		Rental rental = modelMapperService.forRequest().map(createRentalRequest, Rental.class);
		this.rentalDao.save(rental);
		
		return new SuccessResult(messageService.getMessage(Messages.addRental));
	}

	@Override
	public Result delete(DeleteRentalRequest deleteRentalRequest) {
		Result result = BusinessRules.run(checkIfRentalIdExists(deleteRentalRequest.getRentalId()));

		if(result != null) {
			return result;
		}
		Rental rental = modelMapperService.forRequest().map(deleteRentalRequest, Rental.class);
		this.rentalDao.delete(rental);
		return new SuccessResult(messageService.getMessage(Messages.deleteRental));
	}

	@Override
	public Result update(UpdateRentalRequest updateRentalRequest) {
		Result result = BusinessRules.run(checkIfEndDateIsAfterStartDate(updateRentalRequest.getReturnDate(), updateRentalRequest.getRentDate()),
				checkIfRentalIdExists(updateRentalRequest.getRentalId()),
				checkIfReturnKilometerIsBiggerThanRentKilometer(updateRentalRequest.getReturnKilometer(),updateRentalRequest.getCarId()));
		
		if(result != null) {
			return result;
		}
		updateCityNameIfReturnCityIsDifferent(updateRentalRequest);
		this.carService.updateCarKilometer(updateRentalRequest.getCarId(),updateRentalRequest.getReturnKilometer());
		Rental rental = modelMapperService.forRequest().map(updateRentalRequest, Rental.class);
		this.rentalDao.save(rental);
		return new SuccessResult(messageService.getMessage(Messages.updateRental));
	}
	
	public Result checkCarIsReturned(int carId) {
		List<Rental> result = this.rentalDao.getByCar_CarId(carId);
		if(result != null) {
			for (Rental rentals : this.rentalDao.getByCar_CarId(carId)) {
				if(rentals.getReturnDate() == null) {
					return new ErrorResult(messageService.getMessage(Messages.rentalCarIsOnRental));
				}
			}
		}
		return new SuccessResult();
	}
	
	private Result checkFindeksPointAcceptability(int carId, int userId) {
		Car car = carService.getCarAsElementByCarId(carId);
		int findeksCar = car.getFindeksPointCar();
		int findeksUser = financialDataService.getFindeksScore(userId);
		if(findeksCar>findeksUser) {
			return new ErrorResult(messageService.getMessage(Messages.findexPointIsNotEnough));
		}
		return new SuccessResult();
	}
	@Override
	public Result checkIfEndDateIsAfterStartDate(Date endDate, Date startDate) {
		if(endDate != null) {
			if(endDate.before(startDate)) {
				return new ErrorResult(messageService.getMessage(Messages.dateAccordance));
			}
		}
		return new SuccessResult();
	}

	@Override
	public Rental getById(int rentalId) {
		return rentalDao.getById(rentalId);
	}

	private void updateCityNameIfReturnCityIsDifferent(CreateRentalRequest createRentalRequest){
		if(((createRentalRequest.getRentCity()) != (createRentalRequest.getReturnCity()))){
			this.carService.updateCarCity(createRentalRequest.getCarId(),createRentalRequest.getReturnCity());
		}
	}
	private void updateCityNameIfReturnCityIsDifferent(UpdateRentalRequest updateRentalRequest){
		if(((updateRentalRequest.getRentCity()) != (updateRentalRequest.getReturnCity()))){
			this.carService.updateCarCity(updateRentalRequest.getCarId(),updateRentalRequest.getReturnCity());
		}
	}
	private Result checkIfPaymentSuccessful(CreditCard creditCard){
		//creditCard.setCardNumber("");
		boolean result = paymentApprovementService.checkPaymentSuccess(creditCard);
		if(result==false){
			return new ErrorResult(messageService.getMessage(Messages.paymentUnsuccessful));
		}
		return new SuccessResult();
	}

	public DataResult<List<AdditionalService>> getAdditionalServices(Rental rental) {
		List<DemandedAdditionalServicesForRentals> list = this.demandedAdditionalServicesForRentalsService.getAllByRental(rental).getData();
		List<AdditionalService> response = new ArrayList<AdditionalService>();
		for (DemandedAdditionalServicesForRentals demandedAdditionalServicesForRentals : list) {
			response.add(demandedAdditionalServicesForRentals.getAdditionalService());
		}
		return new SuccessDataResult<List<AdditionalService>>(response);
	}

	@Override
	public Result checkIfRentalIdExists(int rentalId) {
		if (this.rentalDao.existsById(rentalId)){
			return new SuccessResult();
		}
		return new ErrorResult(messageService.getMessage(Messages.rentalIdDoesNotExist));
	}
	@Override
	public Result checkIfBillIsAlreadyCreated(int rentalId){
		List<RentingBill> bills = rentingBillService.rentingBills();
		for (RentingBill bill: bills) {
			if (bill.getRental().getRentalId() == rentalId){
				return new ErrorResult(messageService.getMessage(Messages.rentalIdAlreadyExistsInBillTable));
			}
		}
		return new SuccessResult();
	}

	private Result checkIfReturnKilometerIsBiggerThanRentKilometer(int returnKilometer, int carId){
		if(returnKilometer <= carService.getCarAsElementByCarId(carId).getKilometer()){
			return new ErrorResult(messageService.getMessage(Messages.invalidReturnKilometer));
		}
		return new SuccessResult();
	}
	@Override
	public Result checkIfReturnDateIsNull(int rentalId){
		Date endDate = rentalDao.getById(rentalId).getReturnDate();
		if (endDate == null){
			return new ErrorResult(messageService.getMessage(Messages.returnDateIsNull));
		}
		return new SuccessResult();
	}
}
