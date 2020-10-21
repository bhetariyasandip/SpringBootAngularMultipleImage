package com.nxpert.service;

import com.nxpert.entity.Image;


public interface ImageService extends IFinder<Image> , IService<Image>{

	Image saveImagePathUrl( String image_path, String url, String string);


	
}
