package com.ems.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ManyToAny;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//LE INDICAMOS COMO VAMOS A GENERAR EL ID 
	private Long id;
	
	@NotEmpty(message = "no puede estar vacio")// no vacio
	@Size(min = 3, max = 12, message = "debe tener entre 3 y 12 caracteres")//max y min nuero de caracteres
	@Column(nullable = false)//NO PUEDE SER NULL
	private String nombre;
	
	@NotEmpty(message = "no puede estar vacio")
	private String apellido; 
	
	@NotEmpty(message = "no puede estar vacio")
	@Email(message = "no es una direccion de correo valida")
	@Column(nullable = false, unique = true)//NO PUEDE SER FALSO Y DEBE SE UNICO
	private String email;
	
	@Column(name = "create_at")
	@NotNull(message = "no puede estar vacio")
	@Temporal(TemporalType.DATE)      //INDICA CUAL VA SER LA TRANFORMACION O EL TIPO DE DATO
	private Date createAt;            //QUE TENDRA EN LA BD, PARA TRANFORMAR EL TIPO DATE DE JAVA CON EL DE MYSQL 
	
	private String foto;
	
	@NotNull(message = "el campo region no puede estar vacio")
	@ManyToOne(fetch = FetchType.LAZY)//muchos clientes en una sola region; fetchType.LAZY carga peresoza
	@JoinColumn(name = "region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})//ignorar esos atribus en el objeto json
	private Region region;
	
	
	//@PrePersist     //antes de persistir asisnamos la fecha
	//public void prePersist() {
	//	createAt = new Date();
	//}
	
	
	//?serverTimezone=UTC
	//org.hibernate.dialect.MySQL57Dialect
	
	 public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
	
	
	
	
	

}
