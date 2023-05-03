import com.hp.gagawa.java.elements.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WebServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext root = server.createContext("/");
        root.setHandler(WebServer::handleRequest);

        HttpContext user = server.createContext("/users");
        user.setHandler(WebServer::handleRequestOneUser);

        HttpContext context = server.createContext("/users/all");
        context.setHandler(WebServer::handleRequestAllUser);

        HttpContext product = server.createContext("/products");
        product.setHandler(WebServer::handleRequestOneProduct);

        HttpContext allProducts = server.createContext("/products/all");
        allProducts.setHandler(WebServer::handleRequestAllProducts);

        HttpContext order = server.createContext("/orders");
        order.setHandler(WebServer::handleRequestOneOrder);

        HttpContext allOrders = server.createContext("/orders/all");
        allOrders.setHandler(WebServer::handleRequestAllOrders);


        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Online shopping web server") );
        head.appendChild( title );

        Body body = new Body();

        H1 h1 = new H1();
        h1.appendChild( new Text("Online Shopping Web Page") );
        body.appendChild( h1 );

        P para = new P();

        A link1 = new A("/products/all", "/products/all");
        link1.appendText("Product list");

        para.appendChild(link1);
        para.appendChild(new Br());

        A link2 = new A("/orders/all", "/orders/all");
        link2.appendText("Order list");

        para.appendChild(link2);
        para.appendChild(new Br());

        A link3 = new A("/users/all", "/users/all");
        link3.appendText("User list");

        para.appendChild(link3);
        para.appendChild(new Br());

        body.appendChild(para);
        html.appendChild( body );
        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllUser(HttpExchange exchange) throws IOException {
        String url = "jdbc:sqlite:store.db";
        SQLiteDataAdapter dao = new SQLiteDataAdapter();
        dao.connect(url);
        List<UserModel> list = dao.loadAllUsers();

        Html html = new Html();
        Head head = new Head();
        html.appendChild(head);

        Title title = new Title();
        title.appendChild(new Text("User Page Server"));
        head.appendChild(title);

        Body body = new Body();
        html.appendChild(body);

        H1 h1 = new H1();
        h1.appendChild(new Text("User Page"));
        body.appendChild(h1);

        P para = new P();
        para.appendChild(new Text("The server time is " + LocalDateTime.now()));
        body.appendChild(para);

        para = new P();
        para.appendChild(new Text("The server has " + list.size() + " users."));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th();
        header.appendText("User ID");
        row.appendChild(header);
        header = new Th();
        header.appendText("Username");
        row.appendChild(header);
        header = new Th();
        header.appendText("Password");
        row.appendChild(header);
        header = new Th();
        header.appendText("Display Name");
        row.appendChild(header);
        header = new Th();
        header.appendText("Manager Status");
        row.appendChild(header);
        table.appendChild(row);

        for (UserModel loadUser : list) {
            row = new Tr();
            Td cell = new Td();
            cell.appendText(String.valueOf(loadUser.userID));
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(loadUser.userName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(loadUser.password);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(loadUser.displayName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(String.valueOf(loadUser.isManager));
            row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestOneUser(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        String username = uri.substring(uri.lastIndexOf('/') + 1);

        System.out.println(username);


        String url = "jdbc:sqlite:store.db";

        SQLiteDataAdapter dao = new SQLiteDataAdapter();

        dao.connect(url);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("One User Page Server") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("One User Page") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        UserModel users = dao.loadUser(Integer.parseInt(username));

        if (users != null) {

            para = new P();
            para.appendText("User ID:" + users.userID);
            html.appendChild(para);
            para = new P();
            para.appendText("Username:" + users.userName);
            html.appendChild(para);
            para = new P();
            para.appendText("Password:" + users.password);
            html.appendChild(para);
            para = new P();
            para.appendText("Display Name:" + users.displayName);
            html.appendChild(para);
            para = new P();
            para.appendText("Manager:" + users.isManager);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("User not found");
            html.appendChild(para);
        }

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();


        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllProducts(HttpExchange exchange) throws IOException {

        String url = "jdbc:sqlite:store.db";

        SQLiteDataAdapter dao = new SQLiteDataAdapter();

        dao.connect(url);

        List<ProductModel> list = dao.loadAllProducts();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Product Page Server") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Product Page") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " products." ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("ProductID"); row.appendChild(header);
        header = new Th(); header.appendText("Product name"); row.appendChild(header);
        header = new Th(); header.appendText("Price"); row.appendChild(header);
        header = new Th(); header.appendText("Quantity"); row.appendChild(header);
        table.appendChild(row);

        for (ProductModel product : list) {
            row = new Tr();
            Td cell = new Td(); cell.appendText(String.valueOf(product.productID)); row.appendChild(cell);
            cell = new Td(); cell.appendText(product.name); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.price)); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.quantity)); row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();


        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private static void handleRequestOneProduct(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));

        System.out.println(id);


        String url = "jdbc:sqlite:store.db";

        SQLiteDataAdapter dao = new SQLiteDataAdapter();

        dao.connect(url);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("One Product Page Server") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("One Product Page") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        ProductModel product = dao.loadProduct(id);

        if (product != null) {

            para = new P();
            para.appendText("ProductID:" + product.productID);
            html.appendChild(para);
            para = new P();
            para.appendText("Product name:" + product.name);
            html.appendChild(para);
            para = new P();
            para.appendText("Price:" + product.price);
            html.appendChild(para);
            para = new P();
            para.appendText("Quantity:" + product.quantity);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private static void handleRequestAllOrders(HttpExchange exchange) throws IOException {

        String url = "jdbc:sqlite:store.db";

        SQLiteDataAdapter dao = new SQLiteDataAdapter();

        dao.connect(url);

        List<OrderModel> list = dao.loadAllOrders();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Order Page Server") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Order Page") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " orders." ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("Order ID"); row.appendChild(header);
        header = new Th(); header.appendText("Date"); row.appendChild(header);
        header = new Th(); header.appendText("Customer"); row.appendChild(header);
        header = new Th(); header.appendText("Total Cost"); row.appendChild(header);
        header = new Th(); header.appendText("Total Tax"); row.appendChild(header);
        table.appendChild(row);

        for (OrderModel order : list) {
            row = new Tr();
            Td cell = new Td(); cell.appendText(String.valueOf(order.orderID)); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.date)); row.appendChild(cell);
            cell = new Td(); cell.appendText(order.customer); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.totalCost)); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(order.totalTax)); row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();

        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestOneOrder(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));

        System.out.println(id);


        String url = "jdbc:sqlite:store.db";

        SQLiteDataAdapter dao = new SQLiteDataAdapter();

        dao.connect(url);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("One Order Page Server") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("One Order Page") );
        body.appendChild( h1 );

        P para = new P();
        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
        body.appendChild(para);

        OrderModel order = dao.loadOrder(id);

        if (order != null) {

            para = new P();
            para.appendText("Order ID:" + order.orderID);
            html.appendChild(para);
            para = new P();
            para.appendText("Date:" + order.date);
            html.appendChild(para);
            para = new P();
            para.appendText("Customer name:" + order.customer);
            html.appendChild(para);
            para = new P();
            para.appendText("Total tax:" + order.totalCost);
            html.appendChild(para);
            para = new P();
            para.appendText("Total cost:" + order.totalTax);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Order not found");
            html.appendChild(para);
        }

        para = new P();
        A link1 = new A("/", "/");
        link1.appendText("Return Home");
        para.appendChild(link1);
        para.appendChild(new Br());
        body.appendChild(para);

        String response = html.write();


        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
