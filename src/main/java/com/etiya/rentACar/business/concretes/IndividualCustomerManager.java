package com.etiya.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.abstracts.UserService;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.results.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.abstracts.IndividualCustomerService;
import com.etiya.rentACar.business.dtos.IndividualCustomerSearchListDto;
import com.etiya.rentACar.business.request.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.etiya.rentACar.business.request.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.etiya.rentACar.business.request.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.dataAccess.abstracts.IndividualCustomerDao;
import com.etiya.rentACar.entities.IndividualCustomer;

@Service
public class IndividualCustomerManager implements IndividualCustomerService{
	
	private IndividualCustomerDao individualCustomerDao;
	private ModelMapperService modelMapperService;
	private UserService userService;
	private MessageService messageService;
	@Autowired
	public IndividualCustomerManager(IndividualCustomerDao individualCustomerDao, ModelMapperService modelMapperService, UserService userService,
									 MessageService messageService) {
		super();
		this.individualCustomerDao = individualCustomerDao;
		this.modelMapperService = modelMapperService;
		this.userService = userService;
		this.messageService = messageService;
	}

	@Override
	public DataResult<List<IndividualCustomerSearchListDto>> getAll() {
		List<IndividualCustomer> result = this.individualCustomerDao.findAll();
		List<IndividualCustomerSearchListDto> response = result.stream().map(individualCustomer -> modelMapperService.forDto()
				.map(individualCustomer, IndividualCustomerSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<IndividualCustomerSearchListDto>>(response);
	}

	@Override
	public Result save(CreateIndividualCustomerRequest createIndividualCustomerRequest) {
		Result result = BusinessRules.run(userService.existsByEmail(createIndividualCustomerRequest.getEmail()));
		if (result != null){
			return result;
		}
		IndividualCustomer individualCustomer = modelMapperService.forRequest().map(createIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.save(individualCustomer);
		return new SuccessResult(messageService.getMessage(Messages.addIndividualCustomer));
	}

	@Override
	public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {
		Result result = BusinessRules.run(checkIfUserIdExists(deleteIndividualCustomerRequest.getUserId()));
		if (result != null){
			return result;
		}
		IndividualCustomer individualCustomer = modelMapperService.forRequest().map(deleteIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.delete(individualCustomer);
		return new SuccessResult(messageService.getMessage(Messages.deleteIndividualCustomer));
	}

	@Override
	public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) {
		Result result = BusinessRules.run(checkIfUserIdExists(updateIndividualCustomerRequest.getUserId()));
		if (result != null){
			return result;
		}
		IndividualCustomer individualCustomer = modelMapperService.forRequest().map(updateIndividualCustomerRequest, IndividualCustomer.class);
		this.individualCustomerDao.save(individualCustomer);
		return new SuccessResult(messageService.getMessage(Messages.updateIndividualCustomer));
	}

	@Override
	public IndividualCustomer getCustomerByCustomerId(int customerId) {
		IndividualCustomer individualCustomer = individualCustomerDao.getById(customerId);
		return individualCustomer;
	}

	private Result checkIfUserIdExists(int userId){
		IndividualCustomer individualCustomer = individualCustomerDao.getByUserId(userId);
		if (individualCustomer == null){
			return new ErrorResult(messageService.getMessage(Messages.userDoesNotExist));
		}
		return new SuccessResult();
	}

}
