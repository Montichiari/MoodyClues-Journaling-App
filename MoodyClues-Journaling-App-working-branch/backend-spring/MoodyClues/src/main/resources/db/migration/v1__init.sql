-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: moodyclues_db
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `counsellor_client`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `counsellor_client` (
  `counsellor_id` varchar(255) NOT NULL,
  `client_id` varchar(255) NOT NULL,
  KEY `FK19a0770fs6aqysnhql5esy54h` (`client_id`),
  KEY `FK30ncg6ollxajea8ln4ygspfl3` (`counsellor_id`),
  CONSTRAINT `FK19a0770fs6aqysnhql5esy54h` FOREIGN KEY (`client_id`) REFERENCES `journal_users` (`id`),
  CONSTRAINT `FK30ncg6ollxajea8ln4ygspfl3` FOREIGN KEY (`counsellor_id`) REFERENCES `counsellor_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `counsellor_user`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `counsellor_user` (
  `id` varchar(255) NOT NULL,
  `archived` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emotions`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emotions` (
  `id` varchar(255) NOT NULL,
  `emotion_label` varchar(255) DEFAULT NULL,
  `icon_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entry_emotions`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entry_emotions` (
  `entry_id` varchar(255) NOT NULL,
  `emotion_id` varchar(255) NOT NULL,
  KEY `FKhcecgbnbvi7im6b5xwy6qy882` (`emotion_id`),
  KEY `FK8i93kb9v128o9j7vbafdwniea` (`entry_id`),
  CONSTRAINT `FK8i93kb9v128o9j7vbafdwniea` FOREIGN KEY (`entry_id`) REFERENCES `journal_entries` (`id`),
  CONSTRAINT `FKhcecgbnbvi7im6b5xwy6qy882` FOREIGN KEY (`emotion_id`) REFERENCES `emotions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `habits_entries`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habits_entries` (
  `id` varchar(255) NOT NULL,
  `archived` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `last_saved_at` datetime(6) DEFAULT NULL,
  `sleep` double NOT NULL,
  `water` double NOT NULL,
  `work_hours` double NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeaehlac90n0ytl52wg66lp1mw` (`user_id`),
  CONSTRAINT `FKeaehlac90n0ytl52wg66lp1mw` FOREIGN KEY (`user_id`) REFERENCES `journal_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journal_entries`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journal_entries` (
  `id` varchar(255) NOT NULL,
  `archived` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `last_saved_at` datetime(6) DEFAULT NULL,
  `entry_text` varchar(255) DEFAULT NULL,
  `entry_title` varchar(255) DEFAULT NULL,
  `mood` int NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4hlu7kdodir02mr6pdx5hpxh` (`user_id`),
  CONSTRAINT `FKh4hlu7kdodir02mr6pdx5hpxh` FOREIGN KEY (`user_id`) REFERENCES `journal_users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `journal_users`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `journal_users` (
  `id` varchar(255) NOT NULL,
  `archived` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `show_emotion` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link_request`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `link_request` (
  `id` varchar(255) NOT NULL,
  `requested_at` datetime(6) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `counsellor_user_id` varchar(255) DEFAULT NULL,
  `journal_user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqmo1m0cv1i4k5wvel7pytsgoj` (`counsellor_user_id`),
  KEY `FKsksi88nq3k3lkf15a95b5lrr3` (`journal_user_id`),
  CONSTRAINT `FKqmo1m0cv1i4k5wvel7pytsgoj` FOREIGN KEY (`counsellor_user_id`) REFERENCES `counsellor_user` (`id`),
  CONSTRAINT `FKsksi88nq3k3lkf15a95b5lrr3` FOREIGN KEY (`journal_user_id`) REFERENCES `journal_users` (`id`),
  CONSTRAINT `link_request_chk_1` CHECK ((`status` between 0 and 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-07 14:03:45
