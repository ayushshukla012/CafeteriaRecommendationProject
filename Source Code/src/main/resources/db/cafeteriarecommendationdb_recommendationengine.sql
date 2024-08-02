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
-- Table structure for table `recommendationengine`
--

DROP TABLE IF EXISTS `recommendationengine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendationengine` (
  `recommendationId` int NOT NULL AUTO_INCREMENT,
  `menuId` int DEFAULT NULL,
  `averageRating` float DEFAULT NULL,
  `sentimentAnalysis` varchar(255) DEFAULT NULL,
  `recommendationDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`recommendationId`),
  KEY `menuId` (`menuId`),
  CONSTRAINT `recommendationengine_ibfk_1` FOREIGN KEY (`menuId`) REFERENCES `menu` (`menuId`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recommendationengine`
--

LOCK TABLES `recommendationengine` WRITE;
/*!40000 ALTER TABLE `recommendationengine` DISABLE KEYS */;
INSERT INTO `recommendationengine` VALUES (2,2,4,'Positive','2024-06-19 01:27:17'),(4,4,4.9,'Very Positive','2024-06-19 01:27:17'),(5,5,4.3,'Positive','2024-06-19 01:27:17'),(7,7,4.1,'Positive','2024-06-19 01:27:17'),(8,8,4.8,'Very Positive','2024-06-19 01:27:17'),(9,9,3,'Neutral','2024-06-19 01:27:17'),(11,10,4.22857,'neutral','2024-06-21 01:50:22'),(12,6,4.17143,'very positive','2024-06-21 01:50:22'),(13,9,4.68571,'very positive','2024-06-21 01:50:22'),(14,3,3.35,'very positive','2024-06-21 01:50:22'),(15,1,3.21667,'positive','2024-06-21 01:50:22'),(16,8,4.34286,'very positive','2024-06-21 01:50:22'),(17,4,2.93333,'positive','2024-06-21 01:50:22'),(18,7,4.62857,'very positive','2024-06-21 01:50:22'),(19,5,2.88333,'positive','2024-06-21 01:50:22'),(20,2,3.08333,'very positive','2024-06-21 01:50:22'),(21,10,4.22857,'neutral','2024-07-01 02:17:53'),(22,3,3.35,'very positive','2024-07-01 02:17:53'),(23,1,3.21667,'positive','2024-07-01 02:17:53'),(24,5,2.88333,'positive','2024-07-01 02:17:53'),(25,6,4.17143,'very positive','2024-07-01 02:17:53'),(26,8,4.34286,'very positive','2024-07-01 02:17:53'),(27,9,4.68571,'very positive','2024-07-01 02:17:53'),(28,2,3.08333,'very positive','2024-07-01 02:17:53'),(29,7,4.62857,'very positive','2024-07-01 02:17:53'),(30,4,2.93333,'positive','2024-07-01 02:17:53'),(31,2,3.08333,'very positive','2024-07-01 08:01:00'),(32,8,4.34286,'very positive','2024-07-01 08:01:00'),(33,9,4.68571,'very positive','2024-07-01 08:01:00'),(34,4,2.93333,'positive','2024-07-01 08:01:00'),(35,7,4.62857,'very positive','2024-07-01 08:01:00'),(36,5,2.88333,'positive','2024-07-01 08:01:00'),(37,3,3.35,'very positive','2024-07-01 08:01:00'),(38,1,3.21667,'positive','2024-07-01 08:01:00'),(39,6,4.17143,'very positive','2024-07-01 08:01:00'),(40,10,4.22857,'neutral','2024-07-01 08:01:00'),(41,5,2.88333,'positive','2024-07-01 11:47:44'),(42,4,2.93333,'positive','2024-07-01 11:47:44'),(43,2,3.08333,'very positive','2024-07-01 11:47:44'),(44,3,3.35,'very positive','2024-07-01 11:47:44'),(45,6,4.17143,'very positive','2024-07-01 11:47:44'),(46,9,4.68571,'very positive','2024-07-01 11:47:44'),(47,1,3.21667,'positive','2024-07-01 11:47:44'),(48,7,4.62857,'very positive','2024-07-01 11:47:44'),(49,8,4.34286,'very positive','2024-07-01 11:47:44'),(50,10,4.22857,'neutral','2024-07-01 11:47:44');
/*!40000 ALTER TABLE `recommendationengine` ENABLE KEYS */;
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
