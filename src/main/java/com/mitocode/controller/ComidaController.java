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
import com.mitocode.model.Comida;
import com.mitocode.service.IComidaService;

@RestController
@RequestMapping("/comidas")
public class ComidaController {
	
	@Autowired
	private IComidaService service;

	@GetMapping
	public ResponseEntity<List<Comida>> listar(){
		List<Comida> lista = service.listar();
		return new ResponseEntity<List<Comida>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Comida> listarPorId(@PathVariable("id") Integer id) {
		Comida pel = service.listarPorId(id);
		if(pel.getIdComida() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		return new ResponseEntity<Comida>(pel, HttpStatus.OK);
	}
	
	//Spring Boot 2.1 | Hateoas 0.9
	/*@GetMapping(value = "/{id}")
	public Resource<Comida> listarPorId(@PathVariable("id") Integer id){
		
		Comida pel = service.listarPorId(id);
		if(pel.getIdComida() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		Resource<Comida> resource = new Resource<Comida>(pel);
		// /Comidas/{4}
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Comida-resource"));
		
		return resource;
	}*/
	
	@GetMapping("/hateoas/{id}")
	//Spring Boot 2.2 | Hateoas 1
	public EntityModel<Comida> listarPorIdHateoas(@PathVariable("id") Integer id){
		
		Comida pel = service.listarPorId(id);
		if(pel.getIdComida() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		EntityModel<Comida> resource = new EntityModel<Comida>(pel);
		// /Comidas/{4}
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Comida-resource"));
		
		return resource;
	}
	
	/*@PostMapping
	public ResponseEntity<Comida> registrar(@Valid @RequestBody Comida obj) {
		Comida pel = service.registrar(obj);
		return new ResponseEntity<Comida>(pel, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Comida obj) {
		Comida pel = service.registrar(obj);
		
		// localhost:8080/Comidas/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pel.getIdComida()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Comida> modificar(@Valid @RequestBody Comida obj) {
		Comida pel = service.modificar(obj);
		return new ResponseEntity<Comida>(pel, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
