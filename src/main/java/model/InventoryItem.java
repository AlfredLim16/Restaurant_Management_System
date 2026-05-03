package model;

import java.time.LocalDate;

/**
 *
 * @author admin
 */
public class InventoryItem {

    private int inventoryId;
    private String itemName;
    private String category;
    private int quantity;
    private String unit;
    private double costPerUnit;
    private int reorderLevel;
    private String supplier;
    private LocalDate lastRestocked;
    private LocalDate expiryDate;

    public InventoryItem(){

    }

    public InventoryItem(int inventoryId, String itemName, String category, int quantity, String unit, double costPerUnit, int reorderLevel, String supplier, LocalDate lastRestocked, LocalDate expiryDate){
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
        this.reorderLevel = reorderLevel;
        this.supplier = supplier;
        this.lastRestocked = lastRestocked;
        this.expiryDate = expiryDate;
    }

    public int getInventoryId(){
        return inventoryId;
    }

    public void setInventoryId(int inventoryId){
        this.inventoryId = inventoryId;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public double getCostPerUnit(){
        return costPerUnit;
    }

    public void setCostPerUnit(double costPerUnit){
        this.costPerUnit = costPerUnit;
    }

    public int getReorderLevel(){
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel){
        this.reorderLevel = reorderLevel;
    }

    public String getSupplier(){
        return supplier;
    }

    public void setSupplier(String supplier){
        this.supplier = supplier;
    }

    public LocalDate getLastRestocked(){
        return lastRestocked;
    }

    public void setLastRestocked(LocalDate lastRestocked){
        this.lastRestocked = lastRestocked;
    }

    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate){
        this.expiryDate = expiryDate;
    }
}
