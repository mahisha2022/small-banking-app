package Service;

import DAO.BankUserDAO;
import DAO.AccountDAO;
import Model.Account;
import Model.Transaction;

import java.util.Date;
import java.util.List;

public class AccountService {
    /**
     * Create a new account for existed user, validate the user before creating new account
     * @param account
     * @return
     */
    public static Account createNewAccount(Account account){
        if(BankUserDAO.isUserValid(account.getUser()))
            return AccountDAO.createNewAccount(account);
        else
            return null;
    }

    /**
     *
     * @param account_user
     * @return
     */

    public static List<Account> getAccountsByUserID(int account_user){
        return AccountDAO.getAccountsByUserID(account_user);
    }

    /**
     * deposit a fund to a user account
     * the deposit amount must be > zero
     * @param account
     * @param amount
     * @return
     */

    public static Account deposit(Account account, double amount){
        if(amount > 0){
            double newAccountBalance = account.getBalance() + amount;
            account.setBalance(newAccountBalance);
            AccountDAO.updateAccount(account);

            Transaction transaction = new Transaction();
            transaction.setTransactionType("Deposit");
            transaction.setAmount(amount);
            transaction.setAccountUser(account.getUser());
            transaction.setTransactionTime(new Date());
            transaction.setAccountID(account.getAccount_id());

        }
        else {
            System.out.println("Deposit amount is not allowed");
        }
        return account;
    }

    /**
     * withdraw a fund from a user account
     * the withdraw amount must be <= user account balance
     * @param account
     * @param amount
     * @return
     */

    public static Account withdraw(Account account, double amount){
        if(amount > account.getBalance()){
            System.out.println("Insufficient fund! Please change the withdraw amount ");
        }

        double newAccountBalance = account.getBalance() - amount;
        AccountDAO.updateAccount(account);
        account.setBalance(newAccountBalance);

        Transaction transaction = new Transaction();
        transaction.setTransactionType("Withdrawal");
        transaction.setAmount(-amount);
        transaction.setAccountUser(account.getUser());
        transaction.setTransactionTime(new Date());
        transaction.setAccountID(account.getAccount_id());

        return account;
    }
}
