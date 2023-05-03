import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    public int orderID;
    public String customer;
    public double totalCost;
    public double totalTax;
    public Date date;

    public List<OrderLineModel> lines;
    public OrderModel() {
        lines = new ArrayList<>();
    }
}