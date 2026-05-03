package service;

import exception.InsufficientInventoryException;
import exception.ValidationException;
import storage.IInventoryItem;
import model.InventoryItem;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author admin
 */
public class InventoryService {

    private final IInventoryItem _inventoryItem;

    public InventoryService(IInventoryItem inventoryItem){
        this._inventoryItem = inventoryItem;
    }

    public InventoryItem addInventoryItem(String itemName, String category, int quantity, String unit, double costPerUnit, int reorderLevel, String supplier, LocalDate expiryDate) throws ValidationException{
        ensureNotEmpty(itemName, "Item name");
        ensureNotEmpty(category, "Category");
        ensureNotNegative(quantity, "Quantity");
        ensureNotEmpty(unit, "Unit");
        ensureNotNegative(costPerUnit, "Cost per unit");
        ensureNotNegative(reorderLevel, "Reorder level");
        ensureNotEmpty(supplier, "Supplier");

        InventoryItem newItem = new InventoryItem();
        newItem.setItemName(itemName);
        newItem.setCategory(category);
        newItem.setQuantity(quantity);
        newItem.setUnit(unit);
        newItem.setCostPerUnit(costPerUnit);
        newItem.setReorderLevel(reorderLevel);
        newItem.setSupplier(supplier);
        newItem.setLastRestocked(LocalDate.now());
        newItem.setExpiryDate(expiryDate);

        _inventoryItem.create(newItem);
        return newItem;
    }

    public void updateQuantity(int inventoryId, int newQuantity) throws ValidationException{
        InventoryItem existingItem = getInventoryItemOrThrow(inventoryId);
        ensureNotNegative(newQuantity, "New quantity");

        existingItem.setQuantity(newQuantity);
        _inventoryItem.update(existingItem);
    }

    public void updateReorderLevel(int inventoryId, int newReorderLevel) throws ValidationException{
        InventoryItem existingItem = getInventoryItemOrThrow(inventoryId);
        ensureNotNegative(newReorderLevel, "Reorder level");

        existingItem.setReorderLevel(newReorderLevel);
        _inventoryItem.update(existingItem);
    }

    public void deleteInventoryItem(int inventoryId) throws ValidationException{
        InventoryItem existingItem = getInventoryItemOrThrow(inventoryId);
        _inventoryItem.delete(inventoryId);
    }

    public void consumeInventory(int inventoryId, int quantityToConsume) throws ValidationException, InsufficientInventoryException{
        InventoryItem existingItem = getInventoryItemOrThrow(inventoryId);
        ensureNotNegative(quantityToConsume, "Quantity used");

        if(existingItem.getQuantity() < quantityToConsume){
            throw new InsufficientInventoryException("Insufficient inventory for " + existingItem.getItemName() + ". Available: " + existingItem.getQuantity() + ", Required: " + quantityToConsume);
        }
        existingItem.setQuantity(existingItem.getQuantity() - quantityToConsume);
        _inventoryItem.update(existingItem);
    }

    public void restockInventory(int inventoryId, int quantityToAdd) throws ValidationException{
        InventoryItem existingItem = getInventoryItemOrThrow(inventoryId);
        ensurePositive(quantityToAdd, "Quantity added");

        existingItem.setQuantity(existingItem.getQuantity() + quantityToAdd);
        existingItem.setLastRestocked(LocalDate.now());
        _inventoryItem.update(existingItem);
    }

    public boolean isLowStock(int inventoryId){
        InventoryItem existingItem = _inventoryItem.get(inventoryId);
        if(existingItem == null){
            return false;
        }
        return existingItem.getReorderLevel() > existingItem.getQuantity();
    }

    public boolean isExpiringSoon(int inventoryId, int daysThreshold){
        InventoryItem existingItem = _inventoryItem.get(inventoryId);
        if(existingItem == null || existingItem.getExpiryDate() == null){
            return false;
        }
        long daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), existingItem.getExpiryDate());
        return daysThreshold > daysUntilExpiry;
    }

    public double calculateItemValue(InventoryItem inventoryItem){
        return inventoryItem.getQuantity() * inventoryItem.getCostPerUnit();
    }

    public double calculateTotalInventoryValue(){
        double totalInventoryValue = 0.0;
        for(InventoryItem currentItem : _inventoryItem.getAll()){
            totalInventoryValue += calculateItemValue(currentItem);
        }
        return totalInventoryValue;
    }

    public long getDaysUntilExpiry(int inventoryIdentifier){
        InventoryItem existingItem = _inventoryItem.get(inventoryIdentifier);
        if(existingItem == null || existingItem.getExpiryDate() == null){
            return Long.MAX_VALUE;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), existingItem.getExpiryDate());
    }

    public ArrayList<InventoryItem> getAllInventoryItems(){
        return new ArrayList<>(_inventoryItem.getAll());
    }

    public ArrayList<InventoryItem> getLowStockItems(){
        return new ArrayList<>(_inventoryItem.findLowStock());
    }

    public ArrayList<InventoryItem> getItemsExpiringSoon(int daysThreshold){
        return new ArrayList<>(_inventoryItem.findExpiringSoon(daysThreshold));
    }

    public ArrayList<InventoryItem> getItemsByCategory(String targetCategory){
        return new ArrayList<>(_inventoryItem.findByCategory(targetCategory));
    }

    public int getLowStockCount(){
        return _inventoryItem.findLowStock().size();
    }

    public ArrayList<InventoryItem> getItemsNeedingReorder(){
        ArrayList<InventoryItem> itemsNeedingReorder = new ArrayList<>();
        for(InventoryItem item : _inventoryItem.getAll()){
            if(item.getQuantity() <= item.getReorderLevel()){
                itemsNeedingReorder.add(item);
            }
        }

        for(int i = 0;i < itemsNeedingReorder.size() - 1;i++){
            int minIndex = i;
            for(int j = i + 1;j < itemsNeedingReorder.size();j++){
                if(itemsNeedingReorder.get(j).getQuantity() < itemsNeedingReorder.get(minIndex).getQuantity()){
                    minIndex = j;
                }
            }
            InventoryItem temp = itemsNeedingReorder.get(i);
            itemsNeedingReorder.set(i, itemsNeedingReorder.get(minIndex));
            itemsNeedingReorder.set(minIndex, temp);
        }

        return itemsNeedingReorder;
    }

    public HashMap<String, ArrayList<InventoryItem>> groupByCategory(ArrayList<InventoryItem> inventoryItemList){
        HashMap<String, ArrayList<InventoryItem>> itemsGroupedByCategory = new HashMap<>();
        for(InventoryItem currentItem : inventoryItemList){
            String itemCategory = currentItem.getCategory();
            putIntoGroupedMap(itemsGroupedByCategory, itemCategory, currentItem);
        }
        return itemsGroupedByCategory;
    }

    private InventoryItem getInventoryItemOrThrow(int inventoryId) throws ValidationException{
        InventoryItem retrievedItem = _inventoryItem.get(inventoryId);
        if(retrievedItem == null){
            throw new ValidationException("Inventory item not found");
        }
        return retrievedItem;
    }

    private void ensureNotEmpty(String valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck == null || valueToCheck.trim().isEmpty()){
            throw new ValidationException(fieldName + " is required");
        }
    }

    private void ensureNotNegative(int valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck < 0){
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }

    private void ensureNotNegative(double valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck < 0){
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }

    private void ensurePositive(int valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck <= 0){
            throw new ValidationException(fieldName + " must be greater than 0");
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
