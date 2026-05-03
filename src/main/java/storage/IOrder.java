package storage;

import model.Order;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public interface IOrder {

    void create(Order order);
    Order get(int orderId);
    ArrayList<Order> getAll();
    void update(Order order);
    void delete(int orderId);

    ArrayList<Order> findByStatus(String status);
    ArrayList<Order> findByTable(String tableNumber);
}
