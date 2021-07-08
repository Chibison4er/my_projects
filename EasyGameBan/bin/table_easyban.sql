-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Окт 30 2016 г., 19:37
-- Версия сервера: 5.5.47
-- Версия PHP: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `testt`
--

-- --------------------------------------------------------

--
-- Структура таблицы `easyban`
--

CREATE TABLE IF NOT EXISTS `easyban` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player` varchar(255) NOT NULL,
  `whosetban` varchar(255) DEFAULT NULL,
  `whosettempban` varchar(255) DEFAULT NULL,
  `whosetmute` varchar(255) DEFAULT NULL,
  `banreason` varchar(255) DEFAULT NULL,
  `tempbanreason` varchar(255) DEFAULT NULL,
  `mutereason` varchar(255) DEFAULT NULL,
  `tempbanendtime` bigint(20) DEFAULT NULL,
  `bantype` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `player` (`player`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=18 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
