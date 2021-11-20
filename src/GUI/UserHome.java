package GUI;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class UserHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the UserHome frame.
     */
    public UserHome(String userName) {
        // TODO - ADD a button that move the user inside the application
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
                    findGame = new FindGame();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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
                    joinGame = new JoinGame(userName);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                joinGame.setVisible(true);
            }
        });
        btnJoinGame.setBounds(247, 235, 491, 90);
        contentPane.add(btnJoinGame);


        JButton button = new JButton("Change-password\n");
        button.setBackground(UIManager.getColor("Button.disabledForeground"));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangePassword bo = null;
                try {
                    bo = new ChangePassword(userName);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                bo.setTitle("Change Password");
                bo.setVisible(true);

            }
        });
        button.setFont(new Font("Tahoma", Font.PLAIN, 35));
        button.setBounds(247, 370, 491, 90);
        contentPane.add(button);
    }
}