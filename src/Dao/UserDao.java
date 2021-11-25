package Dao;
import java.sql.Connection;
import java.sql.SQLException;

import Models.User;


public interface UserDao {
    int insert(User user) throws SQLException;
    boolean userLogin(String userName, String password) throws SQLException;
    int updateChangePass(String pstr, String userName) throws SQLException;
    String validPassword(String userName) throws SQLException;
}
