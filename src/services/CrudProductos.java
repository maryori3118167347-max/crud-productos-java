package services;

import java.util.ArrayList;
import java.util.List;
import models.Producto;

public class CrudProductos {

    private ArrayList<Producto> productos = new ArrayList<>();
    private int siguienteId = 1;

    // Crea un producto nuevo, generando el id automáticamente
    public Producto crearProducto(String nombre, double precio, int stock, String categoria) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres.");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }

        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        if (precio > 1_000_000) {
            throw new IllegalArgumentException("El precio parece demasiado alto, verifica el valor.");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        Producto producto = new Producto(siguienteId, nombre, precio, stock, categoria);
        siguienteId++;

        productos.add(producto);
        return producto;
    }

    // Leer
    public List<Producto> listarProductos() {
        return productos;
    }

    // Buscar
    public Producto buscarProducto(int id) {

        for (Producto producto : productos) {

            if (producto.getId() == id) {
                return producto;
            }

        }

        return null;
    }

    // Actualizar
    public Producto actualizarProducto(int id, String nombre, double precio, int stock, String categoria) {

        Producto producto = buscarProducto(id);

        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres.");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }

        if (precio < 0 || stock < 0) {
            throw new IllegalArgumentException("El precio y el stock no pueden ser negativos.");
        }

        if (precio > 1_000_000) {
            throw new IllegalArgumentException("El precio parece demasiado alto, verifica el valor.");
        }

        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setCategoria(categoria);

        return producto;
    }

    // Eliminar
    public Producto eliminarProducto(int id) {

        Producto producto = buscarProducto(id);

        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }

        productos.remove(producto);
        return producto;
    }

}