package Service;

import DAO.*;
import Model.*;

public class BankUserService {

    private BankUserDAO accountDAO;

    //Constructor for creating new AccountService() with a new AccountDAO()
    public BankUserService(){
        accountDAO = new BankUserDAO();
    }

    /**
     * Constructor when AccountDAO is provided
     * @param accountDAO
     */
    public BankUserService(BankUserDAO accountDAO){
        this.accountDAO = new BankUserDAO();
    }

    public BankUser addAccount(BankUser bankUser){
        //The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
        if (bankUser.username == "") return null;
        if (bankUser.password.length() < 4) return null;

        return accountDAO.insertNewAccount(bankUser);
    }

    public BankUser loginAccount(BankUser bankUser){
        //The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database.

        return accountDAO.loginIntoAccount(bankUser);
    }

    public boolean validateUser(BankUser user) {
        return accountDAO.isUserValid(user.user_id);
    }
}
