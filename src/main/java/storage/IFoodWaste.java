package storage;

import model.FoodWaste;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public interface IFoodWaste {

    void create(FoodWaste waste);
    FoodWaste get(int wasteId);
    ArrayList<FoodWaste> getAll();
    void update(FoodWaste waste);
    void delete(int wasteId);

    ArrayList<FoodWaste> findByDateRange(LocalDate startDate, LocalDate endDate);
    ArrayList<FoodWaste> findByReason(String reason);
    ArrayList<FoodWaste> findByCategory(String category);
}
