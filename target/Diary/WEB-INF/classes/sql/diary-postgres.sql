
CREATE TABLE books(
   book_id  SERIAL PRIMARY KEY,
   book_title        char(255)      NOT NULL,
   author            char(255)       NOT NULL,
   book_description        text
);
INSERT INTO books VALUES (1,'\"Atlas shrugged\"','Ayn Rand','Novel by Ayn'),(2,'water','fsd','sdfsd');

CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  username char(50) NOT NULL,
  password character varying(64) NOT NULL,
  enabled char(1) NOT NULL,
  email char(45) NOT NULL
) 
INSERT INTO users VALUES (1,'user1','$2a$10$zaXx4XNKJCaG3FIkjOmQuuT3jEL215SiDIUqXeZ12ykMirT7xngvW',1,'user1@gmail.eu'),(2,'user2','123456',1,'user2@gmail.eu'),(3,'user3','123456',1,'user3@gmail.eu'),(21,'kverchi','$2a$10$zaXx4XNKJCaG3FIkjOmQuuT3jEL215SiDIUqXeZ12ykMirT7xngvW',1,'kverchi@hotmail.com');


CREATE TABLE roles (
  role_id SERIAL PRIMARY KEY,
  role char(45) NOT NULL
) 
INSERT INTO roles VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_TRAVELLER');

CREATE TABLE user_roles (
  user_id int NOT NULL references users(user_id),
  role_id int NOT NULL references roles(role_id),
  PRIMARY KEY (user_id, role_id)
)
INSERT INTO user_roles VALUES (1,1),(1,3),(2,2),(3,3);

CREATE TABLE posts (
  post_id SERIAL NOT NULL,
  title char(100) NOT NULL,
  text text NOT NULL,
  description char(255) DEFAULT NULL,
  sight_id int DEFAULT NULL,
  user_id int DEFAULT NULL references users(user_id),
  post_datetime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (post_id)
) 
INSERT INTO posts VALUES (1,'Deep forest','It is so fun hiding in the forest.','About TShirt',86,2,'2017-03-18 21:10:49'),(40,'Sunny post :)','<p>Nice book for life</p>','About interesting book',87,21,'2017-04-24 13:38:35'),(41,'Hello','Let is be friends?!','Hello, who are you?',86,1,'2017-03-18 21:10:49'),(42,'Coffee morning','<p>Depresso :)</p>','Coffee is it good?',87,21,'2017-04-24 13:44:01'),(43,'dfsdf','sdfsdf','werwerr',86,3,'2017-03-18 21:10:49');


CREATE TABLE comments (
  comment_id SERIAL PRIMARY KEY,
  comment_datetime timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  text char(255) NOT NULL,
  post_id INT NOT NULL references posts(post_id),
  user_id INT NOT NULL references users(user_id)
) 
INSERT INTO comments VALUES (1,'2017-03-09 20:11:24','Good job',42,21),(2,'2017-03-29 14:06:52','hello',43,21),(3,'2017-03-29 14:23:17','hello',42,21),(4,'2017-03-29 14:24:23','hello again',42,21),(5,'2017-03-29 14:37:45','I am your friend :)',41,21),(8,'2017-03-29 14:45:21','Let is go for lunch!',42,21),(9,'2017-03-29 14:47:54','Potatoes with garlic!',42,21),(10,'2017-03-29 14:51:04','And buckwheat with broccoli.',42,21);


CREATE TABLE countries (
  country_code char(4) NOT NULL,
  country_name char(45) DEFAULT NULL,
  img_path char(45) DEFAULT NULL,
  PRIMARY KEY (country_code),
  UNIQUE (country_code)
) 
INSERT INTO countries VALUES ('cz','Czechia','CzechRepublic.png'),('it','Italy','Italy.png'),('ua','Ukraine','Ukraine.png'),('uk','United Kingdom','UnitedKingdom.png');

CREATE TABLE countries_sights (
  sight_id SERIAL NOT NULL,
  sight_label char(100) NOT NULL,
  country_code char(2) NOT NULL references countries(country_code),
  img_url char(100) DEFAULT NULL,
  description char(300) DEFAULT NULL,
  PRIMARY KEY (sight_id)
)
INSERT INTO countries_sights VALUES (86,'Prague','cz','fairy-tail-prague.jpg','Like in fairy tail'),(87,'Brno','cz',NULL,'Industrialization'),(88,'Liberec','cz','','Dream about you');

CREATE TABLE password_change_requests (
  UUID char NOT NULL,
  user_id int NOT NULL references users(user_id),
  created_time timestamp NOT NULL,
  isUUIDused char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (UUID)
)

