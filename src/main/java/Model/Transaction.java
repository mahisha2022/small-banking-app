package Model;

import java.util.Date;

public class Transaction {

    private int TransactionId;

    //    private String transactionType;
    private double amount;

    private Date transactionTime;
    private int accountUser;
    private int accountID;

    private String transactionType;

    public Transaction(){

    }

    public Transaction(int transactionId, String transactionType, double amount, Date transactionTime,
                       int accountUser, int accountID) {
        TransactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.accountUser = accountUser;
        this.accountID = accountID;

    }

    public int getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(int transactionId) {
        TransactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public int getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(int accountUser) {
        this.accountUser = accountUser;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}


