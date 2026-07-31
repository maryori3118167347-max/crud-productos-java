import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import services.CrudProductos;
import web.ProductoHandler;
import web.StaticFileHandler;

public class Main {

    public static void main(String[] args) throws IOException {

        int puerto = 8080;
        CrudProductos crud = new CrudProductos();

        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);

        servidor.createContext("/api/productos", new ProductoHandler(crud));
        servidor.createContext("/", new StaticFileHandler("public"));

        servidor.setExecutor(null);
        servidor.start();

        System.out.println("Servidor corriendo en http://localhost:" + puerto);
    }
}
