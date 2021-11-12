USE yaron_db;

-- The following two tables will be used by the loaded dataset
CREATE TABLE IF NOT EXISTS countries
( country_id varchar(35) NOT NULL,
  country_name varchar(70) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cities
( city_name varchar(70) NOT NULL,
  PRIMARY KEY (city_name),
  FOREIGN KEY (city_name) REFERENCES countries(country_id)
);


-- Other tables:
CREATE TABLE IF NOT EXISTS game_details
( user_name varchar(15) NOT NULL, -- The user that created the game
  game_name varchar(35) NOT NULL,
  game_date DATETIME NOT NULL,
  creation_date DATETIME NOT NULL,
  city varchar(70) NOT NULL,
  sport_category varchar(20) NOT NULL,
  players int NOT NULL,
  level int NOT NULL,
  PRIMARY KEY (user_name, game_name, creation_date),
  FOREIGN KEY(city) REFERENCES cities(city_name),
  FOREIGN KEY (user_name, game_name, creation_date) REFERENCES match_games(user_name, game_name, creation_date)
);


CREATE TABLE IF NOT EXISTS match_games
( user_name varchar(35) NOT NULL, -- The user that created the game
  game_name varchar(15) NOT NULL,
  creation_date varchar(20) NOT NULL,
  participant varchar(35) NOT NULL, -- The user that joined to the game
  PRIMARY KEY (user_name, game_name, creation_date, participant)
  --TODO: check if FOREIGN KEY to game_details table is needed.
);






