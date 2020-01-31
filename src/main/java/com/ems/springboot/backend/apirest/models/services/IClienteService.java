package com.ems.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ems.springboot.backend.apirest.models.entity.Cliente;
import com.ems.springboot.backend.apirest.models.entity.Region;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable);//recibe por argumento un objeto pageble, 
	                                         //el meotdo retorna un page que es parecido a List solo que atraves de rangos
	
	public Cliente findById(Long id);

	public Cliente save(Cliente cliente);
	
	public void delete(Long id);
	
	public List<Region> findAllRegiones();
	
	
}
