package formatting;

import model.Payment;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author admin
 */
public class PaymentFormatting {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public String formatPaymentTime(Payment payment){
        if(payment.getPaymentTime() == null){
            return "";
        }
        return payment.getPaymentTime().format(TIME_FORMATTER);
    }

    public String formatAmount(double amount){
        return String.format("%.2f", amount);
    }

    public String formatTipAmount(double tipAmount){
        if(tipAmount == 0){
            return "";
        }
        return String.format("%.2f", tipAmount);
    }

    public String formatStatus(String status){
        switch(status){
            case "Completed":
                return status;
            case "Pending":
                return status;
            case "Failed":
                return status;
            case "Refunded":
                return status;
            default:
                return status;
        }
    }
}
