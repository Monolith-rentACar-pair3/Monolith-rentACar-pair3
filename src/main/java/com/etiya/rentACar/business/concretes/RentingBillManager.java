package com.etiya.rentACar.business.concretes;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.*;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.business.request.rentingBillRequests.CreateRentingBillRequest;
import com.etiya.rentACar.business.request.rentingBillRequests.UpdateRentingBillRequest;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.results.*;
import com.etiya.rentACar.entities.AdditionalService;
import com.etiya.rentACar.entities.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.dtos.RentingBillSearchListDto;
import com.etiya.rentACar.business.request.rentingBillRequests.DeleteRentingBillRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.dataAccess.abstracts.RentingBillDao;
import com.etiya.rentACar.entities.RentingBill;


@Service
public class RentingBillManager implements RentingBillService {

	private RentingBillDao rentingBillDao;
	private ModelMapperService modelMapperService;
	private UserService userService;
	private CarService carService;
	private RentalService rentalService;
	private MessageService messageService;

	@Autowired
	public RentingBillManager(RentingBillDao rentingBillDao, ModelMapperService modelMapperService,
							  UserService userService, CarService carService, @Lazy RentalService rentalService,
							  MessageService messageService) {
		super();
		this.rentingBillDao = rentingBillDao;
		this.modelMapperService = modelMapperService;
		this.userService = userService;
		this.carService = carService;
		this.rentalService = rentalService;
		this.messageService = messageService;
	}

	@Override
	public DataResult<List<RentingBillSearchListDto>> getAll() {
		List<RentingBill> result = rentingBillDao.findAll();
		List<RentingBillSearchListDto> response = result.stream().map(rentingBill -> modelMapperService.forDto().
				map(rentingBill, RentingBillSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<RentingBillSearchListDto>>(response);
	}

	@Override
	public Result save(CreateRentingBillRequest createRentingBillRequest) {
		Result result = BusinessRules.run(rentalService.checkIfBillIsAlreadyCreated(createRentingBillRequest.getRentalId()));
		if (result != null){
			return result;
		}
		Rental rental = rentalService.getById(createRentingBillRequest.getRentalId());
		RentingBill rentingBill = new RentingBill();
		Date dateNow = new java.sql.Date(new java.util.Date().getTime());
		rentingBill.setCreationDate(dateNow);
		rentingBill.setRentingStartDate(rental.getRentDate());
		rentingBill.setRentingEndDate(rental.getReturnDate());
		rentingBill.setUser(rental.getUser());
		int totalRentDay = calculateDifferenceBetweenDays(rental.getReturnDate(), rental.getRentDate());
		rentingBill.setTotalRentingDay(totalRentDay);
		int dailyPriceOfCar = (int)(carService.getCarDetailsByCarId(rental.getCar().getCarId()).getData().getDailyPrice());
		rentingBill.setRentingPrice(calculateRentingPrice(dailyPriceOfCar,totalRentDay,rental));
		rentingBill.setRental(rentalService.getById(rental.getRentalId()));
		rentingBillDao.save(rentingBill);
		return new SuccessResult(messageService.getMessage(Messages.addRentingBill));
	}

	@Override
	public Result delete(DeleteRentingBillRequest deleteRentingBillRequest) {
		Result result = BusinessRules.run(checkIfRentingBillIdExist(deleteRentingBillRequest.getBillId()));
		if (result != null){
			return result;
		}
		RentingBill rentingBill = modelMapperService.forRequest().map(deleteRentingBillRequest, RentingBill.class);
		this.rentingBillDao.delete(rentingBill);
		return new SuccessResult(messageService.getMessage(Messages.deleteRentingBill));
	}

	@Override
	public Result update(UpdateRentingBillRequest updateRentingBillRequest) {
		Result result = BusinessRules.run(checkIfRentingBillIdExist(updateRentingBillRequest.getBillId()),
				checkIfRentalIdIsUsedBefore(updateRentingBillRequest.getBillId(),updateRentingBillRequest.getRentalId()));
		if (result != null){
			return result;
		}
		Date dateNow = new java.sql.Date(new java.util.Date().getTime());
		RentingBill rentingBill = modelMapperService.forRequest().map(updateRentingBillRequest, RentingBill.class);
		rentingBill.setCreationDate(dateNow);
		this.rentingBillDao.save(rentingBill);
		return new SuccessResult(messageService.getMessage(Messages.updateRentingBill));
	}
	
	private int calculateDifferenceBetweenDays(Date endDate, Date startDate) {
		long difference = (endDate.getTime() - startDate.getTime())/86400000;
		return Math.abs((int)difference);
	}

	@Override
	public DataResult<List<RentingBillSearchListDto>> getRentingBillByUserId(int userId) {
		Result result = BusinessRules.run(userService.existsById(userId));
		if (result != null){
			return new ErrorDataResult<List<RentingBillSearchListDto>>(null, messageService.getMessage(Messages.userDoesNotExist));
		}
		List<RentingBill> list = rentingBillDao.getByUser_UserId(userId);
		List<RentingBillSearchListDto> response = list.stream().map(rentingBill -> modelMapperService.forDto().
				map(rentingBill, RentingBillSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<RentingBillSearchListDto>>(response);
	}

	@Override
	public DataResult<List<RentingBillSearchListDto>> getRentingBillByDateInterval(Date startDate, Date endDate) {
		Result result = BusinessRules.run(rentalService.checkIfEndDateIsAfterStartDate(endDate,startDate));
		if (result != null){
			return new ErrorDataResult<List<RentingBillSearchListDto>>(null, messageService.getMessage(Messages.dateAccordance));
		}
		List<RentingBill> list = rentingBillDao.findByCreationDateBetween(startDate, endDate);
		List<RentingBillSearchListDto> response = list.stream().map(rentingBill -> modelMapperService.forDto().
				map(rentingBill, RentingBillSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<RentingBillSearchListDto>>(response);
	}

	@Override
	public List<RentingBill> rentingBills() {
		return rentingBillDao.findAll();
	}

	private int calculateRentingPrice(int dailyPriceOfCar,
									  int totalRentDay, Rental rental){

		List<AdditionalService> list = new ArrayList<>();
		int totalDailyAdditionalServiceCost = 0;
		if (rentalService.getAdditionalServices(rental) != null){
			list = rentalService.getAdditionalServices(rental).getData();
			for (AdditionalService service : list) {
				totalDailyAdditionalServiceCost += service.getServiceDailyPrice();
			}
		}
			if (rental.getRentCity() != rental.getReturnCity()){
				int price = ((dailyPriceOfCar+totalDailyAdditionalServiceCost)*totalRentDay) + 500;
				return price;
			}
			int price = (dailyPriceOfCar+totalDailyAdditionalServiceCost)*totalRentDay;
			return price;
	}
	private Result checkIfRentingBillIdExist(int rentingBillId){
		if (rentingBillDao.existsById(rentingBillId)){
			return new SuccessResult();
		}
		return new ErrorResult(messageService.getMessage(Messages.rentingBillIdDoesNotExist));
	}

	private Result checkIfRentalIdIsUsedBefore(int billId, int rentalId){
		List<RentingBill> list = this.rentingBillDao.findAll();
		for (RentingBill rentingBill : list) {
			if (rentingBill.getBillId() == billId){
				continue;
			}
			if(rentingBill.getRental().getRentalId() == rentalId){
				return new ErrorResult(messageService.getMessage(Messages.rentalIdAlreadyExistsInBillTable));
			}
		}
		return new SuccessResult();
	}

}
