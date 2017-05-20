CREATE TABLE IF NOT EXISTS books(
   book_id  SERIAL PRIMARY KEY,
   book_title        char(255)      NOT NULL,
   author            char(255)       NOT NULL,
   book_description        text
);
CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  username char(50) NOT NULL,
  password character varying(64) NOT NULL,
  enabled char(1) NOT NULL,
  email char(45) NOT NULL
);
CREATE TABLE roles (
  role_id SERIAL PRIMARY KEY,
  role char(45) NOT NULL
); 
CREATE TABLE user_roles (
  user_id int NOT NULL references users(user_id),
  role_id int NOT NULL references roles(role_id),
  PRIMARY KEY (user_id, role_id)
);
CREATE TABLE posts (
  post_id SERIAL NOT NULL,
  title char(100) NOT NULL,
  text text NOT NULL,
  description char(255) DEFAULT NULL,
  sight_id int DEFAULT NULL,
  user_id int DEFAULT NULL references users(user_id),
  post_datetime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (post_id)
);
CREATE TABLE comments (
  comment_id SERIAL PRIMARY KEY,
  comment_datetime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  text char(255) NOT NULL,
  post_id INT NOT NULL references posts(post_id),
  user_id INT NOT NULL references users(user_id)
);
CREATE TABLE countries (
  country_code char(4) NOT NULL,
  country_name char(45) DEFAULT NULL,
  img_path char(45) DEFAULT NULL,
  PRIMARY KEY (country_code),
  UNIQUE (country_code)
);
CREATE TABLE countries_sights (
  sight_id SERIAL NOT NULL,
  sight_label char(100) NOT NULL,
  country_code char(2) NOT NULL references countries(country_code),
  img_url char(100) DEFAULT NULL,
  description char(300) DEFAULT NULL,
  PRIMARY KEY (sight_id)
);
CREATE TABLE password_change_requests (
  UUID  character varying(255) NOT NULL,
  user_id int NOT NULL references users(user_id),
  created_time timestamp NOT NULL,
  isUUIDused char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (UUID)
);

