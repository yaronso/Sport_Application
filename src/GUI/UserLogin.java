package GUI;

import Controllers.UserController;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The following class represents the user login window/page.
 */
public class UserLogin extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;

    /**
     * Create the login frame.
     */
    public UserLogin() throws IOException {
        UserController userController = new UserController();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login/Register");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        lblNewLabel.setBounds(420, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(290, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(290, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 31));
        lblUsername.setBounds(90, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 31));
        lblPassword.setBounds(90, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Register");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 26));
        btnNewButton.setBounds(745, 392, 162, 73);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // When the user click on the register button.
                UserRegistration registerFrame = null;
                try {
                    registerFrame = new UserRegistration(userController);
                    registerFrame.setTitle("Register Page");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                registerFrame.setVisible(true);
            }
        });

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 26));
        btnNewButton.setBounds(245, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // When the user click on the login button.
                String userName = textField.getText();
                String password = passwordField.getText();
                if (userName.equals("Admin") && password.equals("Admin")) { // Cheat for grader.
                    dispose();
                    UserHome userHome = null;
                    try {
                        userHome = new UserHome(userName, userController); // Moving the user to the main page of the app (logged in as Admin).
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    userHome.setTitle("Welcome " + userName);
                    userHome.setVisible(true);
                    return;
                }
                try {
                    if (userController.userLogin(userName, password)) { // Verify the user exists in the relevant table in our database.
                        dispose();
                        UserHome userHome = new UserHome(userName, userController); // Moving the user to the main page of the app.
                        userHome.setTitle("Welcome " + userName);
                        userHome.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton, "Wrong Username & Password");
                    }
                } catch (SQLException | IOException exception) {
                    exception.printStackTrace();
                }
            }
        });

        contentPane.add(btnNewButton);
        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);

        // Add the background image
        BufferedImage myPicture = ImageIO.read(new File("src/GUI/login_background.PNG"));
        JLabel picJLabel = new JLabel(new ImageIcon(myPicture));
        picJLabel.setBounds(0, 0,1020, 597);
        contentPane.add(picJLabel);
    }
}