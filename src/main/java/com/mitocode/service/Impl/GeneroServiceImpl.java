package com.mitocode.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Genero;
import com.mitocode.repo.IGeneroRepo;
import com.mitocode.service.IGeneroService;

@Service
public class GeneroServiceImpl implements IGeneroService {
	
	@Autowired
	private IGeneroRepo repo;
	
	@Override
	public Genero registrar(Genero obj) {
		return repo.save(obj);
	}

	@Override
	public Genero modificar(Genero obj) {
		return repo.save(obj);
	}

	@Override
	public List<Genero> listar() {
		return repo.findAll();
	}

	@Override
	public Genero listarPorId(Integer v) {
		Optional<Genero> op = repo.findById(v);
		return op.isPresent() ? op.get() : new Genero();
	}

	@Override
	public void eliminar(Integer v) {
		repo.deleteById(v);
		
	}

}
