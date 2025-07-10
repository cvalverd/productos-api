package com.example.productos_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.productos_api.model.Producto;
import com.example.productos_api.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<EntityModel<Producto>> listar() {
        List<EntityModel<Producto>> productos = service.listar().stream()
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(ProductoController.class).obtener(p.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("listar")))
            .toList();

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel(),
                linkTo(methodOn(ProductoController.class).crear(null)).withRel("crear"));
    }

    @GetMapping("/{id}")
    public EntityModel<Producto> obtener(@PathVariable Long id) {
        Producto producto = service.obtenerPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("listar"),
                linkTo(methodOn(ProductoController.class).obtenerCategoria(id)).withRel("categoria"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crear(@RequestBody Producto producto) {
        Producto nuevo = service.guardar(producto);

        return ResponseEntity
            .created(linkTo(methodOn(ProductoController.class).obtener(nuevo.getId())).toUri())
            .body(EntityModel.of(nuevo,
                linkTo(methodOn(ProductoController.class).obtener(nuevo.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("listar")));
    }

    @GetMapping("/{id}/categoria")
    public ResponseEntity<Map<String, String>> obtenerCategoria(@PathVariable Long id) {
        Producto producto = service.obtenerPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(Map.of("categoria", producto.getCategoria()));
    }

    @PostMapping("/{id}/categoria")
    public ResponseEntity<Void> asignarCategoria(@PathVariable Long id, @RequestBody Map<String, String> body) {
        service.asignarCategoria(id, body.get("categoria"));
        return ResponseEntity.ok().build();
    }
}

