import java.util.List;

public interface DataAccess {
    void connect(String str);

    void saveProduct(ProductModel product);
    List<ProductModel> loadAllProducts();
    ProductModel loadProduct(int productID);

    void saveOrder(OrderModel order);
    OrderModel loadOrder(int orderID);
    List<OrderModel> loadAllOrders();

    public boolean checkUser(String username, String password);

    void saveUser(UserModel user);
    UserModel loadUser(int userID);

    List<UserModel> loadAllUsers();

    boolean updateProductPrice(int int1, double double1);

    boolean updateProductQuantity(int productID, int quantity);

    boolean checkValidUser(String userName, String password);

    boolean checkOrderDelete(String userName, int orderID);
}
