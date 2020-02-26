package com.mitocode.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.dto.VentaDTO;
import com.mitocode.model.Venta;
import com.mitocode.repo.IVentaComidaRepo;
import com.mitocode.repo.IVentaRepo;
import com.mitocode.service.IVentaService;

@Service
public class VentaServiceImpl implements IVentaService {
	
	@Autowired
	private IVentaRepo repo;
	
	@Autowired
	private IVentaComidaRepo vcRepo;
	
	@Override
	public Venta registrar(Venta obj) {
		return repo.save(obj);
	}
	
    @Transactional
	@Override
	
	public Venta registrarTransaccional(VentaDTO ventaDto) {
		ventaDto.getVenta().getDetalle().forEach(det -> {
			det.setVenta(ventaDto.getVenta());
		});
		
		repo.save(ventaDto.getVenta());
		ventaDto.getLstComidas().forEach(c -> {
			vcRepo.registrar(ventaDto.getVenta().getIdVenta(), c.getIdComida());
		});
		
		return ventaDto.getVenta();
	}


	@Override
	public Venta modificar(Venta obj) {
		return repo.save(obj);
	}

	@Override
	public List<Venta> listar() {
		return repo.findAll();
	}

	@Override
	public Venta listarPorId(Integer v) {
		Optional<Venta> op = repo.findById(v);
		return op.isPresent() ? op.get() : new Venta();
	}

	@Override
	public void eliminar(Integer v) {
		repo.deleteById(v);
		
	}

}
