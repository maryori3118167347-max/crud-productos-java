package models;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;

    public Producto(int id, String nombre, double precio, int stock, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getCategoria() {
        return categoria;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Nombre: " + nombre +
                " | Precio: $" + precio +
                " | Stock: " + stock +
                " | Categoría: " + categoria;
    }

    // Convierte el producto a una cadena JSON, escapando comillas del texto
    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"nombre\":\"" + escapar(nombre) + "\","
                + "\"precio\":" + precio + ","
                + "\"stock\":" + stock + ","
                + "\"categoria\":\"" + escapar(categoria) + "\""
                + "}";
    }

    private String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
