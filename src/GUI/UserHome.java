package GUI;

import Controllers.UserController;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The following class represents main window of our application.
 */
public class UserHome extends JFrame {
    // Fields:
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the UserHome frame.
     */
    public UserHome(String userName, UserController userController) throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton = new JButton("Find Game");
        btnNewButton.setForeground(new Color(0, 0, 0));
        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 39));
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindGame findGame = null;
                try {
                    findGame = new FindGame(userName); // Opens the FindGame window.
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                assert findGame != null;
                findGame.setTitle("Find Game Page");
                findGame.setVisible(true);
            }
        });
        btnNewButton.setBounds(247, 100, 491, 90);
        contentPane.add(btnNewButton);


        JButton btnJoinGame = new JButton("Games Management");
        btnJoinGame.setForeground(new Color(0, 0, 0));
        btnJoinGame.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnJoinGame.setFont(new Font("Tahoma", Font.PLAIN, 39));
        btnJoinGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JoinGame joinGame = null;
                try {
                    joinGame = new JoinGame(userName); // Opens the Games Management window.
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                assert joinGame != null;
                joinGame.setTitle("Game Management Page");
                joinGame.setVisible(true);
            }
        });
        btnJoinGame.setBounds(247, 235, 491, 90);
        contentPane.add(btnJoinGame);


        JButton button = new JButton("Change-password\n");
        button.setBackground(UIManager.getColor("Button.disabledForeground"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangePassword changePassword = null;
                try {
                    changePassword = new ChangePassword(userName, userController); // Opens the Change password window.
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                assert changePassword != null;
                changePassword.setTitle("Change Password");
                changePassword.setVisible(true);

            }
        });
        button.setFont(new Font("Tahoma", Font.PLAIN, 35));
        button.setBounds(247, 370, 491, 90);
        contentPane.add(button);

        BufferedImage myPicture = ImageIO.read(new File("src/GUI/jordan.PNG"));
        JLabel picJLabel = new JLabel(new ImageIcon(myPicture));
        picJLabel.setBounds(0, 0,1000, 597);
        contentPane.add(picJLabel);
    }
}