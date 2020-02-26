package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Cliente;
import com.mitocode.service.IClienteService;

@RestController
@RequestMapping("/Clientes")
public class ClienteController {
	
	@Autowired
	private IClienteService service;

	@GetMapping
	public ResponseEntity<List<Cliente>> listar(){
		List<Cliente> lista = service.listar();
		return new ResponseEntity<List<Cliente>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> listarPorId(@PathVariable("id") Integer id) {
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		return new ResponseEntity<Cliente>(pel, HttpStatus.OK);
	}
	
	//Spring Boot 2.1 | Hateoas 0.9
	/*@GetMapping(value = "/{id}")
	public Resource<Cliente> listarPorId(@PathVariable("id") Integer id){
		
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		Resource<Cliente> resource = new Resource<Cliente>(pel);
		// /Clientes/{4}
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Cliente-resource"));
		
		return resource;
	}*/
	
	@GetMapping("/hateoas/{id}")
	//Spring Boot 2.2 | Hateoas 1
	public EntityModel<Cliente> listarPorIdHateoas(@PathVariable("id") Integer id){
		
		Cliente pel = service.listarPorId(id);
		if(pel.getIdCliente() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		EntityModel<Cliente> resource = new EntityModel<Cliente>(pel);
		// /Clientes/{4}
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Cliente-resource"));
		
		return resource;
	}
	
	/*@PostMapping
	public ResponseEntity<Cliente> registrar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.registrar(obj);
		return new ResponseEntity<Cliente>(pel, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.registrar(obj);
		
		// localhost:8080/Clientes/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pel.getIdCliente()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Cliente> modificar(@Valid @RequestBody Cliente obj) {
		Cliente pel = service.modificar(obj);
		return new ResponseEntity<Cliente>(pel, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
