USE yaron_db;

CREATE TABLE IF NOT EXISTS users
( first_name varchar(20) NOT NULL,
  last_name varchar(30) NOT NULL,
  user_name varchar(15) NOT NULL,
  password varchar(15),
  email_id varchar(50) NOT NULL,
  mobile_number varchar(15) NOT NULL,
  PRIMARY KEY (user_name)
);