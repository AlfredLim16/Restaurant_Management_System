package service;

import exception.PaymentFailedException;
import exception.ValidationException;
import storage.IOrder;
import storage.IPayment;
import model.Order;
import model.Payment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author admin
 */
public class PaymentService {

    private final IPayment _payment;
    private final IOrder _order;

    public PaymentService(IPayment payment, IOrder order){
        this._payment = payment;
        this._order = order;
    }

    public Payment processPayment(int orderId, double tipAmount, String paymentMethod) throws ValidationException, PaymentFailedException{
        Order existingOrder = getOrderOrThrow(orderId);
        if(!existingOrder.getStatus().equals(OrderStatus.SERVED) && !existingOrder.getStatus().equals(OrderStatus.READY)){
            throw new ValidationException("Order must be served before payment");
        }else if(hasCompletedPayment(orderId)){
            throw new ValidationException("Order already paid");
        }else if(!isValidPaymentMethod(paymentMethod)){
            throw new ValidationException("Invalid payment method: " + paymentMethod);
        }
        if(tipAmount < 0){
            throw new ValidationException("Tip amount cannot be negative");
        }
        Payment newRecord = new Payment();
        newRecord.setOrderId(orderId);
        newRecord.setAmount(existingOrder.getTotalAmount());
        newRecord.setTipAmount(tipAmount);
        newRecord.setPaymentMethod(paymentMethod);
        newRecord.setPaymentTime(LocalDateTime.now());
        newRecord.setTransactionId(generateTransactionId());
        try{
            boolean paymentSuccessful = simulatePaymentProcessing(paymentMethod);
            if(paymentSuccessful){
                newRecord.setStatus(PaymentStatus.COMPLETED);
                existingOrder.setStatus(OrderStatus.COMPLETED);
                _order.update(existingOrder);
            }else{
                newRecord.setStatus(PaymentStatus.FAILED);
                throw new PaymentFailedException("Payment processing failed");
            }
        }catch(PaymentFailedException paymentException){
            newRecord.setStatus(PaymentStatus.FAILED);
            _payment.create(newRecord);
            throw new PaymentFailedException("Payment processing error: " + paymentException.getMessage(), paymentException);
        }
        _payment.create(newRecord);
        return newRecord;
    }

    private boolean hasCompletedPayment(int orderIdentifier){
        for(Payment currentPayment : _payment.findByOrderId(orderIdentifier)){
            if(PaymentStatus.COMPLETED.equals(currentPayment.getStatus())){
                return true;
            }
        }
        return false;
    }

    private boolean isValidPaymentMethod(String methodToValidate){
        if(methodToValidate == null){
            return false;
        }
        String lowerCaseMethod = methodToValidate.toLowerCase();
        return lowerCaseMethod.equals(PaymentMethod.CASH) || lowerCaseMethod.equals(PaymentMethod.CARD) || lowerCaseMethod.equals(PaymentMethod.GCASH) || lowerCaseMethod.equals(PaymentMethod.MAYA);
    }

    private boolean simulatePaymentProcessing(String paymentMethod){
        return Math.random() < 0.95;
    }

    private String generateTransactionId(){
        String date = LocalDate.now().toString().replace("-", "");
        int random = (int) (Math.random() * 10000);
        return "RMS-TXN-" + date + "-" + random;
    }

    public double getTotalWithTip(Payment paymentRecord){
        return paymentRecord.getAmount() + paymentRecord.getTipAmount();
    }

    public long getTransactionCount(){
        long completedTransactionCount = 0;
        for(Payment currentPayment : _payment.getAll()){
            if(PaymentStatus.COMPLETED.equals(currentPayment.getStatus())){
                completedTransactionCount++;
            }
        }
        return completedTransactionCount;
    }

    public HashMap<String, Long> getCountByMethod(){
        HashMap<String, Long> paymentCountByMethod = new HashMap<>();
        for(Payment currentPayment : _payment.getAll()){
            String paymentMethod = currentPayment.getPaymentMethod();
            Long currentCount = paymentCountByMethod.get(paymentMethod);
            if(currentCount == null){
                paymentCountByMethod.put(paymentMethod, 1L);
            }else{
                paymentCountByMethod.put(paymentMethod, currentCount + 1);
            }
        }
        return paymentCountByMethod;
    }

