package storage;

import model.InventoryItem;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public interface IInventoryItem {

    void create(InventoryItem item);
    InventoryItem get(int inventoryId);
    ArrayList<InventoryItem> getAll();
    void update(InventoryItem item);
    void delete(int inventoryId);

    ArrayList<InventoryItem> findLowStock();
    ArrayList<InventoryItem> findByCategory(String category);
    ArrayList<InventoryItem> findExpiringSoon(int daysThreshold);
}
