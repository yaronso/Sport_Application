package Models;

import java.io.Serializable;

/**
 * Domain User model
 */
public class User implements Serializable {

    //private static final long serialVersionUID = 3789909326487155148L;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String mobile;

    // Constructor
    public User(String firstName, String lastName, String userName, String password, String emailId, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.email = emailId;
        this.mobile = mobileNumber;
    }


    public User() {
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}