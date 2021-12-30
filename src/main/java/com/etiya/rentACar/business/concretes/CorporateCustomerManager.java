package com.etiya.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.abstracts.UserService;
import com.etiya.rentACar.business.constants.messages.CorporateCustomerMessages;
import com.etiya.rentACar.business.constants.messages.CreditCardMessages;
import com.etiya.rentACar.business.constants.messages.UserMessages;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.results.*;
import com.etiya.rentACar.entities.IndividualCustomer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.abstracts.CorporateCustomerService;
import com.etiya.rentACar.business.dtos.CorporateCustomerSearchListDto;
import com.etiya.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.etiya.rentACar.business.request.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.etiya.rentACar.business.request.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.dataAccess.abstracts.CorporateCustomerDao;
import com.etiya.rentACar.entities.CorporateCustomer;
@Service
public class CorporateCustomerManager implements CorporateCustomerService{

	private ModelMapperService modelMapperService;
	private CorporateCustomerDao corporateCustomerDao;
	private UserService userService;
	private Environment environment;
	private MessageService messageService;
	
	@Autowired
	public CorporateCustomerManager(ModelMapperService modelMapperService, CorporateCustomerDao corporateCustomerDao, UserService userService,
									Environment environment, MessageService messageService) {
		super();
		this.modelMapperService = modelMapperService;
		this.corporateCustomerDao = corporateCustomerDao;
		this.userService = userService;
		this.environment = environment;
		this.messageService = messageService;
	}

	@Override
	public DataResult<List<CorporateCustomerSearchListDto>> getAll() {
		List<CorporateCustomer> result = corporateCustomerDao.findAll();
		List<CorporateCustomerSearchListDto> response = result.stream().map(corporateCustomer -> modelMapperService.forDto()
				.map(corporateCustomer, CorporateCustomerSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<CorporateCustomerSearchListDto>>(response);
	}

	@Override
	public Result save(CreateCorporateCustomerRequest createCorporateCustomerRequest) {
		Result result = BusinessRules.run(userService.existsByEmail(createCorporateCustomerRequest.getEmail()),
				checkIfTaxNumberIsNumeric(createCorporateCustomerRequest.getTaxNumber()));
		if (result != null){
			return result;
		}
		CorporateCustomer corporateCustomer = modelMapperService.forRequest().map(createCorporateCustomerRequest, CorporateCustomer.class);
		this.corporateCustomerDao.save(corporateCustomer);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),36));
	}

	@Override
	public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {
		Result result = BusinessRules.run(checkIfUserIdExists(deleteCorporateCustomerRequest.getUserId()));
		if (result != null){
			return result;
		}
		CorporateCustomer corporateCustomer = modelMapperService.forRequest().map(deleteCorporateCustomerRequest, CorporateCustomer.class);
		this.corporateCustomerDao.delete(corporateCustomer);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),37));
	}

	@Override
	public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest) {
		Result result = BusinessRules.run(checkIfUserIdExists(updateCorporateCustomerRequest.getUserId()),
				checkIfTaxNumberIsNumeric(updateCorporateCustomerRequest.getTaxNumber()));
		if (result != null){
			return result;
		}
		CorporateCustomer corporateCustomer = modelMapperService.forRequest().map(updateCorporateCustomerRequest, CorporateCustomer.class);
		this.corporateCustomerDao.save(corporateCustomer);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),38));
	}

	@Override
	public CorporateCustomer getCustomerByCustomerId(int customerId) {
		CorporateCustomer corporateCustomer = corporateCustomerDao.getById(customerId);
		return corporateCustomer;
	}

	private Result checkIfUserIdExists(int userId){
		CorporateCustomer corporateCustomer = corporateCustomerDao.getByUserId(userId);
		if (corporateCustomer == null){
			return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),30));
		}
		return new SuccessResult();
	}

	public Result checkIfTaxNumberIsNumeric(String taxNumber){
		if(StringUtils.isNumeric(taxNumber)){
			return new SuccessResult();
		}
		else {
			return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),45));
		}
	}

}
