package GUI;

import Dao.GameDao;
import Dao.GameDaoImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.sql.SQLException;

public class FindGame extends JFrame {
    private static final long serialVersionUID = 1L;

    public FindGame() throws IOException {
        Init();
    }

    public void Init() throws IOException {
        // Initialization Objects:
        GameDao gameDao = new GameDaoImpl();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();

        // TextFields:
        gameNameTxt = new JTextField();
        findGameCityTxt = new JTextField();
        countryTxt = new JTextField();
        findMaxLevelTxt = new JTextField();
        countryMostPlayedText = new JTextField();

        // JLabels:
        jLabelTitle = new JLabel();
        jLabelGameName = new JLabel();
        jLabelFindGamesInCity = new JLabel();
        jLabelFindGamesCountry = new JLabel();
        jLabelFindGamesMaxLevel = new JLabel();
        jLabelMostPlayedSportInCountryPref = new JLabel();
        jLabelMostPlayedSportInCountrySuff = new JLabel();

        jLabelGameName.setText("Find Game according game name:");
        jLabelFindGamesInCity.setText("Find All Games in city:");
        jLabelFindGamesCountry.setText("Find All Games in country:");
        jLabelFindGamesMaxLevel.setText("Find All Games with max level Under Sport Category:");
        jLabelMostPlayedSportInCountryPref.setText("The country where");
        jLabelMostPlayedSportInCountrySuff.setText("is most played is");

        // JButtons:
        clearBtn = new JButton();
        findAccGameNameBtn = new JButton();
        findCityBtn = new JButton();
        findCountryBtn = new JButton();
        findMaxLevelBtn = new JButton();
        countryMostPlayedBtn = new JButton();


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(45, 155, 193));
        // Set the full Games Table
        jTable1.setModel(new DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //jTable1MouseClicked(evt);
            }
        });


        findAccGameNameBtn.setText("Find game");
        findAccGameNameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = null;
                String gameName = gameNameTxt.getText();
                try {
                    if(Utils.PropertiesReaders.isAnyObjectNull(gameName)) {
                        JOptionPane.showMessageDialog(findAccGameNameBtn, "Find Game Error: You must write the game name.");
                        return;
                    }
                    dm = gameDao.findByGameName(gameName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findCityBtn.setText("Find All Cities");
        findCityBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = null;
                String city = findGameCityTxt.getText();
                try {
                    if(Utils.PropertiesReaders.isAnyObjectNull(city)) {
                        JOptionPane.showMessageDialog(findCityBtn, "Find City Error: You must write a city.");
                        return;
                    }
                    dm = gameDao.findByCityName(city);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findCountryBtn.setText("Find All Countries");
        findCountryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = null;
                String country = countryTxt.getText();
                try {
                    if(Utils.PropertiesReaders.isAnyObjectNull(country)) {
                        JOptionPane.showMessageDialog(findCountryBtn, "Find Country Error: You must write a country.");
                        return;
                    }
                    dm = gameDao.findByCountryName(country);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                assert dm != null;
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findMaxLevelBtn.setText("Find Max");
        findMaxLevelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = null;
                String max = findMaxLevelTxt.getText();
                try {
                    if(Utils.PropertiesReaders.isAnyObjectNull(max)) {
                        JOptionPane.showMessageDialog(findMaxLevelBtn, "Find Max Level Error: You must write the game's category.");
                        return;
                    }
                    dm = gameDao.findMaxLevelGamesInEachCountry(max);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        countryMostPlayedBtn.setText("Click");
        countryMostPlayedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = null;
                String mostPlayedCountry = countryMostPlayedText.getText();
                try {
                    if(Utils.PropertiesReaders.isAnyObjectNull(mostPlayedCountry)) {
                        JOptionPane.showMessageDialog(countryMostPlayedBtn, "Most Played Category In Country Error: You must write the Sport Category.");
                        return;
                    }
                    dm = gameDao.findCountryMostPlayedSport(mostPlayedCountry);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                jTable1.setModel(dm);
                setViewTable();
            }
        });


        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable();
                clearBtnActionPerformed(evt);
            }
        });


        // Set the screen layout and its components:
        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        // Horizontal Group
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE) // for the 1st table
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(jLabelFindGamesInCity))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)


                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(gameNameTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findGameCityTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18))

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(findAccGameNameBtn,  GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(findCityBtn,  GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))

                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGap(21, 21, 21))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelFindGamesCountry)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findCountryBtn, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelFindGamesMaxLevel)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findMaxLevelTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findMaxLevelBtn))


                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelMostPlayedSportInCountryPref)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryMostPlayedText, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(jLabelMostPlayedSportInCountrySuff)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryMostPlayedBtn))
                                                                .addGap(0, 9, Short.MAX_VALUE)))


                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                        .addComponent(clearBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))));

        // Vertical Group
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(gameNameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findAccGameNameBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(27, 27, 27)

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesInCity)
                                                                        .addComponent(findGameCityTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findCityBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52))

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesCountry)
                                                                        .addComponent(countryTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findCountryBtn)))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesMaxLevel)
                                                                        .addComponent(findMaxLevelTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findMaxLevelBtn))
                                                                .addGap(52, 52, 52)


                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelMostPlayedSportInCountryPref)
                                                                        .addComponent(countryMostPlayedText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabelMostPlayedSportInCountrySuff)
                                                                        .addComponent(countryMostPlayedBtn))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(clearBtn))
                                                                .addGap(51, 51, 51))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addGap(38, 38, 38)
                                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)));

        jLabelTitle.setText("Find Games");
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(234, 234, 234)
                                .addComponent(jLabelTitle, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabelTitle)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>

    // performed when clicking the clean button
    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        jTable1.setModel(new DefaultTableModel());
    }

    // Set the correct table in the application view
    public void setViewTable(){
        jScrollPane1.setViewportView(jTable1);
    }

    // labels declarations:
    JLabel jLabelTitle;
    JLabel jLabelGameName;
    JLabel jLabelFindGamesInCity;
    JLabel jLabelFindGamesCountry;
    JLabel jLabelFindGamesMaxLevel;
    JLabel jLabelMostPlayedSportInCountryPref;
    JLabel jLabelMostPlayedSportInCountrySuff;

    // Text Fields declarations:
    JTextField gameNameTxt;
    JTextField findGameCityTxt;
    JTextField countryTxt;
    JTextField findMaxLevelTxt;
    JTextField countryMostPlayedText;

    // Buttons declarations:
    JButton clearBtn;
    JButton findAccGameNameBtn;
    JButton findCityBtn;
    JButton findCountryBtn;
    JButton findMaxLevelBtn;
    JButton countryMostPlayedBtn;

    // etc
    JPanel jPanel1;
    JPanel jPanel2;
    JScrollPane jScrollPane1;
    JTable jTable1;
}
