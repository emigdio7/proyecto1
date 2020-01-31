package com.ems.springboot.backend.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ems.springboot.backend.apirest.models.entity.Cliente;
import com.ems.springboot.backend.apirest.models.entity.Region;

public interface IClienteDao extends JpaRepository<Cliente, Long> {
	
	@Query("from Region")
	public List<Region> findAllRegiones();
	
	

}
