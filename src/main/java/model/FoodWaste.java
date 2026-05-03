package model;

import java.time.LocalDateTime;

/**
 *
 * @author admin
 */
public class FoodWaste {

    private int wasteId;
    private String itemName;
    private double quantity;
    private String unit;
    private String reason;
    private double estimatedCost;
    private LocalDateTime wasteDate;
    private String recordedBy;
    private String category;

    public FoodWaste(){

    }

    public FoodWaste(int wasteId, String itemName, double quantity, String unit, String reason, double estimatedCost, LocalDateTime wasteDate, String recordedBy, String category){
        this.wasteId = wasteId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.reason = reason;
        this.estimatedCost = estimatedCost;
        this.wasteDate = wasteDate;
        this.recordedBy = recordedBy;
        this.category = category;
    }

    public int getWasteId(){
        return wasteId;
    }

    public void setWasteId(int wasteId){
        this.wasteId = wasteId;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public double getQuantity(){
        return quantity;
    }

    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    public String getUnit(){
        return unit;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getReason(){
        return reason;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public double getEstimatedCost(){
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost){
        this.estimatedCost = estimatedCost;
    }

    public LocalDateTime getWasteDate(){
        return wasteDate;
    }

    public void setWasteDate(LocalDateTime wasteDate){
        this.wasteDate = wasteDate;
    }

    public String getRecordedBy(){
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy){
        this.recordedBy = recordedBy;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }
}
