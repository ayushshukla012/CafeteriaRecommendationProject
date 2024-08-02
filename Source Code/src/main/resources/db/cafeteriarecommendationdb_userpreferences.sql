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
-- Table structure for table `userpreferences`
--

DROP TABLE IF EXISTS `userpreferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userpreferences` (
  `preferenceId` int NOT NULL AUTO_INCREMENT,
  `employeeId` int DEFAULT NULL,
  `dietaryPreference` enum('Vegetarian','Non Vegetarian','Eggetarian') DEFAULT 'Vegetarian',
  `spiceLevel` enum('High','Medium','Low') DEFAULT 'Medium',
  `preferredCuisine` enum('North Indian','South Indian','Chinese','Italian','Mexican','Other') DEFAULT 'Other',
  `sweetTooth` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`preferenceId`),
  KEY `employeeId` (`employeeId`),
  CONSTRAINT `userpreferences_ibfk_1` FOREIGN KEY (`employeeId`) REFERENCES `users` (`employeeId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userpreferences`
--

LOCK TABLES `userpreferences` WRITE;
/*!40000 ALTER TABLE `userpreferences` DISABLE KEYS */;
INSERT INTO `userpreferences` VALUES (1,3,'Vegetarian','High','South Indian',1),(2,4,'Non Vegetarian','High','North Indian',0),(3,5,'Eggetarian','Medium','Chinese',1),(4,6,'Vegetarian','Low','Italian',0),(5,7,'Non Vegetarian','High','Mexican',1),(6,8,'Vegetarian','Medium','Other',0),(7,9,'Non Vegetarian','Medium','North Indian',1),(8,10,'Eggetarian','Medium','South Indian',0),(9,11,'Vegetarian','High','Chinese',1),(10,12,'Non Vegetarian','Medium','Italian',0),(11,13,'Eggetarian','Low','Other',1);
/*!40000 ALTER TABLE `userpreferences` ENABLE KEYS */;
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
