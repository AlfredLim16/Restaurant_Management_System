package model;

/**
 *
 * @author admin
 */
public class OrderItem {

    private String id;
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(String id, MenuItem menuItem, int quantity){
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public MenuItem getMenuItem(){
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem){
        this.menuItem = menuItem;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public double getSubtotal(){
        return menuItem.getPrice() * quantity;
    }
}
