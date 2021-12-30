package com.etiya.rentACar.business.concretes;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.etiya.rentACar.business.abstracts.CarService;
import com.etiya.rentACar.business.abstracts.MessageService;
import com.etiya.rentACar.business.constants.messages.Messages;
import com.etiya.rentACar.core.utilities.results.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.etiya.rentACar.business.abstracts.CarImageService;
import com.etiya.rentACar.business.dtos.CarImageSearchListDto;
import com.etiya.rentACar.business.request.carImageRequests.CreateCarImageRequest;
import com.etiya.rentACar.business.request.carImageRequests.DeleteCarImageRequest;
import com.etiya.rentACar.business.request.carImageRequests.UpdateCarImageRequest;
import com.etiya.rentACar.core.utilities.business.BusinessRules;
import com.etiya.rentACar.core.utilities.helpers.FileHelper;
import com.etiya.rentACar.core.utilities.helpers.FileHelperManager;
import com.etiya.rentACar.core.utilities.mapping.ModelMapperService;
import com.etiya.rentACar.dataAccess.abstracts.CarImageDao;
import com.etiya.rentACar.entities.CarImage;

@Service
public class CarImageManager implements CarImageService {

	private CarImageDao carImageDao;
	private ModelMapperService modelMapperService;
	private CarService carService;
	private MessageService messageService;
	private Environment environment;
	FileHelper fileHelper = new FileHelperManager();

	@Autowired
	public CarImageManager(CarImageDao carImageDao, ModelMapperService modelMapperService, CarService carService,
						   MessageService messageService, Environment environment) {
		super();
		this.carImageDao = carImageDao;
		this.modelMapperService = modelMapperService;
		this.carService = carService;
		this.messageService = messageService;
		this.environment = environment;
	}

	@Override
	public List<CarImageSearchListDto> getCarImages() {
		List<CarImage> list = this.carImageDao.findAll();	
		List<CarImageSearchListDto> response = list.stream()
				.map(carImage -> modelMapperService.forDto().map(carImage, CarImageSearchListDto.class))
				.collect(Collectors.toList());

		return response;
	}

	@Override
	public Result save(CreateCarImageRequest createCarImageRequest) throws Exception {
		
		Result result = BusinessRules.run(checkCarImageCount(createCarImageRequest.getCarId()),
				carService.checkExistingCar(createCarImageRequest.getCarId()), checkIfAnyPhotoIsImported(createCarImageRequest));

		if (result != null) {
			return result;
		}
		CarImage carImage = modelMapperService.forRequest().map(createCarImageRequest, CarImage.class);
		String carImageName = fileHelper
				.saveImage(createCarImageRequest.getCarId(), createCarImageRequest.getMultipartFile()).getMessage();
		Date dateNow = new java.sql.Date(new java.util.Date().getTime());
		carImage.setDate(dateNow);
		carImage.setImagePath(
				fileHelper.returnFilePath(createCarImageRequest.getCarId()).getMessage() + "\\" + carImageName);
		this.carImageDao.save(carImage);
		return new SuccessResult(messageService.getMessage(Messages.addCarImage));
	}

	@Override
	public Result delete(DeleteCarImageRequest deleteCarImageRequest) {
		CarImage carImage = this.carImageDao.getById(deleteCarImageRequest.getImageId());
		String toBeDeletedPath = carImage.getImagePath();
		fileHelper.deleteImage(toBeDeletedPath);
		this.carImageDao.delete(carImage);
		return new SuccessResult(messageService.getMessage(Messages.deleteCarImage));
	}

	@Override
	public Result update(UpdateCarImageRequest updateCarImageRequest) throws IOException {
		CarImage carImage = this.carImageDao.getById(updateCarImageRequest.getImageId());
		int id = carImage.getCar().getCarId();
		fileHelper.deleteImage(carImage.getImagePath());
		String carImageName = fileHelper.saveImage(id, updateCarImageRequest.getMultipartFile()).getMessage();
		Date dateNow = new java.sql.Date(new java.util.Date().getTime());
		carImage.setDate(dateNow);
		carImage.setImagePath(
				fileHelper.returnFilePath(updateCarImageRequest.getCarId()).getMessage() + "\\" + carImageName);
		this.carImageDao.save(carImage);
		return new SuccessResult(messageService.getMessage(Messages.updateCarImage));
	}

	public DataResult<List<CarImageSearchListDto>> getCarImageByCarId(int carId) {
		Result result1 = BusinessRules.run(carService.checkExistingCar(carId));
		if (result1 != null){
			return new ErrorDataResult<List<CarImageSearchListDto>>(null,
					messageService.getMessage(Messages.carNotFound));
		}
		Result resultCheck = BusinessRules.run(checkIfThereIsNoPicture(carId));
		if (resultCheck != null) {
			List<CarImage> carImages = new ArrayList<CarImage>();
			CarImage carImage1 = new CarImage();
			carImage1.setImagePath(environment.getProperty("default.image.path"));
			carImage1.setCar(carService.getCarAsElementByCarId(carId));
			carImages.add(carImage1);
			List<CarImageSearchListDto> list1 = carImages.stream()
					.map(carImage -> modelMapperService.forDto().map(carImage, CarImageSearchListDto.class))
					.collect(Collectors.toList());
			return new SuccessDataResult<List<CarImageSearchListDto>>(list1);
		}
		List<CarImage> result = this.carImageDao.getByCar_CarId(carId);
		List<CarImageSearchListDto> list = result.stream()
				.map(carImage -> modelMapperService.forDto().map(carImage, CarImageSearchListDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<CarImageSearchListDto>>(list); 
	}

	private Result checkCarImageCount(int carId) {
		File file = new File(environment.getProperty("image.subfolder") + carId);
		if (file.exists()) {
			int numberOfFiles = file.listFiles().length;
			if (numberOfFiles >= 5) {
				return new ErrorResult(messageService.getMessage(Messages.upperImageLimit));
			}
		}
		return new SuccessResult();
	}

	private Result checkIfThereIsNoPicture(int carId) {
		File file = new File( environment.getProperty("image.subfolder") + carId);
		if ((file.exists() && file.listFiles().length == 0) || !file.exists()) {
				return new ErrorResult();
		}
		return new SuccessResult();
	}

	@Override
	public List<CarImage> getCarImageListByCarId(int carId) {
		return this.carImageDao.getByCar_CarId(carId);
	}

	private Result checkIfAnyPhotoIsImported(CreateCarImageRequest createCarImageRequest){
		if (createCarImageRequest.getMultipartFile() == null){
			return new ErrorResult(messageService.getMessage(Messages.noImage));
		}
		return new SuccessResult();
	}
}
