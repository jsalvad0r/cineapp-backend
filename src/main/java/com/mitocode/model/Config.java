package com.mitocode.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "config")
public class Config {

	@Id
	private Integer idConfig;
	
	@Column(name = "parametro", length = 5)
	private String parametro;

	@Column(name = "valor", length = 25)
	private String valor;
  
}