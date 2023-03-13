package Controller;

import Model.Account;
import Model.BankUser;
import Model.Transaction;
import Service.AccountService;
import Service.BankUserService;
import Service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.text.ParseException;

import java.util.List;

// Defining endpoints and handlers for controller
public class BankController {
    private ObjectMapper mapper;

    /**
     * Endpoints in the startAPI() method, as the test suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        mapper = new ObjectMapper();

        Javalin app = Javalin.create();
        //1. Process registration - POST localhost:8080/register
        app.post("/register", this::registerHandler);

        //2. Process logins- POST localhost:8080/login
        app.post("/login", this::loginHandler);

        /* Create new account */
        app.post("/register/account*", this::accountOpenHandler);

        /* Get account */
        app.get("/users/accounts*", this::accountsGetHandler);
        app.get("/{accountID}/account*", this::accountGetByIDHandler);
        /* Get transaction by user id*/
        app.get("/{accountID}/transactions*", this::getTransactionsHandler);
        app.post("/{accountID}/transfer*", this::addTransactionHandler);

        return app;
    }

    /**
     * 1. Handler to POST and new registration endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     *
     * The body will contain a representation of a JSON Account, but will not contain an user_id.
     *  - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     *  - If the registration is not successful, the response status should be 400. (Client error)
     *
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        BankUser user = mapper.readValue(context.body(), BankUser.class);
        BankUser addedUser = BankUserService.addUser(user);

        // if new unique user return JSON BankUser
        if (addedUser != null){
            context.json(mapper.writeValueAsString(addedUser));
            context.status(200);
        } else {
            // else not successful registration
            context.status(400);
        }
    }

    /**
     * 2: API should be able to process User logins.
     * Verify login on the endpoint POST localhost:8080/login.
     * The request body will contain a JSON representation of a BankUser, not containing an user_id. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
     *   - The login will be successful if and only if the username and password provided in the request body JSON match a real user existing on the database.
     *     If successful, the response body should contain a JSON of the user in the response body, including its user_id.
     *     The response status should be 200 OK, which is the default.
     *   - If the login is not successful, the response status should be 401. (Unauthorized)
     *
     * @param context
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        BankUser user = mapper.readValue(context.body(), BankUser.class);
        BankUser loginUser = BankUserService.loginUser(user);

        // if unique user return JSON BankUser
        if (loginUser != null){
            context.json(mapper.writeValueAsString(loginUser));
            context.status(200);
        } else {
            // unauthorized login
            context.status(401);
        }
    }

    /* Get user from request body (JSON) and add user
     * responds with 400 (error) or 200 (success)
     */
    private String[] getParams(String url, String param1, String param2) throws ParseException {
        String[] out = new String[2];
        int paramsIdx = url.indexOf('?');
        if (paramsIdx < 0 || url.charAt(paramsIdx++) != '?')
            throw new ParseException("No ?", paramsIdx);

        if (!url.regionMatches(paramsIdx, param1, 0, param1.length()))
            throw new ParseException("No paramiter: " + param1, paramsIdx);
        
        int amperIdx = url.indexOf('&', paramsIdx += param1.length());
        try {
            while (url.charAt(amperIdx - 1) == '\\')
                amperIdx = url.indexOf('&', ++amperIdx);
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("No seperator", paramsIdx);
        }
        if (!url.regionMatches(amperIdx + 1, param2, 0, param2.length()))
            throw new ParseException("No paramiter: " + param2, amperIdx + 1);
        out[0] = url.substring(paramsIdx, amperIdx);
        out[1] = url.substring(amperIdx + 1 + param2.length());
        return out;
    }

    private void accountOpenHandler(Context ctx) throws JsonProcessingException {
        String[] cred;
        try {
            cred = getParams(ctx.fullUrl(), "username=", "password=");
        } catch (ParseException e) {
            ctx.status(400);
            return;
        }

        BankUser user = new BankUser(cred[0], cred[1]);
        BankUser loginUser = BankUserService.loginUser(user);
        if (loginUser == null) {
            ctx.status(400);
            return;
        }
        Account account = mapper.readValue(ctx.body(), Account.class);
        account.setUser(loginUser.getUser_id());
        Account newAccount = AccountService.createNewAccount(account);
        if (newAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newAccount));
            ctx.status(200);
        }
    }

    private void accountGetByIDHandler(Context ctx) throws JsonProcessingException {
        System.out.println("Account queried: " + ctx.pathParam("accountID"));
        String[] cred;
        try {
            cred = getParams(ctx.fullUrl(), "username=", "password=");
        } catch (ParseException e) {
            ctx.status(401);
            return;
        }
        System.out.println("Checkpoint");
        BankUser user = new BankUser(cred[0], cred[1]);
        BankUser loginUser = BankUserService.loginUser(user);
        int accountID = Integer.parseInt(ctx.pathParam("accountID"));
        Account account = AccountService.getAccountByID(accountID);
        if (loginUser != null && account != null && loginUser.getUser_id() == account.getUser()) {
            ctx.json(account);
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    /* Get user's accounts
     * responds with 200 and accounts in body
     */
    private void accountsGetHandler(Context ctx) throws JsonProcessingException {
        String[] cred;
        try {
            cred = getParams(ctx.fullUrl(), "username=", "password=");
        } catch (ParseException e) {
            ctx.status(401);
            return;
        }
        BankUser user = new BankUser(cred[0], cred[1]);
        BankUser loginUser = BankUserService.loginUser(user);
        if (loginUser != null) {
            ctx.json(AccountService.getAccountsByUserID(loginUser.getUser_id()));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    private void getTransactionsHandler(Context ctx) throws JsonProcessingException {
        String[] cred;
        int accountID = Integer.parseInt(ctx.pathParam("accountID"));
        try {
            cred = getParams(ctx.fullUrl(), "username=", "password=");
        } catch (ParseException e) {
            ctx.status(401);
            return;
        }
        BankUser user = new BankUser(cred[0], cred[1]);
        BankUser loginUser = BankUserService.loginUser(user);
        if (loginUser == null) {
            ctx.status(401);
            return;
        }
        Account account = AccountService.getAccountByID(accountID);
        if (account == null || loginUser.getUser_id() != account.getUser()) {
            ctx.status(401);
            return;
        }
        List<Transaction> transactionByUserID = TransactionService.getTransactionsByAccountID(accountID);
        ctx.json(mapper.writeValueAsString(transactionByUserID));
        ctx.status(200);
    }

    private void addTransactionHandler(Context ctx) throws JsonProcessingException {
        String[] cred;
        int accountID = Integer.parseInt(ctx.pathParam("accountID"));
        try {
            cred = getParams(ctx.fullUrl(), "username=", "password=");
        } catch (ParseException e) {
            ctx.status(400);
            return;
        }
        BankUser user = new BankUser(cred[0], cred[1]);
        BankUser loginUser = BankUserService.loginUser(user);
        if (loginUser == null) {
            ctx.status(400);
            return;
        }
        Transaction transaction = mapper.readValue(ctx.body(), Transaction.class);
        Transaction newTransaction = TransactionService.addTransaction(transaction, accountID, loginUser.getUser_id());
        if(newTransaction != null) {
            ctx.json(mapper.writeValueAsString(newTransaction));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }
}
