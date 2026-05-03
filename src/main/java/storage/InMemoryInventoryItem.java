package storage;

import model.InventoryItem;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class InMemoryInventoryItem extends AbstractInMemory<InventoryItem> implements IInventoryItem {
    
    private final ArrayList<InventoryItem> lowStockItems = new ArrayList<>();
    private final ArrayList<InventoryItem> matchingItems = new ArrayList<>();
    private final ArrayList<InventoryItem> expiringItems = new ArrayList<>();
    
    public InMemoryInventoryItem(){
        sampleInventoryData();
    }

    private void sampleInventoryData(){
        create(new InventoryItem(0, "Chicken Breast", "Meat", 50, "kg", 30.00, 20, "Fresh Farms", LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    @Override
    protected int getEntityId(InventoryItem inventoryItem){
        return inventoryItem.getInventoryId();
    }

    @Override
    protected void setEntityId(InventoryItem inventoryItem, int inventoryId){
        inventoryItem.setInventoryId(inventoryId);
    }

    @Override
    public ArrayList<InventoryItem> findLowStock(){
        lowStockItems.clear();
        for(InventoryItem currentItem : storage.values()){
            if(currentItem.getQuantity() <= currentItem.getReorderLevel()){
                lowStockItems.add(currentItem);
            }
        }
        return lowStockItems;
    }

    @Override
    public ArrayList<InventoryItem> findByCategory(String targetCategory){
        matchingItems.clear();
        for(InventoryItem currentItem : storage.values()){
            if(currentItem.getCategory().equals(targetCategory)){
                matchingItems.add(currentItem);
            }
        }
        return matchingItems;
    }

    @Override
    public ArrayList<InventoryItem> findExpiringSoon(int daysThreshold){
        expiringItems.clear();
        LocalDate thresholdDate = LocalDate.now().plusDays(daysThreshold);
        for(InventoryItem currentItem : storage.values()){
            if(currentItem.getExpiryDate() != null && !currentItem.getExpiryDate().isAfter(thresholdDate)){
                expiringItems.add(currentItem);
            }
        }
        return expiringItems;
    }
}
