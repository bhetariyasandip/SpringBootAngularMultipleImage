package com.nxpert.service;

import com.nxpert.entity.Images;


public interface ImagesService extends IFinder<Images> , IService<Images>{

	Images saveImagePathUrl( String image_path, String url, String string, String smallImagePath, String smallUrl, String smallImageName);


	
}
