package Dao;
import java.util.List;
import Models.User;


public interface UserDao {
    public int delete(String id);
    public List<User> findAll();
    public User findById(String id);
    public User findByName(String name);
    public int insert(User user);
    public int update(User user);

}
