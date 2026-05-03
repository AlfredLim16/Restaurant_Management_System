package model;

import java.time.LocalDateTime;

/**
 *
 * @author admin
 */
public class Payment {

    private int paymentId;
    private int orderId;
    private double amount;
    private double tipAmount;
    private String paymentMethod;
    private String status;
    private LocalDateTime paymentTime;
    private String transactionId;

    public Payment(){

    }

    public Payment(int paymentId, int orderId, double amount, double tipAmount, String paymentMethod, String status, LocalDateTime paymentTime, String transactionId){
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.tipAmount = tipAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentTime = paymentTime;
        this.transactionId = transactionId;
    }

    public int getPaymentId(){
        return paymentId;
    }

    public void setPaymentId(int paymentId){
        this.paymentId = paymentId;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public double getTipAmount(){
        return tipAmount;
    }

    public void setTipAmount(double tipAmount){
        this.tipAmount = tipAmount;
    }

    public String getPaymentMethod(){
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod){
        this.paymentMethod = paymentMethod;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public LocalDateTime getPaymentTime(){
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime){
        this.paymentTime = paymentTime;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
    }
}
