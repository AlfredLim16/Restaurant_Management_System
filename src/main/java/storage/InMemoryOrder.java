package storage;

import model.Order;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class InMemoryOrder extends AbstractInMemory<Order> implements IOrder {

    private final ArrayList<Order> matchingOrders = new ArrayList<>();
    private final ArrayList<Order> tableOrders = new ArrayList<>();

    public InMemoryOrder(){

    }

    @Override
    protected int getEntityId(Order restaurantOrder){
        return restaurantOrder.getOrderId();
    }

    @Override
    protected void setEntityId(Order restaurantOrder, int orderId){
        restaurantOrder.setOrderId(orderId);
    }

    @Override
    public ArrayList<Order> findByStatus(String targetStatus){
        matchingOrders.clear();
        for(Order currentOrder : storage.values()){
            if(currentOrder.getStatus().equals(targetStatus)){
                matchingOrders.add(currentOrder);
            }
        }
        return matchingOrders;
    }

    @Override
    public ArrayList<Order> findByTable(String targetTableNumber){
        tableOrders.clear();
        for(Order currentOrder : storage.values()){
            if(currentOrder.getTableNumber().equals(targetTableNumber)){
                tableOrders.add(currentOrder);
            }
        }
        return tableOrders;
    }
}
