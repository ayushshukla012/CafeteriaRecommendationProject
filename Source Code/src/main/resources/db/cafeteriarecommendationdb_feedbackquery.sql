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
-- Table structure for table `feedbackquery`
--

DROP TABLE IF EXISTS `feedbackquery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedbackquery` (
  `feedbackQueryId` int NOT NULL AUTO_INCREMENT,
  `notificationId` int DEFAULT NULL,
  `employeeId` int DEFAULT NULL,
  `question` text NOT NULL,
  `response` text NOT NULL,
  `responseDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`feedbackQueryId`),
  KEY `notificationId` (`notificationId`),
  KEY `employeeId` (`employeeId`),
  CONSTRAINT `feedbackquery_ibfk_1` FOREIGN KEY (`notificationId`) REFERENCES `notifications` (`notificationId`),
  CONSTRAINT `feedbackquery_ibfk_2` FOREIGN KEY (`employeeId`) REFERENCES `users` (`employeeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbackquery`
--

LOCK TABLES `feedbackquery` WRITE;
/*!40000 ALTER TABLE `feedbackquery` DISABLE KEYS */;
INSERT INTO `feedbackquery` VALUES (1,22,3,'We are trying to improve your experience with Vada. Please provide your feedback and help us.','can be better at taste','2024-07-15 15:24:36');
/*!40000 ALTER TABLE `feedbackquery` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-02  8:05:07
