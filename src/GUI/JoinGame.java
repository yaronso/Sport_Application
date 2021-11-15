package GUI;

import Dao.GameDao;
import Dao.GameDaoImpl;
import Models.Game;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class JoinGame extends JFrame {

    // CTR - invoke init Components method
    public JoinGame(String userName) {
        initComponents(userName);
    }

    // Retrieve the games table to the application screen
    private void retrieve(GameDao gameDao) {
        DefaultTableModel dm = gameDao.findAllGames();
        jTable1.setModel(dm);
    }

    @SuppressWarnings("unchecked")
    private void initComponents(String userName) {
        // Initialization Objects:
        GameDao gameDao = new GameDaoImpl();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPaneMatchGames = new JScrollPane();
        jTable1 = new javax.swing.JTable();
        matchGamesTable = new javax.swing.JTable();

        // TextFields:
        gameNameTxt = new javax.swing.JTextField();
        sportCategoryTxt = new javax.swing.JTextField();
        cityTxt = new JTextField();
        countryTxt = new javax.swing.JTextField();
        dateTxt = new JTextField();
        playersTxt = new JTextField();
        levelTxt = new JTextField();
        creationDateTxt = new JTextField();

        // JLabels:
        jLabelGameName = new javax.swing.JLabel();
        jLabelSportCategory = new javax.swing.JLabel();
        jLabelCountry = new javax.swing.JLabel();
        jLabelCity = new JLabel();
        jLabelDate = new JLabel();
        jLabelPlayers = new JLabel();
        jLabelGameLevel = new JLabel();
        jLabelGameName.setText("Game Name");
        jLabelSportCategory.setText("Sport Category");
        jLabelCountry.setText("Country");
        jLabelCity.setText("City");
        jLabelDate.setText("Date");
        jLabelPlayers.setText("Players");
        jLabelGameLevel.setText("Level");

        // JButtons:
        retrieveBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        deleteMatch = new JButton();
        clearBtn = new javax.swing.JButton();
        joinGameButton = new javax.swing.JButton();
        showMyGames = new javax.swing.JButton();
        dateButton = new javax.swing.JButton();
        jLabelTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(45, 155, 193));

        // Set the full Games Table
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

        // Set up the match games table
        matchGamesTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        matchGamesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                matchGamesTableMouseClicked(evt);
            }
        });


        dateButton.setText("Pick Date");
        dateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dateTxt.setText(new DatePicker(dateTxt).setPickedDate());
            }
        });


        // Buttons actions performed:
        retrieveBtn.setText("Retrieve");
        retrieveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                retrieveBtnActionPerformed(evt, gameDao);
            }
        });

        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                addBtnActionPerformed(evt, userName, gameDao);
            }
        });

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                updateBtnActionPerformed(evt, userName, gameDao, jTable1);
            }
        });

        Delete.setText("Delete Game");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                DeleteActionPerformed(evt, userName, gameDao, jTable1);
            }
        });

        deleteMatch.setText("Delete Match");
        deleteMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPaneMatchGames, matchGamesTable);
                DeleteMatchActionPerformed(evt, userName, gameDao, matchGamesTable);
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                clearBtnActionPerformed(evt);
            }
        });

        joinGameButton.setText("Join A Game");
        joinGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                joinGameBtnActionPerformed(evt, userName, gameDao);
            }
        });

        showMyGames.setText("Your Games");
        showMyGames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setViewTable(jScrollPaneMatchGames, matchGamesTable);
                retrieveMatches(gameDao, userName);
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addComponent(jScrollPaneMatchGames, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(jLabelSportCategory))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(gameNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(sportCategoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(21, 21, 21))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelCountry)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(countryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))

                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelCity)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(cityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelDate)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(dateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(dateButton)

                                                                )
                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelPlayers)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(playersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                                        .addComponent(jLabelGameLevel)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(levelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(0, 9, Short.MAX_VALUE))


                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(deleteMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(retrieveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(joinGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(showMyGames, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))));

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
                                                                        .addComponent(gameNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(27, 27, 27)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelSportCategory)
                                                                        .addComponent(sportCategoryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52))

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelCountry)
                                                                        .addComponent(countryTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelCity)
                                                                        .addComponent(cityTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelDate)
                                                                        .addComponent(dateTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(dateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) )
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelPlayers)
                                                                        .addComponent(playersTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52)

                                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelGameLevel)
                                                                        .addComponent(levelTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addBtn)
                                                        .addComponent(retrieveBtn))
                                                .addGap(51, 51, 51)

                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(updateBtn)
                                                        .addComponent(Delete)
                                                        .addComponent(deleteMatch))

                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(clearBtn)
                                                        .addComponent(joinGameButton)
                                                        .addComponent(showMyGames))
                                        )
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                                .addComponent(jScrollPaneMatchGames, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)));

        jLabelTitle.setText("Games Management");
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


    private void DeleteMatchActionPerformed(ActionEvent evt, String participant, GameDao gameDao, JTable matchGamesTable) {
        String[] options = {"Yes", "No"};
        int rs = JOptionPane.showOptionDialog(null, "Sure To Delete?", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (rs == 0) {
            int currRow = matchGamesTable.getSelectedRow();
            // The delete query can be only executed by the user who created the game
            String gameName = matchGamesTable.getValueAt(currRow, 1).toString();
            if (gameDao.deleteFromMatchGames(participant, gameName)) { /* if succeeded to delete from matches table */
                JOptionPane.showMessageDialog(null, "Deleted Updated");
                retrieveMatches(gameDao, participant);
                // If the user deleted a match from his match tables, update the game level with ++1 at the game management table.
                int currNumPlayers = gameDao.getCurrNumPlayers(gameName);
                if(currNumPlayers != -1) { /* if succeeded to retrieve the current number of players */
                    if (gameDao.updateGameLevel(gameName, currNumPlayers, "UpLevel")) {
                        retrieveMatches(gameDao, participant);
                    }
                    retrieve(gameDao);
                }
            } else {
                JOptionPane.showMessageDialog(null, "The game was not deleted");
            }
        }
    }

    // Display the Match Games of the specific user
    private void retrieveMatches(GameDao gameDao, String participant) {
        DefaultTableModel dm = gameDao.findMatches(participant);
        matchGamesTable.setModel(dm);
    }


    // Set the selected values to the relevant text fields of match games table
    private void matchGamesTableMouseClicked(MouseEvent evt) {
        String game = matchGamesTable.getValueAt(matchGamesTable.getSelectedRow(), 0).toString();
        String date = matchGamesTable.getValueAt(matchGamesTable.getSelectedRow(), 1).toString();
        gameNameTxt.setText(game);
        dateTxt.setText(date);
    }


    // When a user click to join a new game
    // "INSERT IGNORE INTO match_games(user_name, game_name, creation_date, participant) VALUES(?, ?, ?, ?)";
    private void joinGameBtnActionPerformed(ActionEvent evt, String participant, GameDao gameDao) {
        // TODO - add a check that the game does not exist in match
        // SELECT user_name, creation_date FROM game_details WHERE game_name = gameNameTxt.getText()

        String userName = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
        String creationDate = jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();
        gameDao.insertToMatchGameTable(userName, gameNameTxt.getText(), creationDate, participant);  // insert a record to match_games table
        gameDao.updateGameLevel(gameNameTxt.getText(), Integer.parseInt(playersTxt.getText()) , "DownLevel"); // --1 the game number of players
        JOptionPane.showMessageDialog(null, "Successfully joined");
        retrieve(gameDao);
        retrieveMatches(gameDao, participant);
    }

    // Retrieve the game details
    private void retrieveBtnActionPerformed(ActionEvent evt, GameDao gameDao) {
        retrieve(gameDao);
    }

    // Add/Create a new game
    private void addBtnActionPerformed(ActionEvent evt, String userName, GameDao gameDao) {
        Game game = new Game(gameNameTxt.getText(), sportCategoryTxt.getText(), countryTxt.getText(), cityTxt.getText(), dateTxt.getText(), Integer.parseInt(playersTxt.getText()), Integer.parseInt(levelTxt.getText()));
        //gameDao.insertUserGames(game, userName);
        //gameDao.insertGameRegion(game);
        gameDao.insertGameDetails(userName, game);
        retrieve(gameDao);
    }


    // Set the selected values to the relevant text fields of game management table
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        String game = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
        String sport = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
        String country = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString();
        String city = jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString();
        String date = jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();
        String players = jTable1.getValueAt(jTable1.getSelectedRow(), 6).toString();
        String level = jTable1.getValueAt(jTable1.getSelectedRow(), 7).toString();
        gameNameTxt.setText(game);
        sportCategoryTxt.setText(sport);
        countryTxt.setText(country);
        cityTxt.setText(city);
        dateTxt.setText(date);
        playersTxt.setText(players);
        levelTxt.setText(level);
    }

    // Execute update query for all the game tables
    private void updateBtnActionPerformed(ActionEvent evt, String participant, GameDao gameDao, JTable jTable1) {
        int currRow = jTable1.getSelectedRow();
        String userName = jTable1.getValueAt(currRow, 0).toString();
        // The update query can be only executed by the user who created the game
        // TODO - Check that the fields are not null
        if (userName.equals(participant)) {
            System.out.println("currRow selected is " + currRow);
            Game game = new Game(gameNameTxt.getText(), sportCategoryTxt.getText(), countryTxt.getText(), cityTxt.getText(), dateTxt.getText(), Integer.parseInt(playersTxt.getText()), Integer.parseInt(levelTxt.getText()));
            String oldGameName = gameDao.findColumnRow(currRow, "GameName"); // Cursor: The game that the user want to update/replace
            if(gameDao.updateGameFullDetails(game, oldGameName)) {
                JOptionPane.showMessageDialog(null, "Game details were updated");
                // Clear the text fields
                gameNameTxt.setText("");
                sportCategoryTxt.setText("");
                countryTxt.setText("");
                retrieve(gameDao);
            } else {
                JOptionPane.showMessageDialog(null, "Error in updated");
            }
        }  else {
            JOptionPane.showMessageDialog(null, "Not Updated");
        }
    }

    // Execute delete query
    private void DeleteActionPerformed(ActionEvent evt, String currUser, GameDao gameDao, JTable jTable) {
        String[] options = {"Yes", "No"};
        int rs = JOptionPane.showOptionDialog(null, "Sure To Delete?", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (rs == 0) {
            int currRow = jTable.getSelectedRow();
            String userName = jTable.getValueAt(currRow, 0).toString();
            System.out.println(userName);
            // The delete query can be only executed by the user who created the game
            if (userName.equals(currUser)) {
                String gameName = jTable.getValueAt(currRow, 1).toString();
                if (gameDao.deleteGame(gameName)) {
                    JOptionPane.showMessageDialog(null, "Deleted Updated");
                    gameNameTxt.setText("");
                    sportCategoryTxt.setText("");
                    countryTxt.setText("");
                    cityTxt.setText("");
                    dateTxt.setText("");
                    playersTxt.setText("");
                    levelTxt.setText("");
                    retrieve(gameDao);
                } else {
                    JOptionPane.showMessageDialog(null, "The game was not deleted");
                }
            }
        }
    }



    // clear the text fields and the table content
    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        jTable1.setModel(new DefaultTableModel());
    }

    // Main Function
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }

    // Set the correct table in the application view
    public void setViewTable(JScrollPane jScrollPane,JTable table) {
        jScrollPane.setViewportView(table);
    }


    // labels declarations:
    private javax.swing.JLabel jLabelGameName;
    private javax.swing.JLabel jLabelSportCategory;
    private javax.swing.JLabel jLabelCountry;
    private javax.swing.JLabel jLabelTitle;
    private JLabel jLabelCity;
    private JLabel jLabelDate;
    private JLabel jLabelPlayers;
    private JLabel jLabelGameLevel;


    // Text Fields declarations:
    private javax.swing.JTextField gameNameTxt;
    private javax.swing.JTextField sportCategoryTxt;
    private javax.swing.JTextField countryTxt;
    private javax.swing.JTextField cityTxt;
    private javax.swing.JTextField dateTxt;
    private javax.swing.JTextField playersTxt;
    private javax.swing.JTextField levelTxt;
    private javax.swing.JTextField creationDateTxt;

    // Buttons declarations:
    private javax.swing.JButton retrieveBtn;
    private javax.swing.JButton updateBtn;
    private javax.swing.JButton Delete;
    private javax.swing.JButton deleteMatch;
    private javax.swing.JButton addBtn;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton joinGameButton;
    private javax.swing.JButton showMyGames;
    private javax.swing.JButton dateButton;

    // etc
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneMatchGames;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable matchGamesTable;
}
