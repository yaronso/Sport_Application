USE yaron_db;

CREATE TABLE IF NOT EXISTS users
( first_name varchar(250) NOT NULL,
  last_name varchar(250) NOT NULL,
  user_name varchar(250) NOT NULL,
  password varchar(250),
  email_id varchar(250) NOT NULL,
  mobile_number varchar(250) NOT NULL,
  PRIMARY KEY (user_name)
);