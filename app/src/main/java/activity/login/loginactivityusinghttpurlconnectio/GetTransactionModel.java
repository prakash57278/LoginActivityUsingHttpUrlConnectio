package activity.login.loginactivityusinghttpurlconnectio;

/**
 * Created by prakash on 20-07-2016.
 */
public class GetTransactionModel {
    private String Description;
    private int Amount;

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    private String TransactionDate;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }



    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }
}
