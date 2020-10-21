package com.nxpert.service;

import java.util.List;
import java.util.Optional;

public interface IService<T>{
	
	T save(T entity);
	
	List<T> saveAll(Iterable<T> entities);

	Optional<T> findById(Long id);

	Long deleteById(Long id);
}
