package GUI;

import Models.Game;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import Controllers.GameController;

public class JoinGame extends JFrame {
    private static final long serialVersionUID = 1L;
    // CTR - invoke init Components method
    public JoinGame(String userName) throws IOException {
        initComponents(userName);
    }

    // Retrieve the games table to the application screen
    private void retrieve(GameController gameController) throws SQLException {
        DefaultTableModel dm = gameController.findAllGames();
        jTable1.setModel(dm);
    }

    @SuppressWarnings("unchecked")
    private void initComponents(String userName) throws IOException {
        // Initialization Objects:
        GameController gameController = new GameController();
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jScrollPane1.setBorder(BorderFactory.createTitledBorder ("Worldwide Games"));
        jScrollPaneMatchGames = new JScrollPane();
        jScrollPaneMatchGames.setBorder(BorderFactory.createTitledBorder ("Your Matches"));
        jTable1 = new JTable();
        matchGamesTable = new JTable();

        // TextFields:
        gameNameTxt = new JTextField();
        sportCategoryTxt = new JTextField();
        cityTxt = new JTextField();
        countryTxt = new JTextField();
        dateTxt = new JTextField();
        playersTxt = new JTextField();
        levelTxt = new JTextField();
        creationDateTxt = new JTextField();

        // JLabels:
        jLabelGameName = new JLabel();
        jLabelGameName.setForeground(Color.black);
        jLabelGameName.setBackground(Color.white);
        jLabelGameName.setOpaque(true);
        jLabelSportCategory = new JLabel();
        jLabelSportCategory.setForeground(Color.black);
        jLabelSportCategory.setBackground(Color.white);
        jLabelSportCategory.setOpaque(true);
        jLabelCountry = new JLabel();
        jLabelCountry.setForeground(Color.black);
        jLabelCountry.setBackground(Color.white);
        jLabelCountry.setOpaque(true);
        jLabelCity = new JLabel();
        jLabelCity.setForeground(Color.black);
        jLabelCity.setBackground(Color.white);
        jLabelCity.setOpaque(true);
        jLabelDate = new JLabel();
        jLabelDate.setForeground(Color.black);
        jLabelDate.setBackground(Color.white);
        jLabelDate.setOpaque(true);
        jLabelPlayers = new JLabel();
        jLabelPlayers.setForeground(Color.black);
        jLabelPlayers.setBackground(Color.white);
        jLabelPlayers.setOpaque(true);
        jLabelGameLevel = new JLabel();
        jLabelGameLevel.setForeground(Color.black);
        jLabelGameLevel.setBackground(Color.white);
        jLabelGameLevel.setOpaque(true);
        jLabelGameName.setText("Game Name");
        jLabelSportCategory.setText("Sport Category");
        jLabelCountry.setText("Country");
        jLabelCity.setText("City");
        jLabelDate.setText("Date");
        jLabelPlayers.setText("Players");
        jLabelGameLevel.setText("Level");

        // JButtons:
        retrieveBtn = new JButton();
        addBtn = new JButton();
        updateBtn = new JButton();
        Delete = new JButton();
        deleteMatch = new JButton();
        clearBtn = new JButton();
        joinGameButton = new JButton();
        showMyGames = new JButton();
        dateButton = new JButton();
        jLabelTitle = new JLabel();

        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jPanel1.setBackground(new java.awt.Color(45, 155, 193));


        // Set the full Games Table
        jTable1.setModel(new DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });

