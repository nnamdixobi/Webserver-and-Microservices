import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;

public class BasicHttpServer
{

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext context = server.createContext("/products");
        context.setHandler(BasicHttpServer::handleRequest);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        printRequestInfo(exchange);
        String response = "This is the response at " + requestURI;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void printRequestInfo(HttpExchange exchange) {

        System.out.println("-- URI --");
        URI requestURI = exchange.getRequestURI();
        System.out.println(requestURI.toString());

        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();

        for (String key : requestHeaders.keySet()) {
            List<String> value = requestHeaders.get(key);
            System.out.println(" Field " + key);
            System.out.println(" Values " + value);
        }


        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        String query = requestURI.getQuery();
        System.out.println(query);
    }
}