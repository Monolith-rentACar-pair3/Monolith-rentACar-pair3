package com.etiya.rentACar.business.concretes;

import com.etiya.rentACar.business.abstracts.*;
import com.etiya.rentACar.business.constants.messages.Messages;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.request.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.etiya.rentACar.business.request.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.etiya.rentACar.business.request.loginAndRegister.LoginRequest;
import com.etiya.rentACar.business.request.loginAndRegister.RegisterCorporateCustomerRequest;
import com.etiya.rentACar.business.request.loginAndRegister.RegisterIndividualCustomerRequest;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.ErrorResult;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessResult;

@Service
public class LoginManager implements LoginService {
	
	private UserService userService;
	private ModelMapperService modelMapperService;
	private IndividualCustomerService individualCustomerService;
	private CorporateCustomerService corporateCustomerService;
	private MessageService messageService;
	
	public LoginManager(UserService userService, ModelMapperService modelMapperService,
			IndividualCustomerService individualCustomerService, CorporateCustomerService corporateCustomerService, MessageService messageService) {
		super();
		this.userService = userService;
		this.modelMapperService = modelMapperService;
		this.individualCustomerService = individualCustomerService;
		this.corporateCustomerService = corporateCustomerService;
		this.messageService = messageService;
	}

	@Override
	public Result login(LoginRequest loginRequest) {
		Result result = BusinessRules.run(this.checkCustomerByEmail(loginRequest),
				this.checkCustomerByPassword(loginRequest));
		if (result != null) {
			return result;
		}
		return new SuccessResult(messageService.getMessage(Messages.loginSuccessful));
	}

	@Override
	public Result individualCustomerRegister(RegisterIndividualCustomerRequest registerIndividualCustomerRequest) {
		Result resultCheck = BusinessRules.run(checkPasswordConfirmation(registerIndividualCustomerRequest.getPassword(), registerIndividualCustomerRequest.getPasswordConfirm()),
				userService.existsByEmail(registerIndividualCustomerRequest.getEmail()));
		if(resultCheck != null) {
			return resultCheck;
		}
		CreateIndividualCustomerRequest result = modelMapperService.forRequest()
				.map(registerIndividualCustomerRequest, CreateIndividualCustomerRequest.class);
		this.individualCustomerService.save(result);
		return new SuccessResult(messageService.getMessage(Messages.addIndividualCustomer));
	}
	@Override
	public Result corporateCustomerRegister(RegisterCorporateCustomerRequest registerCorporateCustomerRequest) {
		Result resultCheck = BusinessRules.run(checkPasswordConfirmation(registerCorporateCustomerRequest.getPassword(),registerCorporateCustomerRequest.getPasswordConfirm()),
				userService.existsByEmail(registerCorporateCustomerRequest.getEmail()),
				corporateCustomerService.checkIfTaxNumberIsNumeric(registerCorporateCustomerRequest.getTaxNumber()));
		if(resultCheck != null) {
			return resultCheck;
		}
		CreateCorporateCustomerRequest result = modelMapperService.forRequest()
				.map(registerCorporateCustomerRequest, CreateCorporateCustomerRequest.class);
		this.corporateCustomerService.save(result);
		return new SuccessResult(messageService.getMessage(Messages.addCorporateCustomer));
	}
	
	private Result checkCustomerByPassword(LoginRequest loginRequest) {

		if (checkCustomerByEmail(loginRequest).isSuccess()) {

			if (!this.userService.getByEmail(loginRequest.getEmail()).getData().getPassword()
					.equals(loginRequest.getPassword())) {
				return new ErrorResult(messageService.getMessage(Messages.wrongPassword));
			}
		}
		return new SuccessResult();
	}
	
	private Result checkCustomerByEmail(LoginRequest loginRequest) {

		if (this.userService.existsByEmail(loginRequest.getEmail()).isSuccess()) {
			return new ErrorResult(messageService.getMessage(Messages.wrongEmail));
		}
		return new SuccessResult();
	}

	private Result checkPasswordConfirmation(String password, String passwordConfirmation) {
		if(!password.equals(passwordConfirmation)) {
			return new ErrorResult(messageService.getMessage(Messages.passwordConfirmationError));
		}
		return new SuccessResult();
	}

}
