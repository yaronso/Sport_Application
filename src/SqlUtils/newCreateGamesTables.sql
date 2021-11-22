USE yaron_db;

CREATE TABLE IF NOT EXISTS countries
( country_id varchar(35) NOT NULL,
  country_name varchar(70) NOT NULL,
  PRIMARY KEY (country_id)
);

CREATE TABLE IF NOT EXISTS cities
( city_name varchar(70) NOT NULL,
  country_id varchar(35) NOT NULL,
  PRIMARY KEY (city_name,country_id),
  FOREIGN KEY (country_id) REFERENCES countries(country_id)
);


CREATE TABLE IF NOT EXISTS game_details
( user_name varchar(15) NOT NULL,
  game_name varchar(35) NOT NULL,
  game_date DATETIME NOT NULL,
  creation_date DATETIME NOT NULL,
  city varchar(70) NOT NULL,
  sport_category varchar(20) NOT NULL,
  players int NOT NULL,
  level int NOT NULL,
  PRIMARY KEY (user_name, game_name, creation_date),
  FOREIGN KEY(city) REFERENCES cities(city_name)
);


CREATE TABLE IF NOT EXISTS match_games
( user_name varchar(35) NOT NULL,
  game_name varchar(15) NOT NULL,
  creation_date DATETIME NOT NULL,
  participant varchar(35) NOT NULL,
  PRIMARY KEY (user_name, game_name, creation_date, participant),
  FOREIGN KEY(user_name, game_name, creation_date) REFERENCES game_details(user_name, game_name, creation_date)
);







