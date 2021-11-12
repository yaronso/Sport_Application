package GUI;

import Dao.GameDao;
import Dao.GameDaoImpl;
import Dao.UserDao;
import Dao.UserDaoImpl;
import Models.Game;
import Models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class CreateGame extends JFrame{
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField gameName;
    private JTextField sportCategory;
    private JTextField country;
    private JTextField city;
    private JTextField date;
    private JTextField numOfPlayers;
    private JTextField levelOfPlayers;
    private JButton btnNewButton;

    public CreateGame(Connection connection, String userName, JTable jTable) {
        GameDao gameDao = new GameDaoImpl();
        List gameList = new List();
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\User\\Desktop\\STDM.jpg"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewUserRegister = new JLabel("Create A New Game");
        lblNewUserRegister.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        lblNewUserRegister.setBounds(362, 52, 325, 50);
        contentPane.add(lblNewUserRegister);

        JLabel lblID = new JLabel("Game Name");
        lblID.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblID.setBounds(58, 152, 99, 43);
        contentPane.add(lblID);

        JLabel lblCategory = new JLabel("Sport Category");
        lblCategory.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCategory.setBounds(58, 243, 110, 29);
        contentPane.add(lblCategory);


        JLabel lblCity = new JLabel("City");
        lblCity.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCity.setBounds(58, 324, 124, 36);
        contentPane.add(lblCity);

        JLabel lblCountry = new JLabel("Country");
        lblCountry.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCountry.setBounds(58, 420, 110, 29);
        contentPane.add(lblCountry);

        JLabel lblUdate = new JLabel("Date");
        lblUdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblUdate.setBounds(542, 159, 99, 29);
        contentPane.add(lblUdate);

        JLabel lblNumOfPlayers = new JLabel("#Players");
        lblNumOfPlayers.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNumOfPlayers.setBounds(542, 245, 99, 24);
        contentPane.add(lblNumOfPlayers);

        JLabel lblGameLevel = new JLabel("Game Level");
        lblGameLevel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblGameLevel.setBounds(542, 329, 139, 26);
        contentPane.add(lblGameLevel);



        gameName = new JTextField();
        gameName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gameName.setBounds(214, 151, 228, 50);
        contentPane.add(gameName);
        gameName.setColumns(10);

        sportCategory = new JTextField();
        sportCategory.setFont(new Font("Tahoma", Font.PLAIN, 20));
        sportCategory.setBounds(214, 235, 228, 50);
        contentPane.add(sportCategory);
        sportCategory.setColumns(10);

        country = new JTextField();
        country.setFont(new Font("Tahoma", Font.PLAIN, 20));
        country.setBounds(214, 320, 228, 50);
        contentPane.add(country);
        country.setColumns(10);

        city = new JTextField();
        city.setFont(new Font("Tahoma", Font.PLAIN, 20));
        city.setBounds(214, 420, 228, 50);
        contentPane.add(city);
        city.setColumns(10);

        date = new JTextField();
        date.setFont(new Font("Tahoma", Font.PLAIN, 20));
        date.setBounds(707, 151, 228, 50);
        contentPane.add(date);
        date.setColumns(10);

        numOfPlayers = new JTextField();
        numOfPlayers.setFont(new Font("Tahoma", Font.PLAIN, 20));
        numOfPlayers.setBounds(707, 235, 228, 50);
        contentPane.add(numOfPlayers);
        numOfPlayers.setColumns(10);

        levelOfPlayers = new JTextField();
        levelOfPlayers.setFont(new Font("Tahoma", Font.PLAIN, 20));
        levelOfPlayers.setBounds(707, 320, 228, 50);
        contentPane.add(levelOfPlayers);


        btnNewButton = new JButton("Create");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String gameNameText = gameName.getText();
                String sportCategoryText = sportCategory.getText();
                String countryText = country.getText();
                String cityText = city.getText();
                String dateText = date.getText();
                String numOfPlayersText = numOfPlayers.getText();
                String levelOfPlayersText = levelOfPlayers.getText();

                Game game = new Game(gameNameText, sportCategoryText, countryText, cityText, dateText, Integer.parseInt(numOfPlayersText), Integer.parseInt(levelOfPlayersText));
                gameList.add(String.valueOf(game));
                int rcUserGames = gameDao.insertUserGames(game, userName);
                int rcGameRegion = gameDao.insertGameRegion(game);
                int rcGameDetails = gameDao.insertGameDetails(game);
                checkGameCreation(rcUserGames, rcGameRegion, rcGameDetails, gameNameText);
            }
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnNewButton.setBounds(489, 447, 324, 74);
        contentPane.add(btnNewButton);
    }

    private void checkGameCreation(int rcUserGames, int rcGameRegion, int rcGameDetails, String game) {
        if (rcUserGames == 0 || rcGameRegion == 0 ||  rcGameDetails == 0) {
            JOptionPane.showMessageDialog(btnNewButton, "The Game is already exist");
        } else {
            JOptionPane.showMessageDialog(btnNewButton,"The Game: " + game + " is successfully created");
            dispose();
        }
    }

}
