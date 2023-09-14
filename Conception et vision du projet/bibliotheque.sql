-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : jeu. 14 sep. 2023 à 10:36
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

--
-- Déchargement des données de la table `bibliothequaire`
--

INSERT INTO `bibliothequaire` (`id`, `name`, `usership`) VALUES
(1, 'imad eddine', 'A01');

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

CREATE TABLE `book` (
  `isbn` int(5) NOT NULL,
  `name` varchar(200) NOT NULL,
  `author` varchar(200) NOT NULL,
  `quantity` int(200) NOT NULL,
  `status` varchar(200) NOT NULL,
  `lost_quantity` int(200) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `book`
--

INSERT INTO `book` (`isbn`, `name`, `author`, `quantity`, `status`, `lost_quantity`) VALUES
(21, 'imad', 'zaoui', 20, 'disponible', 0),
(22, 'les jours', 'abder', 10, 'disponible', 0),
(24, 'hamid', 'sebt', 0, 'indisponible', 0),
(26, 'savoir etre', 'hamid', 0, 'indisponible', 0),
(27, 'nouveau', 'chamal', 0, 'indisponible', 0),
(34, 'Z', 'sdcqsdv', 109, 'disponible', 1),
(35, 'les miserables', 'hugo', 0, 'perdu', 1),
(36, 'la boite', 'sefrioui', 2, 'disponible', 0);

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

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `name`, `adress`, `membership`) VALUES
(1, 'kamal harite', 'el jadida', 'B01'),
(2, 'ilyas chaoui', 'rabat ', 'B10'),
(3, 'abdo zaoui', 'oujda', 'B11');

-- --------------------------------------------------------

--
-- Structure de la table `emprunt`
--

CREATE TABLE `emprunt` (
  `isbn_book` int(200) NOT NULL,
  `client_id` int(200) NOT NULL,
  `bibliothequaire_id` int(200) NOT NULL,
  `pickup_date` varchar(200) NOT NULL,
  `return_date` varchar(200) NOT NULL,
  `period` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `emprunt`
--

INSERT INTO `emprunt` (`isbn_book`, `client_id`, `bibliothequaire_id`, `pickup_date`, `return_date`, `period`) VALUES
(21, 1, 1, '2023-09-10', '2023-09-13', 3);

--
-- Déclencheurs `emprunt`
--
DELIMITER $$
CREATE TRIGGER `book_trigger` AFTER INSERT ON `emprunt` FOR EACH ROW BEGIN 
  DECLARE qtt INT;
  UPDATE book SET quantity = quantity - 1 WHERE book.isbn = NEW.isbn_book;

  SELECT quantity INTO qtt FROM book WHERE isbn = NEW.isbn_book;

  IF(qtt = 0) THEN
    UPDATE book SET status = "indisponible" WHERE book.isbn = NEW.isbn_book;
  END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `quantity_trigger` AFTER DELETE ON `emprunt` FOR EACH ROW BEGIN 
  DECLARE qtt INT;
  UPDATE book SET quantity = quantity + 1 WHERE book.isbn = OLD.isbn_book;

  SELECT quantity INTO qtt FROM book WHERE isbn = OLD.isbn_book;

  IF (qtt > 0) THEN
    UPDATE book SET status = "disponible" WHERE book.isbn = OLD.isbn_book;
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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unic` (`membership`);

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
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `book`
--
ALTER TABLE `book`
  MODIFY `isbn` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

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
