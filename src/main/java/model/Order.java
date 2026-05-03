package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class Order {

    private int orderId;
    private String tableNumber;
    private String status;
    private LocalDateTime orderTime;
    private ArrayList<OrderItem> items;
    private double totalAmount;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(int orderId, String tableNumber, String status, LocalDateTime orderTime, ArrayList<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.status = status;
        this.orderTime = orderTime;
        this.items = items != null ? items : new ArrayList<>();
        this.totalAmount = totalAmount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
