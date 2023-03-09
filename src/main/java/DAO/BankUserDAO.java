package DAO;

import java.sql.*;
import Model.*;
import Util.ConnectionSingleton;

public class BankUserDAO {

    /**
     * The user_id is automatically generated by the sql database
     *
     ## Table bank_user (
     user_id int primary key auto_increment,
     username varchar(255) unique,
     password varchar(255)
     );
     *
     */
    public BankUser insertNewAccount(BankUser newBankUser){
        Connection connection = ConnectionSingleton.getConnection();
        try {
            // assuming database automatically generate a primary key.
            String sql = "INSERT INTO bank_user(username, password) VALUES (?,?);" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // preparedStatement's setString method
            preparedStatement.setString(1, newBankUser.getUsername());
            preparedStatement.setString(2, newBankUser.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_newAccount_id = (int) pkeyResultSet.getLong(1);
                return new BankUser(generated_newAccount_id, newBankUser.getUsername(), newBankUser.getPassword());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BankUser loginIntoAccount(BankUser loginUser){
        Connection connection = ConnectionSingleton.getConnection();
        try {
            String sql = "SELECT * FROM bank_user WHERE username = ? AND password = ?;" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, loginUser.getUsername());
            preparedStatement.setString(2, loginUser.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                BankUser logedUser = new BankUser(rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return logedUser;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isUserValid() {
        // should check in DDBB if the parameter is a valid user
        return true;
    }
}