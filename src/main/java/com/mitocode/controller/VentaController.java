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

import com.mitocode.dto.VentaDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Venta;
import com.mitocode.service.IVentaService;

@RestController
@RequestMapping("/Ventas")
public class VentaController {
	
	@Autowired
	private IVentaService service;

	@GetMapping
	public ResponseEntity<List<Venta>> listar(){
		List<Venta> lista = service.listar();
		return new ResponseEntity<List<Venta>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Venta> listarPorId(@PathVariable("id") Integer id) {
		Venta pel = service.listarPorId(id);
		if(pel.getIdVenta() == null) {
			throw new ModeloNotFoundException("ID NO EXISTE: " + id);
		}
		return new ResponseEntity<Venta>(pel, HttpStatus.OK);
	}
	
	//Spring Boot 2.1 | Hateoas 0.9
	/*@GetMapping(value = "/{id}")
	public Resource<Venta> listarPorId(@PathVariable("id") Integer id){
		
		Venta pel = service.listarPorId(id);
		if(pel.getIdVenta() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		Resource<Venta> resource = new Resource<Venta>(pel);
		// /Ventas/{4}
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Venta-resource"));
		
		return resource;
	}*/
	
	@GetMapping("/hateoas/{id}")
	//Spring Boot 2.2 | Hateoas 1
	public EntityModel<Venta> listarPorIdHateoas(@PathVariable("id") Integer id){
		
		Venta pel = service.listarPorId(id);
		if(pel.getIdVenta() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		EntityModel<Venta> resource = new EntityModel<Venta>(pel);
		// /Ventas/{4}
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("Venta-resource"));
		
		return resource;
	}
	
	/*@PostMapping
	public ResponseEntity<Venta> registrar(@Valid @RequestBody Venta obj) {
		Venta pel = service.registrar(obj);
		return new ResponseEntity<Venta>(pel, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody VentaDTO obj) {
		Venta pel = service.registrarTransaccional(obj);
		
		// localhost:8080/Ventas/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pel.getIdVenta()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Venta> modificar(@Valid @RequestBody Venta obj) {
		Venta pel = service.modificar(obj);
		return new ResponseEntity<Venta>(pel, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
