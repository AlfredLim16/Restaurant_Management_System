package storage;

import model.Payment;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class InMemoryPayment extends AbstractInMemory<Payment> implements IPayment {

    private final ArrayList<Payment> orderPayments = new ArrayList<>();
    private final ArrayList<Payment> matchingPayments = new ArrayList<>();
    private final ArrayList<Payment> datePayments = new ArrayList<>();
    private final ArrayList<Payment> methodPayments = new ArrayList<>();

    public InMemoryPayment(){

    }

    @Override
    protected int getEntityId(Payment paymentRecord){
        return paymentRecord.getPaymentId();
    }

    @Override
    protected void setEntityId(Payment paymentRecord, int paymentId){
        paymentRecord.setPaymentId(paymentId);
    }

    @Override
    public ArrayList<Payment> findByOrderId(int targetOrderId){
        orderPayments.clear();
        for(Payment currentPayment : storage.values()){
            if(currentPayment.getOrderId() == targetOrderId){
                orderPayments.add(currentPayment);
            }
        }
        return orderPayments;
    }

    @Override
    public ArrayList<Payment> findByStatus(String targetStatus){
        matchingPayments.clear();
        for(Payment currentPayment : storage.values()){
            if(currentPayment.getStatus().equals(targetStatus)){
                matchingPayments.add(currentPayment);
            }
        }
        return matchingPayments;
    }

    @Override
    public ArrayList<Payment> findByDate(LocalDate targetDate){
        datePayments.clear();
        for(Payment currentPayment : storage.values()){
            LocalDate paymentDate = currentPayment.getPaymentTime().toLocalDate();
            if(paymentDate.equals(targetDate)){
                datePayments.add(currentPayment);
            }
        }
        return datePayments;
    }

    @Override
    public ArrayList<Payment> findByPaymentMethod(String targetPaymentMethod){
        methodPayments.clear();
        for(Payment currentPayment : storage.values()){
            if(currentPayment.getPaymentMethod().equals(targetPaymentMethod)){
                methodPayments.add(currentPayment);
            }
        }
        return methodPayments;
    }
}
