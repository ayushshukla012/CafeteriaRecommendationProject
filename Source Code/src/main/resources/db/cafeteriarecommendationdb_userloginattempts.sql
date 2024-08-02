-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: cafeteriarecommendationdb
-- ------------------------------------------------------
-- Server version	8.0.37

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
-- Table structure for table `userloginattempts`
--

DROP TABLE IF EXISTS `userloginattempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userloginattempts` (
  `attemptId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `attemptTimestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`attemptId`),
  KEY `userId` (`userId`),
  CONSTRAINT `userloginattempts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`employeeId`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userloginattempts`
--

LOCK TABLES `userloginattempts` WRITE;
/*!40000 ALTER TABLE `userloginattempts` DISABLE KEYS */;
INSERT INTO `userloginattempts` VALUES (1,1,'2024-06-20 12:10:48','failure'),(2,2,'2024-06-20 12:44:31','failure'),(3,1,'2024-06-20 12:45:06','failure'),(4,3,'2024-06-20 12:45:25','success'),(5,3,'2024-06-20 18:28:51','success'),(6,3,'2024-06-20 18:30:51','success'),(7,4,'2024-06-21 00:50:56','success'),(8,4,'2024-06-21 01:11:09','success'),(9,4,'2024-06-21 01:28:51','success'),(10,1,'2024-06-21 01:31:58','success'),(11,2,'2024-06-21 01:44:37','success'),(12,2,'2024-06-21 01:50:15','success'),(13,2,'2024-07-01 02:17:41','success'),(14,2,'2024-07-01 08:00:50','success'),(15,2,'2024-07-01 11:47:40','success'),(16,2,'2024-07-01 12:08:08','success'),(17,2,'2024-07-01 12:54:24','success'),(18,2,'2024-07-01 13:00:07','success'),(19,2,'2024-07-01 13:05:34','success'),(20,2,'2024-07-01 13:52:30','success'),(21,2,'2024-07-01 14:14:22','success'),(22,2,'2024-07-01 14:19:35','success'),(23,2,'2024-07-01 14:23:18','success'),(24,2,'2024-07-01 16:12:15','success'),(25,2,'2024-07-01 16:15:06','success'),(26,2,'2024-07-01 16:57:09','success'),(27,2,'2024-07-01 17:07:42','success'),(28,2,'2024-07-01 17:08:52','success'),(29,2,'2024-07-02 02:31:15','success'),(30,3,'2024-07-02 02:32:18','success'),(31,3,'2024-07-09 01:54:54','success'),(32,2,'2024-07-10 02:18:07','success'),(33,2,'2024-07-10 02:21:50','success'),(34,3,'2024-07-10 02:30:39','success'),(35,2,'2024-07-10 03:02:12','success'),(36,2,'2024-07-10 03:03:12','success'),(37,2,'2024-07-10 03:05:38','success'),(38,3,'2024-07-10 03:06:39','success'),(39,3,'2024-07-10 03:29:30','success'),(40,3,'2024-07-10 03:42:11','success'),(41,3,'2024-07-10 03:45:41','success'),(42,3,'2024-07-14 10:08:26','success'),(43,3,'2024-07-15 05:51:02','success'),(44,2,'2024-07-15 08:11:36','success'),(45,2,'2024-07-15 09:54:19','success'),(46,3,'2024-07-15 10:01:19','success'),(47,4,'2024-07-15 10:12:57','failure'),(48,3,'2024-07-15 10:13:06','success'),(49,3,'2024-07-15 10:21:33','success'),(50,3,'2024-07-15 10:25:59','success'),(51,1,'2024-07-15 10:39:32','success'),(52,3,'2024-07-15 12:57:24','success'),(53,1,'2024-07-15 12:59:29','success'),(54,3,'2024-07-15 13:02:26','success'),(55,6,'2024-07-15 13:03:28','failure'),(56,4,'2024-07-15 13:04:12','success'),(57,5,'2024-07-15 14:03:20','failure'),(58,1,'2024-07-15 14:03:29','success'),(59,3,'2024-07-15 14:11:52','failure'),(60,1,'2024-07-15 14:12:03','success'),(61,2,'2024-07-15 14:12:54','success'),(62,2,'2024-07-15 14:29:22','failure'),(63,2,'2024-07-15 14:29:31','success'),(64,2,'2024-07-15 14:38:52','success'),(65,2,'2024-07-15 15:12:01','success'),(66,2,'2024-07-15 15:15:41','success'),(67,2,'2024-07-15 15:16:53','success'),(68,2,'2024-07-15 15:18:21','success'),(69,3,'2024-07-15 15:20:38','success'),(70,2,'2024-07-16 08:42:48','success'),(71,2,'2024-07-16 08:45:45','failure'),(72,3,'2024-07-16 08:45:53','success'),(73,2,'2024-07-16 08:48:58','success'),(74,1,'2024-07-29 06:23:47','success'),(75,2,'2024-07-29 06:24:27','success'),(76,3,'2024-07-29 06:26:53','success'),(77,2,'2024-07-29 06:28:16','success'),(78,2,'2024-07-31 09:45:51','success'),(79,1,'2024-08-01 17:20:28','success'),(80,1,'2024-08-01 17:21:19','success'),(81,2,'2024-08-01 17:23:18','success'),(82,3,'2024-08-01 17:44:22','success'),(84,2,'2024-08-01 18:23:16','success'),(85,2,'2024-08-01 18:25:18','success');
/*!40000 ALTER TABLE `userloginattempts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-02  8:05:08
