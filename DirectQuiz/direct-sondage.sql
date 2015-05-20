-- phpMyAdmin SQL Dump
-- version 4.2.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: May 20, 2015 at 04:03 PM
-- Server version: 5.6.17-debug-log
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `direct-sondage`
--

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

CREATE TABLE IF NOT EXISTS `question` (
`id` mediumint(10) NOT NULL,
  `type_id` mediumint(10) NOT NULL,
  `utilisateur_id` mediumint(10) NOT NULL,
  `titre` varchar(100) NOT NULL,
  `enonce` text NOT NULL,
  `points` mediumint(10) DEFAULT NULL,
  `theme_id` int(100) DEFAULT '0',
  `image` varchar(256) DEFAULT NULL,
  `imageRealName` varchar(512) DEFAULT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=512 ;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`id`, `type_id`, `utilisateur_id`, `titre`, `enonce`, `points`, `theme_id`, `image`, `imageRealName`) VALUES
(503, 0, 4, 'titre1', 'ceci est la première question ?', 0, 129, NULL, NULL),
(504, 0, 4, 'titre2', 'ceci est la deuxième question ?', 2, 129, NULL, NULL),
(505, 0, 4, 'titre3', 'ceci est la troisième question ?', 5, 129, NULL, NULL),
(506, 0, 4, 'titre1', 'ceci est la première question ?', 5, 130, NULL, NULL),
(507, 0, 4, 'titre2', 'ceci est la deuxième question ?', 2, 130, NULL, NULL),
(508, 0, 4, 'titre3', 'ceci est la troisième question ?', 5, 130, NULL, NULL),
(509, 0, 4, 'titre4', 'ceci est la quatrième question ?', 5, 129, NULL, NULL),
(510, 0, 4, 'titre1', 'ceci est la première question ?', 4, 131, NULL, NULL),
(511, 0, 4, 'titre2', 'ceci est la deuxième question ?', 4, 131, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `question_sequence`
--

CREATE TABLE IF NOT EXISTS `question_sequence` (
  `sequence_id` mediumint(10) NOT NULL,
  `question_id` mediumint(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_sequence`
--

INSERT INTO `question_sequence` (`sequence_id`, `question_id`) VALUES
(99, 510),
(99, 503),
(99, 504),
(101, 511),
(101, 507),
(101, 510),
(101, 505);

-- --------------------------------------------------------

--
-- Table structure for table `reponse`
--

CREATE TABLE IF NOT EXISTS `reponse` (
`id` mediumint(10) NOT NULL,
  `question_id` mediumint(10) NOT NULL,
  `texte` varchar(200) CHARACTER SET utf8 NOT NULL,
  `valeur` int(1) NOT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2481 ;

--
-- Dumping data for table `reponse`
--

INSERT INTO `reponse` (`id`, `question_id`, `texte`, `valeur`) VALUES
(2458, 503, 'c', 0),
(2457, 503, 'b', 1),
(2456, 503, 'a', 0),
(2480, 504, 'c', 0),
(2479, 504, 'b', 1),
(2478, 504, 'a', 0),
(2474, 505, 'a', 0),
(2473, 505, 'b', 1),
(2472, 505, 'c', 0),
(2468, 506, 'rfrf', 0),
(2467, 506, 'aaa', 0),
(2466, 506, 'a', 1),
(2465, 507, 'kj', 1),
(2464, 507, 'll', 0),
(2463, 507, 'pppp', 0),
(2471, 508, ',ls', 0),
(2470, 508, 's$', 1),
(2469, 508, 'sss', 0),
(2477, 509, 'a', 0),
(2476, 509, 'n', 1),
(2475, 509, 'fff', 0),
(2452, 510, 'rrrr', 1),
(2451, 510, 'sss', 0),
(2450, 510, 's', 0),
(2462, 511, 'pllla', 1),
(2461, 511, 'aaaaaa', 0),
(2460, 511, 'fffffff', 0),
(2459, 511, 'fff', 0);

-- --------------------------------------------------------

--
-- Table structure for table `sequence`
--

CREATE TABLE IF NOT EXISTS `sequence` (
`id` mediumint(100) NOT NULL,
  `utilisateur_id` mediumint(100) NOT NULL,
  `code` varchar(20) CHARACTER SET utf8 NOT NULL,
  `motDePasse` varchar(20) CHARACTER SET utf8 NOT NULL,
  `mode` varchar(10) DEFAULT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=102 ;

--
-- Dumping data for table `sequence`
--

INSERT INTO `sequence` (`id`, `utilisateur_id`, `code`, `motDePasse`, `mode`) VALUES
(99, 4, 'aaa', 'aaa', '0'),
(100, 4, 'testaprmodif', 'testaprmodif', NULL),
(101, 4, 'testfinal', 'testfinal', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `theme`
--

CREATE TABLE IF NOT EXISTS `theme` (
`id` mediumint(10) NOT NULL,
  `utilisateur_id` mediumint(10) NOT NULL,
  `titre` varchar(100) NOT NULL
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=132 ;

--
-- Dumping data for table `theme`
--

INSERT INTO `theme` (`id`, `utilisateur_id`, `titre`) VALUES
(129, 4, 'Math'),
(130, 4, 'Méca'),
(131, 4, 'Chimie');

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

CREATE TABLE IF NOT EXISTS `utilisateur` (
`id` int(11) NOT NULL,
  `login` varchar(20) CHARACTER SET utf8 NOT NULL,
  `mot_de_passe` varchar(32) CHARACTER SET utf8 NOT NULL,
  `date_inscription` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `email` varchar(50) CHARACTER SET utf8 NOT NULL,
  `valide` int(1) NOT NULL,
  `gestionnaire` int(1) NOT NULL,
  `code` text
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=116 ;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `login`, `mot_de_passe`, `date_inscription`, `email`, `valide`, `gestionnaire`, `code`) VALUES
(4, 'test', 'ddbdc435414a5791137b4b955459b4b5', '2015-05-20 15:56:47', 'plantec@insa-toulouse.fr', 1, 1, NULL),
(114, 'salahmd5', '4df41897bb024a8b47fab802d4727663', '2015-04-20 14:20:06', 'salah.at@hotmail.fr', 1, 0, 'vv5gMUGSYK'),
(115, 'atmitim', 'd9ca73e07043f05d3409df9c7a4cc1a8', '2015-04-20 18:36:47', 'salah.at@hotmail.fr', 1, 0, '53BTMXTEcD');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `question`
--
ALTER TABLE `question`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reponse`
--
ALTER TABLE `reponse`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sequence`
--
ALTER TABLE `sequence`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `theme`
--
ALTER TABLE `theme`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `utilisateur`
--
ALTER TABLE `utilisateur`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
MODIFY `id` mediumint(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=512;
--
-- AUTO_INCREMENT for table `reponse`
--
ALTER TABLE `reponse`
MODIFY `id` mediumint(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2481;
--
-- AUTO_INCREMENT for table `sequence`
--
ALTER TABLE `sequence`
MODIFY `id` mediumint(100) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=102;
--
-- AUTO_INCREMENT for table `theme`
--
ALTER TABLE `theme`
MODIFY `id` mediumint(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=132;
--
-- AUTO_INCREMENT for table `utilisateur`
--
ALTER TABLE `utilisateur`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=116;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
