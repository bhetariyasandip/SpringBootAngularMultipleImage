package com.nxpert.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nxpert.entity.Image;
import com.nxpert.model.FileStorageException;
import com.nxpert.model.MyFileNotFoundException;



@Service
public class FileStorageService {

	@Value("${file.image-dir}")
    private String imageDir;
	
	public static final String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

	@Autowired
	private ImageService imageService; 
    
    @Autowired
    public FileStorageService(Environment environment) {

        try {
            Files.createDirectories(Paths.get(environment.getProperty("file.image-dir")));
//            Files.createDirectories(Paths.get(environment.getProperty("file.coin-image-dir")));
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Image storeFile(MultipartFile file, String pathValue) throws Exception {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extention = FilenameUtils.getExtension(fileName);
        
        try {
            if(fileName.contains("..") ) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            fileName = sdf.format(new Date());
            Path targetLocation = Paths.get(pathValue).resolve(fileName+"."+extention);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileName+"."+extention)
                    .toUriString();
            
            
            String image_path =targetLocation.toString();
            return imageService.saveImagePathUrl( image_path, url, fileName+"."+extention);
            
//            return url;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName,String dirpath) {
        try {
            Path filePath = Paths.get(dirpath).resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    
}
