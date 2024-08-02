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
-- Table structure for table `usernotifications`
--

DROP TABLE IF EXISTS `usernotifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usernotifications` (
  `userNotificationId` int NOT NULL AUTO_INCREMENT,
  `receiverId` int DEFAULT NULL,
  `notificationId` int DEFAULT NULL,
  `isRead` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`userNotificationId`),
  KEY `receiverId` (`receiverId`),
  KEY `notificationId` (`notificationId`),
  CONSTRAINT `usernotifications_ibfk_1` FOREIGN KEY (`receiverId`) REFERENCES `users` (`employeeId`),
  CONSTRAINT `usernotifications_ibfk_2` FOREIGN KEY (`notificationId`) REFERENCES `notifications` (`notificationId`)
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usernotifications`
--

LOCK TABLES `usernotifications` WRITE;
/*!40000 ALTER TABLE `usernotifications` DISABLE KEYS */;
INSERT INTO `usernotifications` VALUES (29,2,3,0),(31,5,3,0),(32,7,3,0),(34,9,3,0),(35,10,3,0),(36,2,4,0),(39,7,4,0),(40,8,4,0),(41,9,4,0),(43,2,5,0),(45,5,5,0),(47,8,5,0),(48,9,5,0),(49,10,5,0),(50,2,6,0),(52,5,6,0),(53,7,6,0),(54,8,6,0),(56,10,6,0),(57,2,7,0),(59,5,7,0),(60,7,7,0),(62,9,7,0),(63,10,7,0),(64,2,8,0),(67,7,8,0),(68,8,8,0),(69,9,8,0),(71,2,9,0),(73,5,9,0),(75,8,9,0),(76,9,9,0),(77,10,9,0),(78,2,10,0),(80,5,10,0),(81,7,10,0),(82,8,10,0),(84,10,10,0),(87,5,14,0),(88,7,14,0),(89,8,14,0),(90,9,14,0),(91,10,14,0),(92,11,14,0),(93,12,14,0),(94,13,14,0),(97,5,15,0),(98,7,15,0),(99,8,15,0),(100,9,15,0),(101,10,15,0),(102,11,15,0),(103,12,15,0),(104,13,15,0),(107,5,16,0),(108,7,16,0),(109,8,16,0),(110,9,16,0),(111,10,16,0),(112,11,16,0),(113,12,16,0),(114,13,16,0),(117,5,17,0),(118,7,17,0),(119,8,17,0),(120,9,17,0),(121,10,17,0),(122,11,17,0),(123,12,17,0),(124,13,17,0),(127,5,18,0),(128,7,18,0),(129,8,18,0),(130,9,18,0),(131,10,18,0),(132,11,18,0),(133,12,18,0),(134,13,18,0),(136,4,19,0),(137,5,19,0),(138,7,19,0),(139,8,19,0),(140,9,19,0),(141,10,19,0),(142,11,19,0),(143,12,19,0),(144,13,19,0),(146,4,20,0),(147,5,20,0),(148,7,20,0),(149,8,20,0),(150,9,20,0),(151,10,20,0),(152,11,20,0),(153,12,20,0),(154,13,20,0),(156,4,21,0),(157,5,21,0),(158,7,21,0),(159,8,21,0),(160,9,21,0),(161,10,21,0),(162,11,21,0),(163,12,21,0),(164,13,21,0),(166,4,22,0),(167,5,22,0),(168,7,22,0),(169,8,22,0),(170,9,22,0),(171,10,22,0),(172,11,22,0),(173,12,22,0),(174,13,22,0),(176,4,23,0),(177,5,23,0),(178,7,23,0),(179,8,23,0),(180,9,23,0),(181,10,23,0),(182,11,23,0),(183,12,23,0),(184,13,23,0),(185,3,24,0),(186,4,24,0),(187,5,24,0),(188,7,24,0),(189,8,24,0),(190,9,24,0),(191,10,24,0),(192,11,24,0),(193,12,24,0),(194,13,24,0);
/*!40000 ALTER TABLE `usernotifications` ENABLE KEYS */;
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
