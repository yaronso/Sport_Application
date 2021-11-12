package GUI;

import Dao.GameDao;
import Dao.GameDaoImpl;
import Models.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class FindGame extends JFrame {
    private static final long serialVersionUID = 1L;

    public FindGame(Connection connection)
    {
        Init(connection);
    }

    public void Init(Connection connection) {
        // Initialization Objects:
        GameDao gameDao = new GameDaoImpl();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        // TextFields:
        gameNameTxt = new javax.swing.JTextField();
        findGameCityTxt = new javax.swing.JTextField();
        countryTxt = new javax.swing.JTextField();
        findMaxLevelTxt = new JTextField();
        countryMostPlayedText = new JTextField();


        // JLabels:
        jLabelTitle = new javax.swing.JLabel();
        jLabelGameName = new javax.swing.JLabel();
        jLabelFindGamesInCity = new javax.swing.JLabel();
        jLabelFindGamesCountry = new javax.swing.JLabel();
        jLabelFindGamesMaxLevel = new JLabel();
        jLabelMostPlayedSportInCountryPref = new JLabel();
        jLabelMostPlayedSportInCountrySuff = new JLabel();

        jLabelGameName.setText("Find Game according game name:");
        jLabelFindGamesInCity.setText("Find All Games in city:");
        jLabelFindGamesCountry.setText("Find All Games in country:");
        jLabelFindGamesMaxLevel.setText("Find All Games with max level:");
        jLabelMostPlayedSportInCountryPref.setText("The country where");
        jLabelMostPlayedSportInCountrySuff.setText("is most played is");


        // JButtons:
        clearBtn = new JButton();
        findAccGameNameBtn = new JButton();
        findCityBtn = new JButton();
        findCountryBtn = new JButton();
        findMaxLevelBtn = new JButton();
        countryMostPlayedBtn = new JButton();



        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(45, 155, 193));
        // Set the full Games Table
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //jTable1MouseClicked(evt);
            }
        });


        findAccGameNameBtn.setText("Find game");
        findAccGameNameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = gameDao.findByGameName(gameNameTxt.getText());
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findCityBtn.setText("Find All Cities");
        findCityBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = gameDao.findByCityName(findGameCityTxt.getText());
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findCountryBtn.setText("Find All Countries");
        findCountryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = gameDao.findByCountryName(countryTxt.getText());
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        findMaxLevelBtn.setText("Find Max");
        findMaxLevelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = gameDao.findMaxLevelGamesInEachCountry(findMaxLevelTxt.getText());
                jTable1.setModel(dm);
                setViewTable();
            }
        });

        countryMostPlayedBtn.setText("Click");
        countryMostPlayedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DefaultTableModel dm = gameDao.findCountryMostPlayedSport(countryMostPlayedText.getText());
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
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        // Horizontal Group
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE) // for the 1st table
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(jLabelFindGamesInCity))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)


                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(gameNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findGameCityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18))

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(findAccGameNameBtn,  javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(findCityBtn,  javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGap(21, 21, 21))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelFindGamesCountry)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findCountryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelFindGamesMaxLevel)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findMaxLevelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(findMaxLevelBtn))


                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelMostPlayedSportInCountryPref)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryMostPlayedText, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(jLabelMostPlayedSportInCountrySuff)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryMostPlayedBtn))
                                                                .addGap(0, 9, Short.MAX_VALUE)))


                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                        .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))));

        // Vertical Group
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(gameNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findAccGameNameBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(27, 27, 27)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesInCity)
                                                                        .addComponent(findGameCityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findCityBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52))

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesCountry)
                                                                        .addComponent(countryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findCountryBtn)))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelFindGamesMaxLevel)
                                                                        .addComponent(findMaxLevelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(findMaxLevelBtn))
                                                                .addGap(52, 52, 52)


                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelMostPlayedSportInCountryPref)
                                                                        .addComponent(countryMostPlayedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(jLabelMostPlayedSportInCountrySuff)
                                                                        .addComponent(countryMostPlayedBtn))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(clearBtn))
                                                                .addGap(51, 51, 51))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addGap(38, 38, 38)
                                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)));

        jLabelTitle.setText("Find Games");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(234, 234, 234)
                                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabelTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
