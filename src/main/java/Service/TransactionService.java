package Service;

import DAO.TransactionDAO;
import Model.Transaction;
import java.util.List;

public class TransactionService {
    /**
     * @param userId
     * @return a list of transaction by user id
     */
    public static List<Transaction> getTransactionsByAccountID(int accountID) {
        return TransactionDAO.getTransactionsByAccountID(accountID);
    }

    public static Transaction addTransaction(Transaction transaction) {
       return TransactionDAO.addTransaction(transaction);
    }
}
