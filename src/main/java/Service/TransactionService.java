package Service;


import DAO.TransactionDAO;
import Model.Transaction;

import java.util.List;

public class TransactionService {

    private TransactionDAO transactionDAO;

public TransactionService(){

}
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

    public Transaction addTransaction(Transaction transaction) {
       return transactionDAO.addTransaction(transaction);


    }
}



