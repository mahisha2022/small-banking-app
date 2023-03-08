package Service;

import DAO.TransactionDAO;
import Model.Transaction;

import java.util.List;

public class TransactionService {

    private  TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;

    }

    public List<Transaction> getTransactionByUserID(int userId){
        return null;
    }

    public void addTransaction(Transaction transaction){
        //transactionDAO.addTransaction(transaction);
    }
}
