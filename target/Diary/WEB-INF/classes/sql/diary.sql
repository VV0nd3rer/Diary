-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Мар 29 2017 г., 19:16
-- Версия сервера: 10.1.19-MariaDB
-- Версия PHP: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `diary`
--

-- --------------------------------------------------------

--
-- Структура таблицы `books`
--

CREATE TABLE `books` (
  `book_id` int(11) NOT NULL,
  `book_title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `book_description` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `books`
--

INSERT INTO `books` (`book_id`, `book_title`, `author`, `book_description`) VALUES
(1, '"Atlas shrugged"', 'Ayn Rand', 'Novel by Ayn'),
(2, 'water', 'fsd', 'sdfsd');

-- --------------------------------------------------------

--
-- Структура таблицы `comments`
--

CREATE TABLE `comments` (
  `comment_id` int(11) NOT NULL,
  `comment_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `text` varchar(255) NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `comments`
--

INSERT INTO `comments` (`comment_id`, `comment_datetime`, `text`, `post_id`, `user_id`) VALUES
(1, '2017-03-09 20:11:24', 'Good job', 42, 21),
(2, '2017-03-29 14:06:52', 'hello', 43, 21),
(3, '2017-03-29 14:23:17', 'hello', 42, 21),
(4, '2017-03-29 14:24:23', 'hello again', 42, 21),
(5, '2017-03-29 14:37:45', 'I''m your friend :)', 41, 21),
(8, '2017-03-29 14:45:21', 'Let''s go for lunch!', 42, 21),
(9, '2017-03-29 14:47:54', 'Potatoes with garlic!', 42, 21),
(10, '2017-03-29 14:51:04', 'And buckwheat with broccoli.', 42, 21);

-- --------------------------------------------------------

--
-- Структура таблицы `countries`
--

CREATE TABLE `countries` (
  `country_code` varchar(4) NOT NULL,
  `country_name` varchar(45) DEFAULT NULL,
  `img_path` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `countries`
--

INSERT INTO `countries` (`country_code`, `country_name`, `img_path`) VALUES
('cz', 'Czechia', 'CzechRepublic.png'),
('it', 'Italy', 'Italy.png'),
('ua', 'Ukraine', 'Ukraine.png'),
('uk', 'United Kingdom', 'UnitedKingdom.png');

-- --------------------------------------------------------

--
-- Структура таблицы `countries_sights`
--

CREATE TABLE `countries_sights` (
  `sight_id` int(11) NOT NULL,
  `sight_label` varchar(100) NOT NULL,
  `country_code` varchar(2) NOT NULL,
  `img_url` varchar(100) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `countries_sights`
--

INSERT INTO `countries_sights` (`sight_id`, `sight_label`, `country_code`, `img_url`, `description`) VALUES
(86, 'Prague', 'cz', 'fairy-tail-prague.jpg', 'Like in fairy tail');

-- --------------------------------------------------------

--
-- Структура таблицы `password_change_requests`
--

CREATE TABLE `password_change_requests` (
  `UUID` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_time` datetime NOT NULL,
  `isUUIDused` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `password_change_requests`
--

INSERT INTO `password_change_requests` (`UUID`, `user_id`, `created_time`, `isUUIDused`) VALUES
('-12899045', 21, '2017-02-17 20:03:49', 1),
('-1632187161', 21, '2017-02-17 20:01:08', 1),
('-1640102924', 21, '2017-03-08 13:58:01', 0),
('-1666581228', 21, '2017-03-01 21:47:48', 1),
('-1704339970', 21, '2017-02-17 20:10:48', 1),
('1199007491', 21, '2017-03-01 21:52:00', 0),
('1904649547', 21, '2017-02-17 20:08:35', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `posts`
--

CREATE TABLE `posts` (
  `post_id` int(11) NOT NULL,
  `title` varchar(100) NOT NULL,
  `text` text NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `sight_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `post_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `posts`
--

INSERT INTO `posts` (`post_id`, `title`, `text`, `description`, `sight_id`, `user_id`, `post_datetime`) VALUES
(1, 'Deep forest', 'It''s so fun hiding in the forest.', 'About TShirt', NULL, 2, '2017-03-18 21:10:49'),
(40, 'Sunny post :)', 'Nice book for life', 'About interesting book', NULL, 21, '2017-03-18 21:10:49'),
(41, 'Hello', 'Let''s be friends?!', 'Hello, who are you?', NULL, 1, '2017-03-18 21:10:49'),
(42, 'Coffee morning', 'Depresso :)', 'Coffee is it good?', 86, 21, '2017-03-18 21:10:49'),
(43, 'dfsdf', 'sdfsdf', 'werwerr', NULL, 3, '2017-03-18 21:10:49');

-- --------------------------------------------------------

--
-- Структура таблицы `roles`
--

CREATE TABLE `roles` (
  `role_id` int(11) NOT NULL,
  `role` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `roles`
--

INSERT INTO `roles` (`role_id`, `role`) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_TRAVELLER');

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(64) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `enabled`, `email`) VALUES
(1, 'user1', '$2a$10$zaXx4XNKJCaG3FIkjOmQuuT3jEL215SiDIUqXeZ12ykMirT7xngvW', 1, 'user1@gmail.eu'),
(2, 'user2', '123456', 1, 'user2@gmail.eu'),
(3, 'user3', '123456', 1, 'user3@gmail.eu'),
(21, 'kverchi', '$2a$10$zaXx4XNKJCaG3FIkjOmQuuT3jEL215SiDIUqXeZ12ykMirT7xngvW', 1, 'kverchi@hotmail.com');

-- --------------------------------------------------------

--
-- Структура таблицы `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Дамп данных таблицы `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(1, 3),
(2, 2),
(3, 3);

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`book_id`);

--
-- Индексы таблицы `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`comment_id`),
  ADD KEY `post_id_idx` (`post_id`),
  ADD KEY `user_id_idx` (`user_id`);

--
-- Индексы таблицы `countries`
--
ALTER TABLE `countries`
  ADD PRIMARY KEY (`country_code`),
  ADD UNIQUE KEY `country_code_UNIQUE` (`country_code`);

--
-- Индексы таблицы `countries_sights`
--
ALTER TABLE `countries_sights`
  ADD PRIMARY KEY (`sight_id`),
  ADD KEY `country_code` (`country_code`);

--
-- Индексы таблицы `password_change_requests`
--
ALTER TABLE `password_change_requests`
  ADD PRIMARY KEY (`UUID`),
  ADD KEY `user_id_pass_change_idx` (`user_id`);

--
-- Индексы таблицы `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`post_id`),
  ADD KEY `sight_id_idx` (`sight_id`),
  ADD KEY `user_id_idx` (`user_id`);

--
-- Индексы таблицы `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Индексы таблицы `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Индексы таблицы `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `user_id_idx` (`user_id`),
  ADD KEY `role_id_idx` (`role_id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `books`
--
ALTER TABLE `books`
  MODIFY `book_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT для таблицы `comments`
--
ALTER TABLE `comments`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT для таблицы `countries_sights`
--
ALTER TABLE `countries_sights`
  MODIFY `sight_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;
--
-- AUTO_INCREMENT для таблицы `posts`
--
ALTER TABLE `posts`
  MODIFY `post_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;
--
-- AUTO_INCREMENT для таблицы `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT для таблицы `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;
--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comment_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `comment_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `countries_sights`
--
ALTER TABLE `countries_sights`
  ADD CONSTRAINT `countries_sights_ibfk_1` FOREIGN KEY (`country_code`) REFERENCES `countries` (`country_code`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `password_change_requests`
--
ALTER TABLE `password_change_requests`
  ADD CONSTRAINT `user_id_pass_change` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `sight_id` FOREIGN KEY (`sight_id`) REFERENCES `countries_sights` (`sight_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `user_id_post` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ограничения внешнего ключа таблицы `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
