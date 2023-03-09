package Service;

import DAO.AccountDAO;

import Model.Account;
import Model.BankUser;

import java.util.List;

public class AccountService {
    /**
     * Create a new account for existed user, validate the user before creating new account
     * @param account
     * @return
     */
    /* TODO: implement user vaidation
    public static Account createNewAccount(Account account, BankUser user){
        if(BankUserService.validateUser(user))
            return AccountDAO.createNewAccount(account);
        else
            return null;
    }
    */
    public static Account createNewAccount(Account account){
        return AccountDAO.createNewAccount(account);
    }

    /**
     *
     * @param account_user
     * @return
     */

    public static List<Account> getAccountByUserID(int account_user){
        return AccountDAO.getAccountByUserID(account_user);
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
                AccountDAO.updateAccount(account);
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

    public static Account withdraw(Account account, double amount){
            if(amount > account.getBalance()){
                System.out.println("Insufficient fund! Please change the withdraw amount ");
            }
        double newAccountBalance = account.getBalance() - amount;
        AccountDAO.updateAccount(account);
        account.setBalance(newAccountBalance);
        return account;
    }
}

