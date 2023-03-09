package Service;

import DAO.AccountDAO;
import DAO.BankUserDAO;
import Model.Account;
import Model.Transaction;

import java.util.Date;
import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    private BankUserDAO bankUserDAO;
    private Transaction transaction;

    public AccountService(){
        accountDAO = new AccountDAO();
        transaction = new Transaction();
    }

    /**
     * Create a new account for existed user, validate the user before creating new account
     * @param account
     * @return
     */
    public Account createNewAccount(Account account){
        if(bankUserDAO.isUserValid(account.getUser()))
            return accountDAO.createNewAccount(account);
        else
            return null;
    }

    /**
     *
     * @param account_user
     * @return
     */

    public List<Account> getAccountByUserID(int account_user){
        return accountDAO.getAccountByUserID(account_user);
    }

    /**
     * deposit a fund to a user account
     * the deposit amount must be > zero
     * @param account
     * @param amount
     * @return
     */

    public  Account deposit(Account account, double amount){
        if(amount > 0){
            double newAccountBalance = account.getBalance() + amount;
            account.setBalance(newAccountBalance);
            accountDAO.updateAccount(account);

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

    public Account withdraw(Account account, double amount){
        if(amount > account.getBalance()){
            System.out.println("Insufficient fund! Please change the withdraw amount ");
        }
        double newAccountBalance = account.getBalance() - amount;
        accountDAO.updateAccount(account);
        account.setBalance(newAccountBalance);

        transaction.setTransactionType("Withdrawal");
        transaction.setAmount(-amount);
        transaction.setAccountUser(account.getUser());
        transaction.setTransactionTime(new Date());
        transaction.setAccountID(account.getAccount_id());

        return account;
    }
}

