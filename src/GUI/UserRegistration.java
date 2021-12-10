package GUI;

import Dao.UserDao;
import Dao.UserDaoImpl;
import Models.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


/**
 * User Registration using Swing.
 */
public class UserRegistration extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField firstname;
    private JTextField lastname;
    private JTextField username;
    private JPasswordField passwordField;
    private JTextField email;
    private JTextField mob;
    private JButton btnNewButton;


    /**
     * Create the register frame.
     */
    public UserRegistration() throws IOException {
        UserDao userDao = new UserDaoImpl();
        List userList = new List();
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\User\\Desktop\\STDM.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("New User Register");
        lblNewUserRegister.setFont(new Font("Times New Roman", Font.BOLD, 35));
        lblNewUserRegister.setForeground(Color.white);
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);


        JLabel lblName = new JLabel("First name");
        lblName.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblName.setForeground(Color.white);
        lblName.setBounds(58, 152, 99, 43);
        contentPane.add(lblName);

        JLabel lblNewLabel = new JLabel("Last name");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel.setBounds(58, 243, 110, 29);
        lblNewLabel.setForeground(Color.white);
        contentPane.add(lblNewLabel);

        JLabel lblEmailAddress = new JLabel("Email\r\n address");
        lblEmailAddress.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblEmailAddress.setBounds(58, 324, 124, 36);
        lblEmailAddress.setForeground(Color.white);
        contentPane.add(lblEmailAddress);


        firstname = new JTextField();
        firstname.setFont(new Font("Tahoma", Font.BOLD, 32));
        firstname.setBounds(214, 151, 228, 50);
        contentPane.add(firstname);
        firstname.setColumns(10);

        lastname = new JTextField();
        lastname.setFont(new Font("Tahoma", Font.BOLD, 32));
        lastname.setBounds(214, 235, 228, 50);
        contentPane.add(lastname);
        lastname.setColumns(10);

        email = new JTextField();

        email.setFont(new Font("Tahoma", Font.BOLD, 32));
        email.setBounds(214, 320, 228, 50);
        contentPane.add(email);
        email.setColumns(10);

        username = new JTextField();
        username.setFont(new Font("Tahoma", Font.BOLD, 32));
        username.setBounds(707, 151, 228, 50);
        contentPane.add(username);
        username.setColumns(10);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblUsername.setBounds(542, 159, 99, 29);
        lblUsername.setForeground(Color.white);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblPassword.setBounds(542, 245, 99, 24);
        lblPassword.setForeground(Color.white);
        contentPane.add(lblPassword);

        JLabel lblMobileNumber = new JLabel("Mobile number");
        lblMobileNumber.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblMobileNumber.setBounds(542, 329, 139, 26);
        lblMobileNumber.setForeground(Color.white);
        contentPane.add(lblMobileNumber);

        mob = new JTextField();
        mob.setFont(new Font("Tahoma", Font.BOLD, 32));
        mob.setBounds(707, 320, 228, 50);
        contentPane.add(mob);
        mob.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.BOLD, 32));
        passwordField.setBounds(707, 235, 228, 50);
        contentPane.add(passwordField);

        btnNewButton = new JButton("Register");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rc = 0;
                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String emailId = email.getText();
                String userName = username.getText();
                String mobileNumber = mob.getText();
                String password = passwordField.getText();
                if (Utils.PropertiesReaders.isAnyObjectNull(firstName, lastName, emailId, userName, password, mobileNumber)) {
                    JOptionPane.showMessageDialog(btnNewButton, "Please fill all the required fields.");
                    return;
                }
                else if (mobileNumber.length() != 10) {
                    JOptionPane.showMessageDialog(btnNewButton, "Enter a valid mobile number");
                    return;
                }
                User user = new User(firstName, lastName, userName, password, emailId, mobileNumber);
                userList.add(String.valueOf(user));
                // Insert the new input user to the DataBase
                try {
                    rc = userDao.insert(user);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                insertRegisteredUser(rc, firstName);
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD,  22));
        btnNewButton.setBounds(399, 447, 259, 74);
        contentPane.add(btnNewButton);

        // Add the background image
        BufferedImage myPicture = ImageIO.read(new File("src/GUI/register_background.PNG"));
        JLabel picJLabel = new JLabel(new ImageIcon(myPicture));
        picJLabel.setBounds(0, 0,1020, 597);
        contentPane.add(picJLabel);
    }


    private void insertRegisteredUser(int rc, String firstName) {
        String msg = "" + firstName;
        msg += " \n";
        if (rc == 0) {
            JOptionPane.showMessageDialog(btnNewButton, "The input user is already exist");
        } else {
            JOptionPane.showMessageDialog(btnNewButton,"Welcome, " + msg + "Your account is successfully created");
            dispose();
        }
    }

}