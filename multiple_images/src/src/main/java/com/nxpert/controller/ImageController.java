package com.nxpert.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.nxpert.entity.Images;
import com.nxpert.model.FileInfo;
import com.nxpert.model.UploadFileResponse;
import com.nxpert.service.FileStorageService;
import com.nxpert.service.ImagesService;

@RestController
@CrossOrigin("*")
public class ImageController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Value("${file.image-dir}")
	private String imageDir;

	@Autowired
	private ImagesService imageService; 


	@PostMapping("/uploadImages")
	public List<UploadFileResponse> uploadImages(@RequestParam("file") MultipartFile[] file) throws Exception {

		List<UploadFileResponse> uploadFileResponseList = new ArrayList<>();
		for (MultipartFile data : file) {
			Images img = fileStorageService.storeFile(data, imageDir);
			uploadFileResponseList.add(new UploadFileResponse(img.getImage_name(), img.getImage_url(),data.getContentType(), data.getSize()));
		}
		return uploadFileResponseList;

		/*String url = fileStorageService.storeFile(file, inventoryId, imageDir);

		String extention = FilenameUtils.getExtension(StringUtils.cleanPath(file.getOriginalFilename()));
		return new UploadFileResponse(inventoryId+"."+extention, url,
				file.getContentType(), file.getSize());*/
	}

	
	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = new ArrayList<>();
		imageService.findAll().forEach(data -> {
			
			FileInfo  fileInfo= FileInfo.builder().id(data.getId()).name(data.getImage_name()).url(data.getImage_url())
					.smallName(data.getSmall_image_name()).smallUrl(data.getSmall_image_url()).build();
			fileInfos.add(fileInfo);
		});

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}


	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		//		Resource file = storageService.load(filename);
		Resource file = fileStorageService.loadFileAsResource(filename, imageDir) ;
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}


	@DeleteMapping("/deleteFile/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Optional<Images> db = imageService.findById(id);
		if(!db.isPresent()){
			return new ResponseEntity<String>("Id not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		imageService.deleteById(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
