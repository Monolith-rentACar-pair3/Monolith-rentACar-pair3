package com.etiya.rentACar.core.utilities.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.etiya.rentACar.business.constants.paths.Paths;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import com.etiya.rentACar.core.utilities.results.Result;
import com.etiya.rentACar.core.utilities.results.SuccessResult;



public class FileHelperManager implements FileHelper {

	private Environment environment;

	public FileHelperManager(Environment environment) {
		this.environment = environment;
	}

	@Override
	public Result saveImage(int carId, MultipartFile multipartFile) throws IOException {
		createNewCarImageFolder(carId);
		String newFolderName = "car" + carId;
		String newImageName = createImageName(multipartFile).getMessage();
		File file = new File("img\\" + newFolderName + "\\" + newImageName);
		file.createNewFile();
		FileOutputStream os = new FileOutputStream(file);
		os.write(multipartFile.getBytes());
		os.close();
		return new SuccessResult(newImageName);
	}

	@Override
	public Result deleteImage(String imagePath) {
		if (!imagePath.isEmpty()) {
		File file = new File(imagePath);
		file.delete();
		}
		return new SuccessResult();
	}
	
	private Result createNewCarImageFolder(int carId) {
		String newFolderName = "car" + carId;
		File newFolder = new File("img\\" + newFolderName);
				newFolder.mkdir();
		return new SuccessResult(newFolderName);
	}
	private Result createImageName(MultipartFile file) {
		String newImageName = java.util.UUID.randomUUID().toString() + "" +"."
				+ file.getContentType().toString().substring(file.getContentType().indexOf("/") + 1);
		return new SuccessResult(newImageName);
	}
	public Result returnFilePath(int carId) {
		String path = this.environment.getProperty(Paths.IMAGESUBFOLDER) + carId;
		return new SuccessResult(path);
	}

}
