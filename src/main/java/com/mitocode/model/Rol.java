package com.mitocode.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rol")
public class Rol {
	
	@Id
	private Integer IdRol;
	
	@Column(name = "nombre", length = 10)
	private String nombre;
	
	@Column(name = "descripcion", length = 25)
	private String descripcion;

}
