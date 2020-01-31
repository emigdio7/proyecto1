package com.ems.springboot.backend.apirest.controllers;


import java.io.IOException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ems.springboot.backend.apirest.models.entity.Cliente;
import com.ems.springboot.backend.apirest.models.entity.Region;
import com.ems.springboot.backend.apirest.models.services.IClienteService;
import com.ems.springboot.backend.apirest.models.services.IUploadService;



@CrossOrigin(origins = {"http://localhost:4200"})//para que se pueda conectar con el cliente angular
@RestController
@RequestMapping("/api")//endpoint
public class ClienteRestController {
   
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadService uploadService;
	

	
	@GetMapping("/clientes")//endpoint
	public List<Cliente> index(){
		return clienteService.findAll(); 
	}
	
	
	@GetMapping("/clientes/page/{page}")//endpoint
	public Page<Cliente> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);//pasamos el numero de pagina y el tamaño
		return clienteService.findAll(pageable);
	}
	
	
	
	
    @GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {//tipo ? porque puede retornar cualquir cosa
    	Cliente cliente = null;
    	HashMap<String, Object> response = new HashMap<>();//mapa para retornar una coleccion de mensajes
    	 
		try {
			cliente = clienteService.findById(id);
			
		} catch (DataAccessException e) { //DataAccesException propio de spring para manejar excepciones con la BD
			 response.put("mensaje", "error al realizar la consulta en la bade la datos");//agregamos al mapa los Objetos mensajes
			 response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			 return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR); //creamos un nuevo objeto ResponseEntity para la respuesta que recibe dos parametros
		}
		if (cliente==null) { 
			response.put("mensaje", "El cliente con Id: ".concat(id.toString()).concat(" no se encuentra en la base de datos"));
	        return new ResponseEntity<HashMap<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);//si existe retornamos el cliente y el status Ok
	}
    
    
    
    @PostMapping("/clientes")
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {//@Valid para aplicar la validacion
    	Cliente newCliente = null;                                 // BindigResult es un objeto que contiene todos los mensajes de error y nos sirve para ver si ocurrio un erro
    	Map<String, Object> response= new HashMap<>();
    	
    	if(result.hasErrors()) {//verificamos si contiene errores
    		List<String> errors = new ArrayList<>();
    		for(FieldError err:   result.getFieldErrors()) {//iteramos por cada error y lo agregamos ala lista
    			errors.add("El campo ' "+err.getField()+" '"+ err.getDefaultMessage());
    		}
    		  
    			response.put("errors", errors);	
    		 return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    	}  
    	
    	try {
    		newCliente = clienteService.save(cliente);
    	
			
		} catch (DataAccessException e) {
			 response.put("mensaje", "error al realizar la insercion en la base de datos");
			 response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			 return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
    	response.put("mensaje", "El cliente se creo con exito!");
    	response.put("cliente", newCliente);
    	
    	return new ResponseEntity<Map<String, Object>>(response , HttpStatus.OK);
    	
     }
          
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteUpdate = null;
		Map<String, Object> response = new HashMap<>();  

		Cliente clienteActual = clienteService.findById(id);
		if (clienteActual == null) {
			response.put("mensaje", "No se puedo editar: No exite el cliente en la bade la datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try {
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());

			clienteUpdate = clienteService.save(clienteActual);
 
		} catch (DataAccessException e) {
			response.put("mensaje", "error al actualizar el cliente en la base la datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("mensaje", "El cliente se actualizo con exito!");
		response.put("cliente", clienteUpdate);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
        
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id) {
    	Map<String, Object> response = new HashMap<>(); 
    	
    	try {
    		//antes de eliminar el cliente tambien tenemos que elimnar la foto
    		Cliente cliente = clienteService.findById(id);
    		String nombreFotoAnterior = cliente.getFoto();
    		
     		uploadService.eliminarFoto(nombreFotoAnterior);
    		clienteService.delete(id);
    		
		} catch (DataAccessException e) {
			 response.put("mensaje", "error al eliminar el clinte de la bade la datos");
			 response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			 return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	response.put("mensaje", "Cliente elimino con exito!");
		 return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    	
    	
    }
    
    @PostMapping("clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
    	
    	String nombreArhivo = null;
    	Map<String, Object> response = new HashMap<>();
         	
         	Cliente cliente = clienteService.findById(id);
         	if(!archivo.isEmpty()) {
         		
         		try {
         			nombreArhivo = uploadService.copiar(archivo);
					
					
				} catch (IOException e) {
					
					response.put("mensaje", "error al subir la imagen la base la datos");
					response.put("error", e.getMessage().concat(" : ").concat(e.getCause().getMessage()));
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

				}   
         		//antes de guardar la foto vamos a validar si el clinete ya tiene foto
         		String nombreFotoAnterior = cliente.getFoto();
         		
         		uploadService.eliminarFoto(nombreFotoAnterior);
         	
				cliente.setFoto(nombreArhivo);
         		clienteService.save(cliente);
         		response.put("cliente", cliente);
         		response.put("mensaje", "La imagen se ha subido con extio: "+nombreArhivo);
         	}
    	 return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    	
    }
    
    @GetMapping("upload/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable("nombreFoto") String nombreFoto){
   
    Resource recurso = null;
          try {
        	  recurso = uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			
			e.printStackTrace();
		}
           HttpHeaders cabecera = new HttpHeaders();
           cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename()+ "\""); //para forzar la descarga
		return new ResponseEntity<Resource>(recurso, cabecera,HttpStatus.OK);
				}
    
    
    @GetMapping("/clientes/regiones")
    public List<Region> mostrarRegiones(){
    	return clienteService.findAllRegiones();
    }
      
    
    
    
    
}
