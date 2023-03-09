package Service;


import DAO.TransactionDAO;
import Model.Transaction;

import java.util.List;

public class TransactionService {

    private TransactionDAO transactionDAO;


    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;

    }

    /**
     * @param userId
     * @return a list of transaction by user id
     */
    public List<Transaction> getTransactionByUserID(int userId) {
        return transactionDAO.transactionByUserID(userId);
    }

    public void addTransaction(Transaction transaction) {
        //transactionDAO.addTransaction(transaction);

        // Instead of adding a transaction here, let Insert a transaction when a user make a
        // withdrawal or deposit


    }
}

// this is for the AccountService class deposit . Fransisco will update it

//    public Account deposit(int acctID, double amount){
//        Transaction transaction = new Transaction();
//        if(amount <= 0){
//            System.out.println( amount +" is invalid amount. Please change the deposit amount");
//        }
//        else {
//            accountDAO.updateAccount(acctID, amount);
//            transaction.setAccountUser(acctID);
//            transaction.setAmount(amount);
//            transaction.setTransactionTime(new Date());
//            transaction.setTransactionType(TransactionType.Deposit);
//            transactionDAO.addTransaction(transaction);
//
//        }


//    public Account withdraw(int acctID, double amount){
//        Transaction transaction = new Transaction();
//        if(amount > account.getBalance()){
//            System.out.println( "Insufficient balance! Your account balance is " + account.getBalance());
//        }
//        else {
//            accountDAO.updateAccount(acctID, amount);
//            transaction.setAccountUser(acctID);
//            transaction.setAmount(-amount);
//            transaction.setTransactionTime(new Date());
//            transaction.setTransactionType(TransactionType.Withdrawal);
//            transactionDAO.addTransaction(transaction);
//
//        }
//        return account;
//
//    }


