package service;

import exception.ValidationException;
import storage.IMenuItem;
import storage.IOrder;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author admin
 */
public class OrderService {

    private final IOrder _order;
    private final IMenuItem _menuItem;

    public OrderService(IOrder order, IMenuItem menuItem){
        this._order = order;
        this._menuItem = menuItem;
    }

    public Order createOrder(String tableNumber, ArrayList<OrderItem> orderItems) throws ValidationException{
        ensureNotEmpty(tableNumber, "Table number");
        ensureNotNullNotEmpty(orderItems, "Order items");

        for(OrderItem orderItem : orderItems){
            MenuItem requestedMenuItem = orderItem.getMenuItem();
            ensureMenuItemAvailable(requestedMenuItem, requestedMenuItem.getItemId());
            ensurePositive(orderItem.getQuantity(), "Item quantity");
        }

        double calculatedTotalAmount = calculateOrderTotal(orderItems);
        Order newOrder = new Order();
        newOrder.setTableNumber(tableNumber);
        newOrder.setItems(new ArrayList<>(orderItems));
        newOrder.setStatus(OrderStatus.PREPARING);
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setTotalAmount(calculatedTotalAmount);

        _order.create(newOrder);
        return newOrder;
    }

    public double calculateOrderTotal(ArrayList<OrderItem> orderItems){
        double calculatedTotal = 0.0;
        for(OrderItem orderItem : orderItems){
            MenuItem currentItem = orderItem.getMenuItem();
            if(currentItem != null){
                calculatedTotal += currentItem.getPrice() * orderItem.getQuantity();
            }
        }
        return calculatedTotal;
    }

    public double calculateOrderRevenue(){
        double accumulatedRevenue = 0.0;
        for(Order completedOrder : _order.findByStatus(OrderStatus.COMPLETED)){
            accumulatedRevenue += completedOrder.getTotalAmount();
        }
        return accumulatedRevenue;
    }

    public void updateOrderStatus(int orderIdentifier, String newOrderStatus) throws ValidationException{
        Order existingOrder = getOrderOrThrow(orderIdentifier);
        if(!isValidStatusTransition(existingOrder.getStatus(), newOrderStatus)){
            throw new ValidationException("Invalid status transition from " + existingOrder.getStatus() + " to " + newOrderStatus);
        }
        existingOrder.setStatus(newOrderStatus);
        _order.update(existingOrder);
    }

    public void cancelOrder(int orderIdentifier) throws ValidationException{
        Order existingOrder = getOrderOrThrow(orderIdentifier);
        if(existingOrder.getStatus().equals(OrderStatus.COMPLETED)){
            throw new ValidationException("Cannot cancel completed order");
        }
        existingOrder.setStatus(OrderStatus.CANCELLED);
        _order.update(existingOrder);
    }

    public long getActiveOrderCount(){
        long activeOrderCounter = 0;
        for(Order currentOrder : _order.getAll()){
            if(!OrderStatus.COMPLETED.equals(currentOrder.getStatus()) && !OrderStatus.CANCELLED.equals(currentOrder.getStatus())){
                activeOrderCounter++;
            }
        }
        return activeOrderCounter;
    }

    public int getTotalItemCount(Order restaurantOrder){
        if(restaurantOrder.getItems() == null){
            return 0;
        }
        int totalItemQuantity = 0;
        for(OrderItem orderItem : restaurantOrder.getItems()){
            totalItemQuantity += orderItem.getQuantity();
        }
        return totalItemQuantity;
    }

    public int getTotalCompletedOrders(){
        return _order.findByStatus(OrderStatus.COMPLETED).size();
    }

    public ArrayList<MenuItem> getAvailableMenuItems(){
        return new ArrayList<>(_menuItem.findAvailable());
    }

    private boolean isValidStatusTransition(String currentOrderStatus, String proposedNewStatus){
        if(currentOrderStatus.equals(OrderStatus.PREPARING)){
            return proposedNewStatus.equals(OrderStatus.READY) || proposedNewStatus.equals(OrderStatus.CANCELLED);
        }else if(currentOrderStatus.equals(OrderStatus.READY)){
            return proposedNewStatus.equals(OrderStatus.SERVED) || proposedNewStatus.equals(OrderStatus.CANCELLED);
        }else if(currentOrderStatus.equals(OrderStatus.SERVED)){
            return proposedNewStatus.equals(OrderStatus.COMPLETED);
        }
        return false;
    }

    public HashMap<String, ArrayList<Order>> groupByStatus(ArrayList<Order> orderList){
        HashMap<String, ArrayList<Order>> ordersGroupedByStatus = new HashMap<>();
        for(Order currentOrder : orderList){
            String orderStatus = currentOrder.getStatus();
            putIntoGroupedMap(ordersGroupedByStatus, orderStatus, currentOrder);
        }
        return ordersGroupedByStatus;
    }

    public HashMap<String, ArrayList<Order>> groupByTable(ArrayList<Order> orderList){
        HashMap<String, ArrayList<Order>> ordersGroupedByTable = new HashMap<>();
        for(Order currentOrder : orderList){
            String tableNumber = currentOrder.getTableNumber();
            putIntoGroupedMap(ordersGroupedByTable, tableNumber, currentOrder);
        }
        return ordersGroupedByTable;
    }

    public ArrayList<Order> getAllOrders(){
        return new ArrayList<>(_order.getAll());
    }

    public ArrayList<Order> getOrdersByStatus(String targetStatus){
        return new ArrayList<>(_order.findByStatus(targetStatus));
    }

    public ArrayList<Order> getOrdersByTable(String targetTableNumber){
        return new ArrayList<>(_order.findByTable(targetTableNumber));
    }

    private Order getOrderOrThrow(int orderIdentifier) throws ValidationException{
        Order retrievedOrder = _order.get(orderIdentifier);
        if(retrievedOrder == null){
            throw new ValidationException("Order not found");
        }
        return retrievedOrder;
    }

    private void ensureNotEmpty(String valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck == null || valueToCheck.trim().isEmpty()){
            throw new ValidationException(fieldName + " is required");
        }
    }

    private void ensureNotNullNotEmpty(ArrayList<?> listToCheck, String fieldName) throws ValidationException{
        if(listToCheck == null || listToCheck.isEmpty()){
            throw new ValidationException(fieldName + " must contain at least one item");
        }
    }

    private void ensurePositive(int valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck <= 0){
            throw new ValidationException(fieldName + " must be greater than 0");
        }
    }

    private void ensureMenuItemAvailable(MenuItem menuItemToValidate, int itemIdentifier) throws ValidationException{
        if(menuItemToValidate == null){
            throw new ValidationException("Menu item with ID " + itemIdentifier + " does not exist");
        }
        if(!menuItemToValidate.isAvailable()){
            throw new ValidationException("Menu item " + menuItemToValidate.getName() + " is not available");
        }
    }

    private <MapKeyType, ListValueType> void putIntoGroupedMap(HashMap<MapKeyType, ArrayList<ListValueType>> targetMap, MapKeyType key, ListValueType value){
        ArrayList<ListValueType> existingList = targetMap.get(key);
        if(existingList == null){
            existingList = new ArrayList<>();
            targetMap.put(key, existingList);
        }
        existingList.add(value);
    }
}
