package service;

import exception.ValidationException;
import storage.IFoodWaste;
import model.FoodWaste;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author admin
 */
public class FoodWasteService {

    private final IFoodWaste _foodWaste;

    public FoodWasteService(IFoodWaste foodWaste){
        this._foodWaste = foodWaste;
    }

    public FoodWaste recordWaste(String itemName, double quantity, String unit, String reason, double estimatedCost, String recordedBy, String category) throws ValidationException{
        ensureNotEmpty(itemName, "Item name");
        ensurePositive(quantity, "Quantity");
        ensureNotEmpty(unit, "Unit");
        ensureNotEmpty(reason, "Reason");
        ensureNotNegative(estimatedCost, "Estimated cost");
        ensureNotEmpty(recordedBy, "Recorded by");
        ensureNotEmpty(category, "Category");

        FoodWaste newRecord = new FoodWaste();
        newRecord.setItemName(itemName);
        newRecord.setQuantity(quantity);
        newRecord.setUnit(unit);
        newRecord.setReason(reason);
        newRecord.setEstimatedCost(estimatedCost);
        newRecord.setWasteDate(LocalDateTime.now());
        newRecord.setRecordedBy(recordedBy);
        newRecord.setCategory(category);

        _foodWaste.create(newRecord);
        return newRecord;
    }

    public void updateWaste(int wasteId, String itemName, double quantity, String unit, String reason, double estimatedCost, String category) throws ValidationException{
        FoodWaste existingRecord = getWasteOrThrow(wasteId);

        if(itemName != null && !itemName.trim().isEmpty()){
            existingRecord.setItemName(itemName);
        }else if(unit != null && !unit.trim().isEmpty()){
            existingRecord.setUnit(unit);
        }else if(reason != null && !reason.trim().isEmpty()){
            existingRecord.setReason(reason);
        }else if(category != null && !category.trim().isEmpty()){
            existingRecord.setCategory(category);
        }

        if(quantity > 0){
            existingRecord.setQuantity(quantity);
        }else if(estimatedCost >= 0){
            existingRecord.setEstimatedCost(estimatedCost);
        }
        _foodWaste.update(existingRecord);
    }

    public void deleteWaste(int wasteId) throws ValidationException{
        FoodWaste existingRecord = getWasteOrThrow(wasteId);
        _foodWaste.delete(wasteId);
    }

    public double calculateTotalWasteCost(LocalDate startDate, LocalDate endDate) throws ValidationException{
        ensureDateRange(startDate, endDate);
        double accumulatedWasteCost = 0.0;

        for(FoodWaste currentRecord : _foodWaste.findByDateRange(startDate, endDate)){
            accumulatedWasteCost += currentRecord.getEstimatedCost();
        }
        return accumulatedWasteCost;
    }

    public double calculateDailyWasteCost(){
        LocalDate today = LocalDate.now();
        double dailyWasteCost = 0.0;
        for(FoodWaste currentWasteRecord : _foodWaste.getAll()){
            if(currentWasteRecord.getWasteDate().toLocalDate().equals(today)){
                dailyWasteCost += currentWasteRecord.getEstimatedCost();
            }
        }
        return dailyWasteCost;
    }

    public double calculateMonthlyWasteCost(int year, int month){
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        double monthlyCost = 0.0;
        for(FoodWaste currentRecord : _foodWaste.findByDateRange(startOfMonth, endOfMonth)){
            monthlyCost += currentRecord.getEstimatedCost();
        }
        return monthlyCost;
    }

    public ArrayList<FoodWaste> getAllWasteRecords(){
        return new ArrayList<>(_foodWaste.getAll());
    }

    public ArrayList<FoodWaste> getWasteByDateRange(LocalDate startDate, LocalDate endDate) throws ValidationException{
        ensureDateRange(startDate, endDate);
        return new ArrayList<>(_foodWaste.findByDateRange(startDate, endDate));
    }

    public ArrayList<FoodWaste> getWasteByReason(String targetReason){
        return new ArrayList<>(_foodWaste.findByReason(targetReason));
    }

