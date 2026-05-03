package storage;

import model.Payment;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public interface IPayment {

    void create(Payment payment);
    Payment get(int paymentId);
    ArrayList<Payment> getAll();
    void update(Payment payment);
    void delete(int paymentId);

    ArrayList<Payment> findByOrderId(int orderId);
    ArrayList<Payment> findByStatus(String status);
    ArrayList<Payment> findByDate(LocalDate date);
    ArrayList<Payment> findByPaymentMethod(String method);
}
