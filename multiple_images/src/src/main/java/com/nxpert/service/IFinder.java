package com.nxpert.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IFinder<T> {
	
	/**
	 * Pagination 
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(Pageable pageable);
	
	/**
	 * Pagination 
	 * @param entity
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(T entity, Pageable pageable);
	
	/**
	 * Pagination
	 * @param pageable
	 * @return Page<T>
	 */
	Page<T> findAll(Specification<T> specification, Pageable pageable);

	/**
	 * Get elements with specifications                                                  
	 * @param specification
	 * @return List<T> 
	 */
	List<T> findAll(Specification<T> specification);

	/**
	 * Get All elements
	 * @return List<T>
	 */
	List<T> findAll();
}
