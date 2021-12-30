package com.etiya.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.constants.messages.UserMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.abstracts.UserService;
import com.etiya.rentACar.business.dtos.UserSearchListDto;
import com.etiya.rentACar.business.request.userRequests.CreateUserRequest;
import com.etiya.rentACar.business.request.userRequests.DeleteUserRequest;
import com.etiya.rentACar.business.request.userRequests.UpdateUserRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.DataResult;
import com.etiya.rentACar.core.utilities.results.ErrorDataResult;
import com.etiya.rentACar.core.utilities.results.ErrorResult;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessDataResult;
import com.etiya.rentACar.core.utilities.results.SuccessResult;
import com.etiya.rentACar.dataAccess.abstracts.UserDao;
import com.etiya.rentACar.entities.User;

@Service
public class UserManager implements UserService {
	
	private UserDao userDao;
	
	private ModelMapperService modelMapperService;
	private Environment environment;
	private MessageService messageService;

	@Autowired
	public UserManager(UserDao userDao, ModelMapperService modelMapperService, Environment environment, MessageService messageService) {
		super();
		this.userDao = userDao;
		this.modelMapperService = modelMapperService;
		this.environment = environment;
		this.messageService = messageService;
	}

	@Override
	public DataResult<List<UserSearchListDto>> getAll() {
		List<User> result = this.userDao.findAll();
		List<UserSearchListDto> response = result.stream().map(user -> modelMapperService.forDto()
				.map(user, UserSearchListDto.class)).collect(Collectors.toList());
		return new SuccessDataResult<List<UserSearchListDto>>(response);
	}

	@Override
	public Result existsByEmail(String email) {
		if (this.userDao.existsByeMail(email)) {
			return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),29));
		}
		return new SuccessResult();
	}

	@Override
	public DataResult<User> getByEmail(String email) {
		if(this.userDao.existsByeMail(email)) {
			return new SuccessDataResult<User>(this.userDao.getByeMail(email));	
		}
		return new ErrorDataResult<User>(null);
	}

	@Override
	public User getById(int userId) {
		return userDao.getById(userId);
	}

	@Override
	public Result existsById(int id) {
		if (this.userDao.existsById(id)){
			return new SuccessResult();
		}
		return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),30));
	}

}
