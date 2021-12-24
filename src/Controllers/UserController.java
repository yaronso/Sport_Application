package Controllers;

import Dao.UserDao;
import Dao.UserDaoImpl;
import Models.User;

import java.io.IOException;
import java.sql.SQLException;

// The following class maps requests between the client side and the server/database side in context of user login, registration & change password.
public class UserController {
    public UserDao userDao;

    public UserController() throws IOException {
        this.userDao = new UserDaoImpl();
    }

    public boolean userLogin(String userName, String password) throws SQLException {
        return userDao.userLogin(userName, password);
    }

    public int insertNewUser(User user) throws SQLException {
        return userDao.insertNewUser(user);
    }

    public String validPassword(String userName) throws SQLException {
        return userDao.validPassword(userName);
    }

    public void updateChangePass(String pstr, String userName) throws SQLException {
        userDao.updateChangePass(pstr, userName);
    }

}
