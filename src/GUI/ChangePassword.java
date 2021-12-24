package GUI;

import Controllers.UserController;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChangePassword extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JLabel lblEnterNewPassword;

    /**
     * Create ChangePassword frame.
     */
    public ChangePassword(String userName, UserController userController) throws IOException {
        setBounds(450, 360, 1024, 234);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 34));
        textField.setBounds(373, 35, 609, 67);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnSearch = new JButton("Enter");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String passTxt = textField.getText();
                try {
                    if (Utils.PropertiesReaders.isAnyObjectNull(passTxt)) {
                        JOptionPane.showMessageDialog(btnSearch, "Change Password Error: Please enter your new password.");
                        return;
                    }
                    if (userController.validPassword(userName).equals(passTxt)) {
                        JOptionPane.showMessageDialog(btnSearch, "Change Password Error: Your new password similar to your old password.");
                        return;
                    }
                    userController.updateChangePass(passTxt, userName);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                JOptionPane.showMessageDialog(btnSearch, "Password has been successfully changed for the user " + userName);
                dispose();
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 29));
        btnSearch.setBackground(new Color(240, 240, 240));
        btnSearch.setBounds(438, 127, 170, 59);
        contentPane.add(btnSearch);

        lblEnterNewPassword = new JLabel("Enter New Password :");
        lblEnterNewPassword.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblEnterNewPassword.setBounds(45, 37, 326, 67);
        contentPane.add(lblEnterNewPassword);
    }
}