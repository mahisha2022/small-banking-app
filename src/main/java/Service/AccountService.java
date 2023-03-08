package Service;

import DAO.AccountDAO;
import DAO.BankUserDAO;
import Model.Account;
//import Model.BankUser;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class AccountService {
    private AccountDAO accountDAO;
    //private BankUser bankUser;
    private BankUserDAO bankUserDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Create a new account for existed user, validate the user before creating new account
     * @param account
     * @return
     */
    public Account createNewAccount(Account account){
        if(bankUserDAO.isUserValid())
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
                accountDAO.updateAccount(account);
                account.setBalance(newAccountBalance);
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
        return account;
    }
}

