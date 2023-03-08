package Model;

import java.util.Date;

public class Transaction {

    private int TransactionId;
    private Date transactionDateAndTime;
    private double amount;
    private int userId;
    private String transactionId;

    public Transaction(int transactionId, Date transactionDateAndTime, double amount, int userId, String transactionId1) {
        TransactionId = transactionId;
        this.transactionDateAndTime = transactionDateAndTime;
        this.amount = amount;
        this.userId = userId;
        this.transactionId = transactionId1;
    }

    public int getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionId(int transactionId) {
        TransactionId = transactionId;
    }

    public Date getTransactionDateAndTime() {
        return transactionDateAndTime;
    }

    public void setTransactionDateAndTime(Date transactionDateAndTime) {
        this.transactionDateAndTime = transactionDateAndTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
