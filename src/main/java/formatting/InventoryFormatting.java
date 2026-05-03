package formatting;

import model.InventoryItem;

/**
 *
 * @author admin
 */
public class InventoryFormatting {

    private static final String LOW_STOCK = " LOW STOCK";
    private static final String OK = "OK";
    private static final String EXPIRED = "EXPIRED";
    private static final String EXPIRES_TODAY = "EXPIRES TODAY";

    public String formatItemValue(double value){
        return String.format("%.2f", value);
    }

    public String formatCostPerUnit(InventoryItem item){
        return String.format("%.2f", item.getCostPerUnit()) + "/" + item.getUnit();
    }

    public String formatQuantity(InventoryItem item){
        return item.getQuantity() + " " + item.getUnit();
    }

    public String formatStockStatus(boolean isLowStock){
        return isLowStock ? LOW_STOCK : OK;
    }

    public String formatExpiryStatus(long daysUntilExpiry){
        if(daysUntilExpiry < 0){
            return EXPIRED;
        }
        if(daysUntilExpiry == 0){
            return EXPIRES_TODAY;
        }
        if(daysUntilExpiry <= 3){
            return "EXPIRES SOON (" + daysUntilExpiry + " days)";
        }
        return daysUntilExpiry + "days remaining";
    }
}
