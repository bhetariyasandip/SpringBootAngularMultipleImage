package com.nxpert.serviceImpl;

import org.springframework.stereotype.Service;

import com.nxpert.entity.Image;
import com.nxpert.repository.ImageRepository;
import com.nxpert.service.BasicService;
import com.nxpert.service.ImageService;

@Service
public class ImageServiceImpl extends BasicService<Image, ImageRepository> implements ImageService{

	@Override
	public Image saveImagePathUrl(String image_path, String url, String fileName) {
		// TODO Auto-generated method stub
		Image img = Image.builder().image_name(fileName).image_path(image_path).image_url(url).build();
		return repository.save(img);
	}


}
