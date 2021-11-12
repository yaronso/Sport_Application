package Dao;
import java.util.List;
import Models.User;


public interface UserDao {
    public int insert(User user);
    public int updateChangePass(String pstr, String userName);
}
