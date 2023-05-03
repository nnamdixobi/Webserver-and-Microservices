import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;

    @Override
    public void connect(String url) {
        try {
            // db parameters
            // create a connection to the database
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            if (conn == null)
                System.out.println("Cannot make the connection!!!");
            else
                System.out.println("The connection object is " + conn);

            System.out.println("Connection to SQLite has been established.");

            /* Test data!!!
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            while (rs.next())
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
            */

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();

            if (loadProduct(product.productID) == null) {           // this is a new product!
                stmt.execute("INSERT INTO Product(productID, name, price, quantity) VALUES ("
                        + product.productID + ","
                        + '\'' + product.name + '\'' + ","
                        + product.price + ","
                        + product.quantity + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Product SET "
                        + "productID = " + product.productID + ","
                        + "name = " + '\'' + product.name + '\'' + ","
                        + "price = " + product.price + ","
                        + "quantity = " + product.quantity +
                        " WHERE productID = " + product.productID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product WHERE ProductID = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public List<ProductModel> loadAllProducts() {
        List<ProductModel> list = new ArrayList<>();
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product ");
            while (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.name = rs.getString(2);
                product.price = rs.getDouble(3);
                product.quantity = rs.getDouble(4);
                list.add(product);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public OrderModel loadOrder(int orderID) {
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders WHERE OrderID = " + orderID);
            if (rs.next()) {
                order = new OrderModel();
                order.orderID = rs.getInt(1);
                order.customer = rs.getString(3);
                order.totalCost = rs.getDouble(4);
                order.totalTax = rs.getDouble(5);
                order.date = rs.getDate(2);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }

    public List<OrderModel> loadAllOrders() {
        List<OrderModel> list = new ArrayList<>();
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders ");
            while (rs.next()) {
                order = new OrderModel();
                order.orderID = rs.getInt(1);
                order.customer = rs.getString(3);
                order.totalCost = rs.getDouble(4);
                order.totalTax = rs.getDouble(5);
                order.date = rs.getDate(2);
                list.add(order);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public List<UserModel> loadAllUsers() {
        List<UserModel> list = new ArrayList<>();
        UserModel user = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User ");
            while (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt(1);
                user.userName = rs.getString(2);
                user.password = rs.getString(3);
                user.displayName = rs.getString(4);
                user.isManager = rs.getBoolean(5);
                list.add(user);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateProductPrice(int int1, double double1) {
        return false;
    }

    @Override
    public boolean updateProductQuantity(int productID, int quantity) {
        return false;
    }

    @Override
    public boolean checkValidUser(String userName, String password) {
        return false;
    }

    @Override
    public boolean checkOrderDelete(String userName, int orderID) {
        return false;
    }

    public UserModel loadUser(int userID) {
        UserModel user = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE UserID = " + userID);
            if (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt(1);
                user.userName = rs.getString(2);
                user.password = rs.getString(3);
                user.displayName = rs.getString(4);
                user.isManager = rs.getBoolean(5);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public void saveUser(UserModel user) {
        try {
            Statement stmt = conn.createStatement();

            if (loadUser(user.userID) == null) { // this is a new user!
                stmt.execute("INSERT INTO User(userID, userName, password, displayName, isSeller) VALUES ("
                        + user.userID + ","
                        + '\'' + user.userName + '\'' + ","
                        + '\'' + user.password + '\'' + ","
                        + '\'' + user.displayName + '\'' + ","
                        + user.isManager +
                        ")"
                );
            } else {
                stmt.executeUpdate("UPDATE User SET "
                        + "userID = " + user.userID + ","
                        + "userName = " + '\'' + user.userName + '\'' + ","
                        + "password = " + '\'' + user.password + '\'' + ","
                        + "displayName = " + '\'' + user.displayName + '\'' + ","
                        + "isManager = " + user.isManager +
                        " WHERE userID = " + user.userID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void saveOrder(OrderModel order) {
        try {
            Statement stmt = conn.createStatement();

            if (loadOrder(order.orderID) == null) {           // this is a new order!
                stmt.execute("INSERT INTO Order(OrderID, CustomerName, TotalCost, TotalTax, Date) VALUES ("
                        + order.orderID + ","
                        + '\'' + order.customer + '\'' + ","
                        + order.totalCost + ","
                        + order.totalTax + ","
                        + '\'' + order.date.toString() + '\'' + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Order SET "
                        + "OrderID = " + order.orderID + ","
                        + "CustomerName = " + '\'' + order.customer + '\'' + ","
                        + "TotalCost = " + order.totalCost + ","
                        + "TotalTax = " + order.totalTax + ","
                        + "Date = " + '\'' + order.date.toString() + '\'' +
                        " WHERE OrderID = " + order.orderID
                );

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean checkUser(String username, String password) {
        boolean result = false;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM User WHERE UserName = '" + username + "' AND Password = '" + password + "'");
            if (rs.next()) {
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }





}