        // Set up the match games table
        matchGamesTable.setModel(new DefaultTableModel(
                new Object [][] {}, new String [] {} ));
        matchGamesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
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
        retrieveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                try {
                    retrieveBtnActionPerformed(evt, gameController);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        addBtn.setText("Add");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                try {
                    addBtnActionPerformed(evt, userName, gameController, addBtn);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        updateBtn.setText("Update");
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                try {
                    updateBtnActionPerformed(evt, userName, gameController, jTable1, updateBtn);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Delete.setText("Delete Game");
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                try {
                    DeleteActionPerformed(evt, userName, gameController, jTable1, Delete);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        deleteMatch.setText("Delete Match");
        deleteMatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPaneMatchGames, matchGamesTable);
                try {
                    DeleteMatchActionPerformed(evt, userName, gameController, matchGamesTable, deleteMatch);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                clearBtnActionPerformed(evt);
            }
        });

        joinGameButton.setText("Join A Game");
        joinGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPane1, jTable1);
                try {
                    joinGameBtnActionPerformed(evt, userName, gameController, joinGameButton);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        showMyGames.setText("Your Games");
        showMyGames.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setViewTable(jScrollPaneMatchGames, matchGamesTable);
                try {
                    retrieveMatches(gameController, userName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addComponent(jScrollPaneMatchGames, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabelGameName)
                                                                        .addComponent(jLabelSportCategory))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(gameNameTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(sportCategoryTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(21, 21, 21))

                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabelCountry)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(countryTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))

                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabelCity)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(cityTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabelDate)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(dateTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(dateButton)

                                                        )
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabelPlayers)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(playersTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(jLabelGameLevel)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(levelTxt, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 9, Short.MAX_VALUE))


                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(updateBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(Delete, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(deleteMatch, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(addBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(retrieveBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addComponent(clearBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(joinGameButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(showMyGames, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))))));
        // Add the background image
        BufferedImage myPicture = ImageIO.read(new File("src/GUI/globe-3411506_1920.jpg"));
        JLabel picJLabel = new JLabel(new ImageIcon(myPicture));
        picJLabel.setBounds(0, 0,1900, 1000);
        jPanel2.add(picJLabel);

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
                                                                        .addComponent(gameNameTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(27, 27, 27)

                                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(jLabelSportCategory)
                                                                        .addComponent(sportCategoryTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(52, 52, 52))

                                                        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(jLabelCountry)
                                                                .addComponent(countryTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabelCity)
                                                        .addComponent(cityTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabelDate)
                                                        .addComponent(dateTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(dateButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE) )
                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabelPlayers)
                                                        .addComponent(playersTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabelGameLevel)
                                                        .addComponent(levelTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(52, 52, 52)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addBtn)
                                                        .addComponent(retrieveBtn))
                                                .addGap(51, 51, 51)

                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(updateBtn)
                                                        .addComponent(Delete)
                                                        .addComponent(deleteMatch))

                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(clearBtn)
                                                        .addComponent(joinGameButton)
                                                        .addComponent(showMyGames))
                                        )
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(38, 38, 38)
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                                                .addComponent(jScrollPaneMatchGames, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)));

        jLabelTitle.setText("Games Management");
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


    // Delete from match games table.
    private void DeleteMatchActionPerformed(ActionEvent evt, String participant, GameController gameController, JTable matchGamesTable, JButton deleteMatch) throws SQLException {
        String[] options = {"Yes", "No"};
        int rs = JOptionPane.showOptionDialog(null, "Sure To Delete?", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (rs == 0) {
            int currRow = matchGamesTable.getSelectedRow();
            if (Utils.PropertiesReaders.isAnyObjectNull(currRow) || currRow == -1) {
                JOptionPane.showMessageDialog(deleteMatch, "Delete Match Error: You must choose a match game from your games list to delete.");
                return;
            }
            // The delete query can be only executed by the user who created the game , update the game level with ++1 at the game management table.
            String gameName = matchGamesTable.getValueAt(currRow, 1).toString();
            if (gameController.deleteFromMatchGames(participant, gameName)) { /* if succeeded to delete from matches table Transactional */
                JOptionPane.showMessageDialog(null, "Deleted Updated");
                retrieveMatches(gameController, participant);
                retrieve(gameController);
            } else {
                JOptionPane.showMessageDialog(null, "The game was not deleted");
            }
        }
    }

    // Display the Match Games of the specific user
    private void retrieveMatches(GameController gameController, String participant) throws SQLException {
        DefaultTableModel dm = gameController.findMatches(participant);
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
    private void joinGameBtnActionPerformed(ActionEvent evt, String participant, GameController gameController, JButton joinGameButton) throws SQLException {
        int currRow = jTable1.getSelectedRow();
        if (Utils.PropertiesReaders.isAnyObjectNull(currRow) || currRow == -1) {
            JOptionPane.showMessageDialog(joinGameButton, "Join Game Error: You must choose a game from the entire games list to join to.");
            return;
        }
        String userName = jTable1.getValueAt(currRow, 0).toString();
        String creationDate = jTable1.getValueAt(currRow, 8).toString();
        // TODO - check the user does not have this match game in its table!
        if(!gameController.checkIfMatchExists(userName, gameNameTxt.getText(), creationDate, participant)) {
            JOptionPane.showMessageDialog(null, "You're already signed up to this game.");
        } else {
            if(gameController.insertToMatchGameTableAndDownPlayers(userName, gameNameTxt.getText(), creationDate, participant, Integer.parseInt(playersTxt.getText()))) { // Transactional Function.
                JOptionPane.showMessageDialog(null, "Successfully joined.");
            } else {
                JOptionPane.showMessageDialog(null, "Problem was occurred while join game.");
            }
        }
        retrieve(gameController);
        retrieveMatches(gameController, participant);
    }

    // Retrieve the game details
    private void retrieveBtnActionPerformed(ActionEvent evt, GameController gameController) throws SQLException {
        retrieve(gameController);
    }

    // Add/Create a new game.
    private void addBtnActionPerformed(ActionEvent evt, String userName, GameController gameController, JButton addBtn) throws SQLException {
        String gameName = gameNameTxt.getText();
        String category = sportCategoryTxt.getText();
        String country = countryTxt.getText();
        String city = cityTxt.getText();
        String date = dateTxt.getText();
        String players = playersTxt.getText();
        String level = levelTxt.getText();
        if (Utils.PropertiesReaders.isAnyObjectNull(gameName, category, country, city, date, players, level)) {
            JOptionPane.showMessageDialog(addBtn, "Add game error: You must fill all the game details.");
            return;
        }
        if (!gameController.isCountryOrCityValid(country, "country")) { // Verify that the input country exists in the DB.
            JOptionPane.showMessageDialog(addBtn, "Add game error: Please choose a country that exists.");
            return;
        } if (!gameController.isCountryOrCityValid(city, "city")) { // Verify that the input city exists in the DB.
            JOptionPane.showMessageDialog(addBtn, "Add game error: Please choose a city that exists.");
            return;
        }
        Game game = new Game(gameName, category, country, city, date, Integer.parseInt(players), Integer.parseInt(level));
        if (gameController.insertGameDetails(userName, game)) { // Transactional.
            JOptionPane.showMessageDialog(addBtn, "Add Game Succeeded.");
            retrieve(gameController);
            retrieveMatches(gameController, userName);
        } else {
            JOptionPane.showMessageDialog(addBtn, "Add game error: Game Creation Was Failed.");
        }
    }


    // Set the selected values to the relevant text fields of game management table
    private void jTable1MouseClicked(MouseEvent evt) {
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
    private void updateBtnActionPerformed(ActionEvent evt, String participant, GameController gameController, JTable jTable1, JButton updateBtn) throws SQLException {
        int currRow = jTable1.getSelectedRow();
        if (Utils.PropertiesReaders.isAnyObjectNull(currRow) || currRow == -1) {
            JOptionPane.showMessageDialog(updateBtn, "Update Game Error: You must choose a game from the entire games list to update.");
            return;
        }
        String country = countryTxt.getText();
        String city = cityTxt.getText();
        if (!gameController.isCountryOrCityValid(country, "country")) { // Verify that the input country exists in the DB.
            JOptionPane.showMessageDialog(updateBtn, "Update game error: Please choose a country that exists.");
            return;
        } if (!gameController.isCountryOrCityValid(city, "city")) { // Verify that the input city exists in the DB.
            JOptionPane.showMessageDialog(updateBtn, "Update game error: Please choose a city that exists.");
            return;
        }
        String userName = jTable1.getValueAt(currRow, 0).toString();
        // The update query can be only executed by the user who created the game
        if (userName.equals(participant)) {
            System.out.println("currRow selected is " + currRow);
            Game game = new Game(gameNameTxt.getText(), sportCategoryTxt.getText(), countryTxt.getText(), cityTxt.getText(), dateTxt.getText(), Integer.parseInt(playersTxt.getText()), Integer.parseInt(levelTxt.getText()));
            String oldGameName = gameController.findColumnRow(currRow, "GameName"); // Cursor: The game that the user want to update/replace
            if(gameController.updateGameFullDetails(game, oldGameName)) { // Transactional.
                JOptionPane.showMessageDialog(null, "Game details were updated");
                // Clear the text fields
                gameNameTxt.setText("");
                sportCategoryTxt.setText("");
                countryTxt.setText("");
                retrieve(gameController);
                retrieveMatches(gameController, userName);
            } else {
                JOptionPane.showMessageDialog(null, "Update Error: General error was occurred in update.");
            }
        }  else {
            JOptionPane.showMessageDialog(null, "Update Error: You Are Not The Game Owner.");
        }
    }

    // Execute delete game from the entire games list.
    private void DeleteActionPerformed(ActionEvent evt, String currUser, GameController gameController, JTable jTable, JButton delete) throws SQLException {
        String[] options = {"Yes", "No"};
        int rs = JOptionPane.showOptionDialog(null, "Sure To Delete?", "Delete Confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (rs == 0) {
            int currRow = jTable.getSelectedRow();
            if (Utils.PropertiesReaders.isAnyObjectNull(currRow) || currRow == -1) {
                JOptionPane.showMessageDialog(delete, "You must choose a game to delete");
                return;
            }
            String userName = jTable.getValueAt(currRow, 0).toString();
            // The delete query can be only executed by the user who created the game
            if (userName.equals(currUser)) {
                String gameName = jTable.getValueAt(currRow, 1).toString();
                String creationDate = jTable.getValueAt(currRow, 8).toString();
                System.out.println(creationDate);
                if (gameController.deleteGame(gameName, creationDate)) {
                    JOptionPane.showMessageDialog(null, "Deleted Updated");
                    gameNameTxt.setText("");
                    sportCategoryTxt.setText("");
                    countryTxt.setText("");
                    cityTxt.setText("");
                    dateTxt.setText("");
                    playersTxt.setText("");
                    levelTxt.setText("");
                    retrieve(gameController);
                    retrieveMatches(gameController, userName);
                } else {
                    JOptionPane.showMessageDialog(null, "The game was not deleted");
                }
            }
        }
    }


    // clear the text fields of both tables.
    private void clearBtnActionPerformed(ActionEvent evt) {
        jTable1.setModel(new DefaultTableModel());
        matchGamesTable.setModel(new DefaultTableModel());
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
    private JLabel jLabelGameName;
    private JLabel jLabelSportCategory;
    private JLabel jLabelCountry;
    private JLabel jLabelTitle;
    private JLabel jLabelCity;
    private JLabel jLabelDate;
    private JLabel jLabelPlayers;
    private JLabel jLabelGameLevel;


    // Text Fields declarations:
    private JTextField gameNameTxt;
    private JTextField sportCategoryTxt;
    private JTextField countryTxt;
    private JTextField cityTxt;
    private JTextField dateTxt;
    private JTextField playersTxt;
    private JTextField levelTxt;
    private JTextField creationDateTxt;

    // Buttons declarations:
    private JButton retrieveBtn;
    private JButton updateBtn;
    private JButton Delete;
    private JButton deleteMatch;
    private JButton addBtn;
    private JButton clearBtn;
    private JButton joinGameButton;
    private JButton showMyGames;
    private JButton dateButton;

    // etc
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPaneMatchGames;
    private JTable jTable1;
    private JTable matchGamesTable;
}