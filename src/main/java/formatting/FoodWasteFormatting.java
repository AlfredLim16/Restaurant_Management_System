package formatting;

import model.FoodWaste;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author admin
 */
public class FoodWasteFormatting {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public String formatWasteDate(FoodWaste waste){
        if(waste.getWasteDate() == null){
            return "";
        }
        return waste.getWasteDate().format(DATE_FORMATTER);
    }

    public String formatQuantity(FoodWaste waste){
        return waste.getQuantity() + " " + waste.getUnit();
    }

    public String formatCost(FoodWaste waste){
        return String.format("%.2f", waste.getEstimatedCost());
    }
}
