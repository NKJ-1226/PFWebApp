-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nkjweb
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (2,'意見','2025-06-06 20:03:06',NULL,NULL),(4,'その他','2025-06-06 20:03:06',NULL,NULL),(5,'質問','2025-06-06 20:03:06',NULL,NULL),(7,'未設定','2025-06-06 20:03:06',NULL,NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contacts` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `message` varchar(1000) NOT NULL,
  `status` varchar(255) NOT NULL,
  `category_id` int NOT NULL,
  `deleted` bit(1) NOT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkhfu1w0e6q7vlxgd3rxrjp2p4` (`category_id`),
  CONSTRAINT `FKkhfu1w0e6q7vlxgd3rxrjp2p4` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contacts`
--

LOCK TABLES `contacts` WRITE;
/*!40000 ALTER TABLE `contacts` DISABLE KEYS */;
INSERT INTO `contacts` VALUES (14,'nkj@aaa.com','テスト用メッセージを送信します。','対応済',2,_binary '\0','2025-06-06 20:01:39'),(18,'nkj@aaa.com','ぇぉずひくゎぴだたせいゑらゃげげただゑやぢこゖぅぃぐしゑてでかだあのく','未対応',4,_binary '\0','2025-06-06 20:01:39'),(19,'nkj@aaa.com','テスト用です。','対応中',2,_binary '\0','2025-06-06 20:01:39'),(20,'nkj@aaa.com','1\r 1\r 1\r','対応中',4,_binary '\0','2025-06-06 20:01:39');
/*!40000 ALTER TABLE `contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `from_user_id` int DEFAULT NULL,
  `to_user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES (1,'2025-05-01 16:40:20.240360',5,1),(2,'2025-05-01 16:40:49.236447',5,4),(3,'2025-05-02 21:04:37.116779',5,5),(4,'2025-05-02 21:13:32.737827',5,7),(5,'2025-05-05 11:42:48.518068',5,6),(6,'2025-05-05 11:43:21.919276',7,1),(7,'2025-05-11 17:19:27.878383',7,4),(8,'2025-05-18 17:42:47.446666',1,1),(9,'2025-05-18 17:43:19.924994',1,6),(10,'2025-05-19 19:40:13.878613',1,5),(11,'2025-05-25 13:16:02.288843',7,5);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `age` int DEFAULT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `is_locked` bit(1) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `password` varchar(32) NOT NULL,
  `self_introduction` varchar(1500) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `furigana` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `like_count_this_month` int NOT NULL,
  `like_count_this_year` int NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (28,1,_binary '\0',_binary '\0',NULL,'Nakajima123_',NULL,'nkj@com','なかじま',NULL,'ROLE_ADMIN','中島',0,0,'2025-05-25 19:03:23','2025-05-25 19:03:23',NULL),(50,4,_binary '\0',_binary '\0',NULL,'Nakajima123-',NULL,'nkj123@aaa.com','',NULL,'ROLE_ADMIN','テスト',0,0,'2025-05-25 19:03:23','2025-05-25 19:03:23',NULL),(150,5,_binary '\0',_binary '\0',NULL,'Nakajima123-',NULL,'test@test.com','てすとに',NULL,'ROLE_USER','テスト2',0,0,'2025-05-25 19:03:23','2025-05-25 20:32:11',NULL),(24,6,_binary '\0',_binary '',NULL,'Nakajima123-',NULL,'nkj3@aaa.com','',NULL,'ROLE_USER','テスト3',0,0,'2025-05-25 19:03:23','2025-05-25 19:03:23',NULL),(15,7,_binary '\0',_binary '\0',NULL,'Nakajima123-',NULL,'nkj1234@aaa.com','',NULL,'ROLE_USER','テスト4',0,0,'2025-05-25 19:03:23','2025-05-25 19:03:23',NULL),(NULL,9,_binary '\0',_binary '\0',NULL,'Nakajima123-',NULL,'nkj10@aaa.com',NULL,NULL,'ROLE_USER','テスト10',0,0,'2025-05-25 19:03:23','2025-05-25 19:05:50','2025-05-25 19:05:20');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-06 20:09:11
