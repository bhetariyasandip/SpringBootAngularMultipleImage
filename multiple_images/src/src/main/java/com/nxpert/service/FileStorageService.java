package com.nxpert.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nxpert.entity.Images;
import com.nxpert.model.FileStorageException;
import com.nxpert.model.MyFileNotFoundException;



@Service
public class FileStorageService {

	@Value("${file.image-dir}")
    private String imageDir;
	
	public static final String DATE_FORMAT_NOW = "yyyy_MM_dd_HH_mm_ss_SSSSSS";
	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

	@Autowired
	private ImagesService imageService; 
    
    @Autowired
    public FileStorageService(Environment environment) {

        try {
            Files.createDirectories(Paths.get(environment.getProperty("file.image-dir")));
//            Files.createDirectories(Paths.get(environment.getProperty("file.coin-image-dir")));
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Images storeFile(MultipartFile file, String pathValue) throws Exception {
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
           
            ///thumbill
            Path smallTargetLocation = Paths.get(pathValue).resolve(fileName+"_small."+extention);
            File input = new File(pathValue+"/"+fileName+"."+extention);
            BufferedImage image = ImageIO.read(input);
            BufferedImage resized = resize(image, 400, 400);

            File output = new File(smallTargetLocation.toString());
            ImageIO.write(resized, extention, output);
            
            String smallUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/files/")
                    .path(fileName+"_small."+extention)
                    .toUriString();

            return imageService.saveImagePathUrl( image_path, url, fileName+"."+extention,
            		smallTargetLocation.toString(), smallUrl, fileName+"_small."+extention);
            
//            return url;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
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
