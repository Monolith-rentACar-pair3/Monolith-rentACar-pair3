package com.etiya.rentACar.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.constants.messages.BrandMessages;
import com.etiya.rentACar.business.constants.messages.ColorMessages;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.results.ErrorResult;
import com.etiya.rentACar.entities.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.abstracts.ColorService;
import com.etiya.rentACar.business.dtos.ColorSearchListDto;
import com.etiya.rentACar.business.request.colorRequests.CreateColorRequest;
import com.etiya.rentACar.business.request.colorRequests.DeleteColorRequest;
import com.etiya.rentACar.business.request.colorRequests.UpdateColorRequest;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessResult;
import com.etiya.rentACar.dataAccess.abstracts.ColorDao;
import com.etiya.rentACar.entities.Color;

@Service
public class ColorManager implements ColorService{
	
	private ColorDao colorDao;
	private ModelMapperService modelMapperService;
	private Environment environment;
	private MessageService messageService;
	
	@Autowired
	public ColorManager(ColorDao colorDao, ModelMapperService modelMapperService, Environment environment, MessageService messageService) {
		super();
		this.colorDao = colorDao;
		this.modelMapperService = modelMapperService;
		this.environment = environment;
		this.messageService = messageService;
	}


	@Override
	public List<ColorSearchListDto> getColors() {
		
		List<Color> result = this.colorDao.findAll();
		List<ColorSearchListDto> response = result.stream().map(color->modelMapperService.forDto()
				.map(color, ColorSearchListDto.class)).collect(Collectors.toList());
				
		return response;
	}


	@Override
	public Result save(CreateColorRequest createColorRequest) {
		Result result = BusinessRules.run(checkExistingColor(createColorRequest.getColorName()));
		if (result != null){
			return result;
		}
		Color color = modelMapperService.forRequest().map(createColorRequest, Color.class);
		this.colorDao.save(color);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),31));
	}


	@Override
	public Result delete(DeleteColorRequest deleteColorRequest) {
		Result result = BusinessRules.run(checkExistingColorId(deleteColorRequest.getColorId()));
		if (result != null){
			return result;
		}
		Color color = modelMapperService.forRequest().map(deleteColorRequest, Color.class);
		this.colorDao.delete(color);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),32));
	}


	@Override
	public Result update(UpdateColorRequest updateColorRequest) {
		Result result = BusinessRules.run(checkExistingColor(updateColorRequest.getColorName()), checkExistingColorId(updateColorRequest.getColorId()));
		if (result != null){
			return result;
		}
		Color color = modelMapperService.forRequest().map(updateColorRequest, Color.class);
		this.colorDao.save(color);
		return new SuccessResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),33));
	}

	private Result checkExistingColor(String colorName1) {
		String lowerCaseColorName = colorName1.toLowerCase();
		for (Color color : colorDao.findAll()) {
			String lowerCaseExistingColorName = color.getColorName().toLowerCase();
			if(lowerCaseColorName.equals(lowerCaseExistingColorName)) {
				return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),34));
			}
		}
		return new SuccessResult();
	}

	private Result checkExistingColorId(int colorId){
		if (colorDao.existsById(colorId)){
			return new SuccessResult();
		}
		return new ErrorResult(messageService.getMessage(Integer.parseInt(environment.getProperty("language.id")),35));
	}
}
