package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import models.Producto;
import services.CrudProductos;

// Maneja las peticiones a /api/productos y /api/productos/{id}
public class ProductoHandler implements HttpHandler {

    private final CrudProductos crud;

    public ProductoHandler(CrudProductos crud) {
        this.crud = crud;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            // /api/productos
            if (path.equals("/api/productos")) {
                if (metodo.equals("GET")) {
                    listar(exchange);
                    return;
                }
                if (metodo.equals("POST")) {
                    crear(exchange);
                    return;
                }
                enviarError(exchange, 405, "Método no permitido.");
                return;
            }

            // /api/productos/{id}
            if (path.startsWith("/api/productos/")) {
                String idTexto = path.substring("/api/productos/".length());
                int id;
                try {
                    id = Integer.parseInt(idTexto);
                } catch (NumberFormatException e) {
                    enviarError(exchange, 400, "Id inválido.");
                    return;
                }

                if (metodo.equals("GET")) {
                    obtener(exchange, id);
                    return;
                }
                if (metodo.equals("PUT")) {
                    actualizar(exchange, id);
                    return;
                }
                if (metodo.equals("DELETE")) {
                    eliminar(exchange, id);
                    return;
                }
                enviarError(exchange, 405, "Método no permitido.");
                return;
            }

            enviarError(exchange, 404, "Ruta no encontrada.");

        } catch (IllegalArgumentException e) {
            enviarError(exchange, 400, e.getMessage());
        } catch (Exception e) {
            enviarError(exchange, 500, "Error interno del servidor.");
        }
    }

    private void listar(HttpExchange exchange) throws IOException {
        List<Producto> productos = crud.listarProductos();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < productos.size(); i++) {
            if (i > 0) json.append(",");
            json.append(productos.get(i).toJson());
        }
        json.append("]");

        enviarJson(exchange, 200, json.toString());
    }

    private void obtener(HttpExchange exchange, int id) throws IOException {
        Producto producto = crud.buscarProducto(id);
        if (producto == null) {
            enviarError(exchange, 404, "Producto no encontrado.");
            return;
        }
        enviarJson(exchange, 200, producto.toJson());
    }

    private void crear(HttpExchange exchange) throws IOException {
        String body = leerCuerpo(exchange);

        String nombre = JsonUtil.getString(body, "nombre");
        Double precio = JsonUtil.getNumber(body, "precio");
        Double stock = JsonUtil.getNumber(body, "stock");
        String categoria = JsonUtil.getString(body, "categoria");

        Producto producto = crud.crearProducto(
                nombre,
                precio == null ? -1 : precio,
                stock == null ? -1 : stock.intValue(),
                categoria
        );

        enviarJson(exchange, 201, producto.toJson());
    }

    private void actualizar(HttpExchange exchange, int id) throws IOException {
        String body = leerCuerpo(exchange);

        String nombre = JsonUtil.getString(body, "nombre");
        Double precio = JsonUtil.getNumber(body, "precio");
        Double stock = JsonUtil.getNumber(body, "stock");
        String categoria = JsonUtil.getString(body, "categoria");

        Producto producto = crud.actualizarProducto(
                id,
                nombre,
                precio == null ? -1 : precio,
                stock == null ? -1 : stock.intValue(),
                categoria
        );

        enviarJson(exchange, 200, producto.toJson());
    }

    private void eliminar(HttpExchange exchange, int id) throws IOException {
        Producto producto = crud.eliminarProducto(id);
        enviarJson(exchange, 200, "{\"mensaje\":\"Producto eliminado\",\"id\":" + producto.getId() + "}");
    }

    private String leerCuerpo(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void enviarJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void enviarError(HttpExchange exchange, int status, String mensaje) throws IOException {
        String json = "{\"error\":\"" + mensaje.replace("\"", "\\\"") + "\"}";
        enviarJson(exchange, status, json);
    }
}
