package storage;

import model.FoodWaste;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class InMemoryFoodWaste extends AbstractInMemory<FoodWaste> implements IFoodWaste {

    private final ArrayList<FoodWaste> dateRangeWasteRecords = new ArrayList<>();
    private final ArrayList<FoodWaste> matchingWasteRecords = new ArrayList<>();
    private final ArrayList<FoodWaste> categoryWasteRecords = new ArrayList<>();

    public InMemoryFoodWaste(){

    }

    @Override
    protected int getEntityId(FoodWaste foodWasteRecord){
        return foodWasteRecord.getWasteId();
    }

    @Override
    protected void setEntityId(FoodWaste foodWasteRecord, int wasteId){
        foodWasteRecord.setWasteId(wasteId);
    }

    @Override
    public ArrayList<FoodWaste> findByDateRange(LocalDate startDate, LocalDate endDate){
        dateRangeWasteRecords.clear();
        for(FoodWaste currentRecord : storage.values()){
            LocalDate wasteDate = currentRecord.getWasteDate().toLocalDate();
            if(!wasteDate.isBefore(startDate) && !wasteDate.isAfter(endDate)){
                dateRangeWasteRecords.add(currentRecord);
            }
        }
        return dateRangeWasteRecords;
    }

    @Override
    public ArrayList<FoodWaste> findByReason(String targetReason){
        matchingWasteRecords.clear();
        for(FoodWaste currentRecord : storage.values()){
            if(currentRecord.getReason().equals(targetReason)){
                matchingWasteRecords.add(currentRecord);
            }
        }
        return matchingWasteRecords;
    }

    @Override
    public ArrayList<FoodWaste> findByCategory(String targetCategory){
        categoryWasteRecords.clear();
        for(FoodWaste currentRecord : storage.values()){
            if(currentRecord.getCategory().equals(targetCategory)){
                categoryWasteRecords.add(currentRecord);
            }
        }
        return categoryWasteRecords;
    }
}