    public ArrayList<FoodWaste> getWasteByCategory(String targetCategory){
        return new ArrayList<>(_foodWaste.findByCategory(targetCategory));
    }

    public HashMap<String, Long> getWasteCountByReason(){
        HashMap<String, Long> wasteCountByReason = new HashMap<>();
        for(FoodWaste currentRecord : _foodWaste.getAll()){
            String wasteReason = currentRecord.getReason();
            Long currentCount = wasteCountByReason.get(wasteReason);
            if(currentCount == null){
                wasteCountByReason.put(wasteReason, 1L);
            }else{
                wasteCountByReason.put(wasteReason, currentCount + 1);
            }
        }
        return wasteCountByReason;
    }

    public HashMap<String, Double> getWasteCostByCategory(){
        HashMap<String, Double> wasteCostByCategory = new HashMap<>();
        for(FoodWaste currentRecord : _foodWaste.getAll()){
            String wasteCategory = currentRecord.getCategory();
            Double accumulatedCost = wasteCostByCategory.get(wasteCategory);
            if(accumulatedCost == null){
                wasteCostByCategory.put(wasteCategory, currentRecord.getEstimatedCost());
            }else{
                wasteCostByCategory.put(wasteCategory, accumulatedCost + currentRecord.getEstimatedCost());
            }
        }
        return wasteCostByCategory;
    }

    public HashMap<String, Double> getQuantityByItem(){
        HashMap<String, Double> quantityByItemName = new HashMap<>();
        for(FoodWaste currentRecord : _foodWaste.getAll()){
            String wastedItemName = currentRecord.getItemName();
            Double accumulatedQuantity = quantityByItemName.get(wastedItemName);
            if(accumulatedQuantity == null){
                quantityByItemName.put(wastedItemName, currentRecord.getQuantity());
            }else{
                quantityByItemName.put(wastedItemName, accumulatedQuantity + currentRecord.getQuantity());
            }
        }
        return quantityByItemName;
    }

    public HashMap<String, ArrayList<FoodWaste>> groupByReason(ArrayList<FoodWaste> wasteRecordList){
        HashMap<String, ArrayList<FoodWaste>> wasteRecordsGroupedByReason = new HashMap<>();
        for(FoodWaste currentRecord : wasteRecordList){
            String wasteReason = currentRecord.getReason();
            putIntoGroupedMap(wasteRecordsGroupedByReason, wasteReason, currentRecord);
        }
        return wasteRecordsGroupedByReason;
    }

    public HashMap<String, ArrayList<FoodWaste>> groupByCategory(ArrayList<FoodWaste> wasteRecordList){
        HashMap<String, ArrayList<FoodWaste>> wasteRecordsGroupedByCategory = new HashMap<>();
        for(FoodWaste currentRecord : wasteRecordList){
            String wasteCategory = currentRecord.getCategory();
            putIntoGroupedMap(wasteRecordsGroupedByCategory, wasteCategory, currentRecord);
        }
        return wasteRecordsGroupedByCategory;
    }

    private FoodWaste getWasteOrThrow(int wasteIdentifier) throws ValidationException{
        FoodWaste retrievedRecord = _foodWaste.get(wasteIdentifier);
        if(retrievedRecord == null){
            throw new ValidationException("Waste record not found");
        }
        return retrievedRecord;
    }

    private void ensureNotEmpty(String valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck == null || valueToCheck.trim().isEmpty()){
            throw new ValidationException(fieldName + " is required");
        }
    }

    private void ensurePositive(double valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck <= 0){
            throw new ValidationException(fieldName + " must be greater than 0");
        }
    }

    private void ensureNotNegative(double valueToCheck, String fieldName) throws ValidationException{
        if(valueToCheck < 0){
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }

    private void ensureDateRange(LocalDate startDate, LocalDate endDate) throws ValidationException{
        if(startDate == null || endDate == null){
            throw new ValidationException("Start date and end date are required");
        }
        if(startDate.isAfter(endDate)){
            throw new ValidationException("Start date cannot be after end date");
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
