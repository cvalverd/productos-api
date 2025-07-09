package com.example.productos_api.repository;

@Repository
public class ProductoRepository {
    private final Map<Long, Producto> productos = new HashMap<>();
    private long nextId = 1;

    @PostConstruct
    public void init() {
        save(new Producto(null, "Laptop", "HP Pavilion", "Tecnología"));
        save(new Producto(null, "Café", "Tostado premium", "Alimentos"));
    }

    public List<Producto> findAll() {
        return new ArrayList<>(productos.values());
    }

    public Optional<Producto> findById(Long id) {
        return Optional.ofNullable(productos.get(id));
    }

    public Producto save(Producto producto) {
        if (producto.getId() == null) {
            producto.setId(nextId++);
        }
        productos.put(producto.getId(), producto);
        return producto;
    }

    public void assignCategoria(Long id, String categoria) {
        productos.get(id).setCategoria(categoria);
    }
}
