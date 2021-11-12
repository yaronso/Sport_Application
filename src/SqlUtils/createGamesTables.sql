USE yaron_db;

CREATE TABLE IF NOT EXISTS user_games
( game_name varchar(35) NOT NULL,
  user_name varchar(15) NOT NULL,
  sport_category varchar(20) NOT NULL,
  PRIMARY KEY (game_name, user_name)
);

CREATE TABLE IF NOT EXISTS regions
( country varchar(35) NOT NULL,
  city varchar(70) NOT NULL,
  PRIMARY KEY (city)
);

CREATE TABLE IF NOT EXISTS game_region
( game_name varchar(35) NOT NULL,
  country varchar(35) NOT NULL,
  city varchar(70) NOT NULL,
  PRIMARY KEY (game_name, city),
  FOREIGN KEY (game_name) REFERENCES user_games(game_name)
);

CREATE TABLE IF NOT EXISTS game_details
( game_name varchar(35) NOT NULL,
  game_date DATETIME NOT NULL,
  players int NOT NULL,
  level int NOT NULL,
  PRIMARY KEY (game_name, game_date),
  FOREIGN KEY (game_name) REFERENCES user_games(game_name)
);


CREATE TABLE IF NOT EXISTS match_games
( user_name varchar(35) NOT NULL,
  game_name varchar(15) NOT NULL,
  game_date varchar(20) NOT NULL,
  PRIMARY KEY (user_name, game_name)
);