    public ArrayList<Payment> getPendingPayments(){
        return new ArrayList<>(_payment.findByStatus(PaymentStatus.PENDING));
    }

    public HashMap<String, ArrayList<Payment>> groupByPaymentMethod(ArrayList<Payment> paymentList){
        HashMap<String, ArrayList<Payment>> paymentsGroupedByMethod = new HashMap<>();
        for(Payment currentPayment : paymentList){
            String paymentMethod = currentPayment.getPaymentMethod();
            putIntoGroupedMap(paymentsGroupedByMethod, paymentMethod, currentPayment);
        }
        return paymentsGroupedByMethod;
    }

    public HashMap<String, ArrayList<Payment>> groupByStatus(ArrayList<Payment> paymentList){
        HashMap<String, ArrayList<Payment>> paymentsGroupedByStatus = new HashMap<>();
        for(Payment currentPayment : paymentList){
            String paymentStatus = currentPayment.getStatus();
            putIntoGroupedMap(paymentsGroupedByStatus, paymentStatus, currentPayment);
        }
        return paymentsGroupedByStatus;
    }

    public double calculateDailyRevenue(LocalDate targetDate){
        double dailyRevenueTotal = 0.0;
        for(Payment currentPayment : _payment.findByDate(targetDate)){
            if(PaymentStatus.COMPLETED.equals(currentPayment.getStatus())){
                dailyRevenueTotal += currentPayment.getAmount();
            }
        }
        return dailyRevenueTotal;
    }

    public double calculateDailyTips(LocalDate targetDate){
        double dailyTipsTotal = 0.0;
        for(Payment currentPayment : _payment.findByDate(targetDate)){
            if(PaymentStatus.COMPLETED.equals(currentPayment.getStatus())){
                dailyTipsTotal += currentPayment.getTipAmount();
            }
        }
        return dailyTipsTotal;
    }

    public double calculateAverageTransaction(){
        ArrayList<Payment> completedPaymentList = new ArrayList<>(_payment.findByStatus(PaymentStatus.COMPLETED));
        if(completedPaymentList.isEmpty()){
            return 0.0;
        }
        double accumulatedTotal = 0.0;
        for(Payment currentPayment : completedPaymentList){
            accumulatedTotal += currentPayment.getAmount();
        }
        return accumulatedTotal / completedPaymentList.size();
    }

    public double calculateTotalRevenue(){
        double accumulatedRevenue = 0.0;
        for(Payment completedPayment : _payment.findByStatus(PaymentStatus.COMPLETED)){
            accumulatedRevenue += completedPayment.getAmount();
        }
        return accumulatedRevenue;
    }

    public double calculateTotalTips(){
        double accumulatedTips = 0.0;
        for(Payment completedPayment : _payment.findByStatus(PaymentStatus.COMPLETED)){
            accumulatedTips += completedPayment.getTipAmount();
        }
        return accumulatedTips;
    }

    public ArrayList<Payment> getAllPayments(){
        return new ArrayList<>(_payment.getAll());
    }

    public ArrayList<Payment> getPaymentsByStatus(String targetStatus){
        return new ArrayList<>(_payment.findByStatus(targetStatus));
    }

    public int getTransactionCountAsInt(){
        return (int) getTransactionCount();
    }

    public void refundPayment(int paymentIdentifier) throws ValidationException{
        Payment existingPaymentRecord = getPaymentOrThrow(paymentIdentifier);

        if(!PaymentStatus.COMPLETED.equals(existingPaymentRecord.getStatus())){
            throw new ValidationException("Can only refund completed payments");
        }
        existingPaymentRecord.setStatus(PaymentStatus.REFUNDED);
        _payment.update(existingPaymentRecord);

        Order relatedOrder = _order.get(existingPaymentRecord.getOrderId());
        if(relatedOrder != null){
            relatedOrder.setStatus(OrderStatus.SERVED);
            _order.update(relatedOrder);
        }
    }

    private Order getOrderOrThrow(int orderId) throws ValidationException{
        Order retrievedOrder = _order.get(orderId);
        if(retrievedOrder == null){
            throw new ValidationException("Order not found");
        }
        return retrievedOrder;
    }

    private Payment getPaymentOrThrow(int paymentId) throws ValidationException{
        Payment retrievedPayment = _payment.get(paymentId);
        if(retrievedPayment == null){
            throw new ValidationException("Payment not found");
        }
        return retrievedPayment;
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
