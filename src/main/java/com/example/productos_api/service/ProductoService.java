package com.example.productos_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.productos_api.model.Producto;
import com.example.productos_api.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listar() {
        return repo.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public Producto guardar(Producto producto) {
        return repo.save(producto);
    }

    public void asignarCategoria(Long id, String categoria) {
        repo.assignCategoria(id, categoria);
    }
}

