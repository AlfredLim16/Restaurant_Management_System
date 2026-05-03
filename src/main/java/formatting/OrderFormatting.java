package formatting;

import model.Order;
import model.OrderItem;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.JComboBox;

/**
 *
 * @author admin
 */
public class OrderFormatting {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final String NO_ITEMS = "No items";
    private static final Set<String> VALID_STATUSES = Set.of("Preparing", "Ready", "Served", "Completed", "Cancelled");

    public String formatOrderTime(Order order){
        if(order.getOrderTime() == null){
            return "";
        }
        return order.getOrderTime().format(TIME_FORMATTER);
    }

    public String formatOrderDate(Order order){
        if(order.getOrderTime() == null){
            return "";
        }
        return order.getOrderTime().format(DATE_FORMATTER);
    }

    public JComboBox<String> formatOrderItemsCombo(Order order){
        if(order.getItems() == null || order.getItems().isEmpty()){
            return new JComboBox<>(new String[]{NO_ITEMS});
        }

        ArrayList<String> formattedItems = new ArrayList<>();
        for(OrderItem item : order.getItems()){
            String itemName = item.getMenuItem().getName();
            formattedItems.add(item.getQuantity() + "x " + itemName);
        }

        return new JComboBox<>(formattedItems.toArray(new String[0]));
    }

    public String formatTotalAmount(double amount){
        return String.format("%.2f", amount);
    }

    public String formatStatus(String status){
        return VALID_STATUSES.contains(status) ? status : "Unknown";
    }
}
