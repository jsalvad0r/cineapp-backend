package com.mitocode.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Cliente;
import com.mitocode.repo.IClienteRepo;
import com.mitocode.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteRepo repo;
	
	@Override
	public Cliente registrar(Cliente obj) {
		return repo.save(obj);
	}

	@Override
	public Cliente modificar(Cliente obj) {
		return repo.save(obj);
	}

	@Override
	public List<Cliente> listar() {
		return repo.findAll();
	}

	@Override
	public Cliente listarPorId(Integer v) {
		Optional<Cliente> op = repo.findById(v);
		return op.isPresent() ? op.get() : new Cliente();
	}

	@Override
	public void eliminar(Integer v) {
		repo.deleteById(v);
		
	}

}
