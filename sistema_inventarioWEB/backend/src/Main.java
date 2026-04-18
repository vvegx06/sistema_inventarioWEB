
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/productos", new ProductoController());
            server.setExecutor(null);
            server.start();

            System.out.println("Servidor iniciado en http://localhost:8080");
        } catch (IOException e) {
        }
    }
}
