-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mar. 05 sep. 2023 à 17:26
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `bibliotheque`
--

-- --------------------------------------------------------

--
-- Structure de la table `bibliothequaire`
--

CREATE TABLE `bibliothequaire` (
  `id` int(5) NOT NULL,
  `name` varchar(200) NOT NULL,
  `usership` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

CREATE TABLE `book` (
  `isbn` int(5) NOT NULL,
  `name` varchar(200) NOT NULL,
  `author` varchar(200) NOT NULL,
  `quantity` int(200) NOT NULL,
  `status` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` int(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `adress` varchar(200) NOT NULL,
  `membership` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `emprunt`
--

CREATE TABLE `emprunt` (
  `isbn_book` int(200) NOT NULL,
  `client_id` int(200) NOT NULL,
  `bibliothequaire_id` int(200) NOT NULL,
  `pickup_date` date NOT NULL,
  `return_date` date NOT NULL,
  `period` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déclencheurs `emprunt`
--
DELIMITER $$
CREATE TRIGGER `book_trigger` AFTER INSERT ON `emprunt` FOR EACH ROW BEGIN 
  DECLARE qtt INT;
  UPDATE book SET quantity = quantity - 1 WHERE book.isbn = NEW.isbn_book;

  SELECT quantity INTO qtt FROM book WHERE isbn = NEW.isbn_book;

  IF(qtt = 0) THEN
    UPDATE book SET status = FALSE WHERE book.isbn = NEW.isbn_book;
  END IF;
END
$$
DELIMITER ;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `bibliothequaire`
--
ALTER TABLE `bibliothequaire`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`isbn`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `emprunt`
--
ALTER TABLE `emprunt`
  ADD KEY `isbn_book` (`isbn_book`),
  ADD KEY `bibliothequaire_id` (`bibliothequaire_id`),
  ADD KEY `client_id` (`client_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `bibliothequaire`
--
ALTER TABLE `bibliothequaire`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `book`
--
ALTER TABLE `book`
  MODIFY `isbn` int(5) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `emprunt`
--
ALTER TABLE `emprunt`
  ADD CONSTRAINT `emprunt_ibfk_1` FOREIGN KEY (`isbn_book`) REFERENCES `book` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `emprunt_ibfk_2` FOREIGN KEY (`bibliothequaire_id`) REFERENCES `bibliothequaire` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `emprunt_ibfk_3` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
