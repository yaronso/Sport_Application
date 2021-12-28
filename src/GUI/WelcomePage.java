package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The following class is the welcome window/page of our application.
 */
public class WelcomePage extends JFrame {
    // Fields
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnNewButton;
    private JLabel label;

    /**
     * Create the Welcome Page frame.
     */
    public WelcomePage() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Welcome");
        lblNewLabel.setForeground(Color.ORANGE);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 36));
        lblNewLabel.setBounds(420, 13, 273, 93);
        contentPane.add(lblNewLabel);

        // When clicking on the Login/Register button the relevant window will open.
        btnNewButton = new JButton("Login/Register");
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
        btnNewButton.setBounds(745, 392, 162, 73);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserLogin loginFrame = null;
                try {
                    dispose();
                    loginFrame = new UserLogin();
                    loginFrame.setTitle("Login/Register Page");
                    loginFrame.setVisible(true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        // Add the background image
        BufferedImage myPicture = ImageIO.read(new File("src/GUI/Match4Sport.PNG"));
        JLabel picJLabel = new JLabel(new ImageIcon(myPicture));
        picJLabel.setBounds(0, 0,1020, 597);
        contentPane.add(picJLabel);
    }

}
