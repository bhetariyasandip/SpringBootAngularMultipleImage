package com.nxpert.serviceImpl;

import org.springframework.stereotype.Service;

import com.nxpert.entity.Images;
import com.nxpert.repository.ImagesRepository;
import com.nxpert.service.BasicService;
import com.nxpert.service.ImagesService;

@Service
public class ImagesServiceImpl extends BasicService<Images, ImagesRepository> implements ImagesService{

	@Override
	public Images saveImagePathUrl(String image_path, String url, String fileName, String smallImagePath, String smallUrl, String smallImageName) {
		// TODO Auto-generated method stub
		Images img = Images.builder().image_name(fileName).image_path(image_path).image_url(url)
				.small_image_name(smallImageName).small_image_path(smallImagePath).small_image_url(smallUrl)
				.build();
		return repository.save(img);
	}


}
