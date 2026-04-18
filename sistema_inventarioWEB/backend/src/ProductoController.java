import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ProductoController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if ("GET".equalsIgnoreCase(method)) {
            listarProductos(exchange);
        } else if ("POST".equalsIgnoreCase(method)) {
            insertarProducto(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void listarProductos(HttpExchange exchange) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[");

        String sql = "SELECT idProducto, nombreProducto, costo, estado, marca " +
                "FROM admin_tablas.producto ORDER BY idProducto";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            boolean primero = true;

            while (rs.next()) {
                if (!primero) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"idProducto\":").append(rs.getInt("idProducto")).append(",")
                        .append("\"nombreProducto\":\"").append(escapeJson(rs.getString("nombreProducto")))
                        .append("\",")
                        .append("\"costo\":").append(rs.getDouble("costo")).append(",")
                        .append("\"estado\":\"").append(escapeJson(rs.getString("estado"))).append("\",")
                        .append("\"marca\":\"").append(escapeJson(rs.getString("marca"))).append("\"")
                        .append("}");

                primero = false;
            }

            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            enviarRespuesta(exchange, 200, json.append("]").toString());

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            enviarRespuesta(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void insertarProducto(HttpExchange exchange) throws IOException {
        String body = leerBody(exchange);

        String nombreProducto = extraerValor(body, "nombreProducto");
        String costoTexto = extraerValor(body, "costo");
        String estado = extraerValor(body, "estado");
        String marca = extraerValor(body, "marca");

        try (Connection conn = DatabaseConnection.getConnection();
                CallableStatement stmt = conn.prepareCall("{call desa10.insertarProducto(?, ?, ?, ?)}")) {

            stmt.setString(1, nombreProducto);
            stmt.setDouble(2, Double.parseDouble(costoTexto));
            stmt.setString(3, estado);
            stmt.setString(4, marca);

            stmt.execute();

            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            enviarRespuesta(exchange, 200, "{\"mensaje\":\"Producto insertado correctamente\"}");

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            enviarRespuesta(exchange, 500, "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private String leerBody(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void enviarRespuesta(HttpExchange exchange, int statusCode, String respuesta) throws IOException {
        byte[] bytes = respuesta.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private String escapeJson(String valor) {
        if (valor == null)
            return "";
        return valor.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String extraerValor(String json, String clave) {
        String patron = "\"" + clave + "\"";
        int inicioClave = json.indexOf(patron);

        if (inicioClave == -1)
            return "";

        int dosPuntos = json.indexOf(":", inicioClave);
        if (dosPuntos == -1)
            return "";

        int inicioValor = dosPuntos + 1;

        while (inicioValor < json.length() && Character.isWhitespace(json.charAt(inicioValor))) {
            inicioValor++;
        }

        if (inicioValor < json.length() && json.charAt(inicioValor) == '"') {
            int finValor = json.indexOf("\"", inicioValor + 1);
            if (finValor == -1)
                return "";
            return json.substring(inicioValor + 1, finValor);
        } else {
            int finValor = inicioValor;
            while (finValor < json.length() && json.charAt(finValor) != ',' && json.charAt(finValor) != '}') {
                finValor++;
            }
            return json.substring(inicioValor, finValor).trim();
        }
    }
}