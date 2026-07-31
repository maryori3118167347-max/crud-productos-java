package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

// Sirve los archivos de la carpeta "public" (frontend)
public class StaticFileHandler implements HttpHandler {

    private final Path raiz;

    public StaticFileHandler(String carpetaPublica) {
        this.raiz = Path.of(carpetaPublica).toAbsolutePath().normalize();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/")) {
            path = "/index.html";
        }

        Path archivo = raiz.resolve(path.substring(1)).normalize();

        // Evita salir de la carpeta pública (path traversal)
        if (!archivo.startsWith(raiz) || !Files.exists(archivo) || Files.isDirectory(archivo)) {
            String noEncontrado = "404 - Archivo no encontrado";
            exchange.sendResponseHeaders(404, noEncontrado.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(noEncontrado.getBytes());
            }
            return;
        }

        byte[] contenido = Files.readAllBytes(archivo);
        exchange.getResponseHeaders().add("Content-Type", tipoContenido(archivo.toString()));
        exchange.sendResponseHeaders(200, contenido.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(contenido);
        }
    }

    private String tipoContenido(String nombreArchivo) {
        if (nombreArchivo.endsWith(".html")) return "text/html; charset=UTF-8";
        if (nombreArchivo.endsWith(".css")) return "text/css; charset=UTF-8";
        if (nombreArchivo.endsWith(".js")) return "application/javascript; charset=UTF-8";
        return "application/octet-stream";
    }
}
