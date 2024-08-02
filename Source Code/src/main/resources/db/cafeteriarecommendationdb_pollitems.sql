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
-- Table structure for table `pollitems`
--

DROP TABLE IF EXISTS `pollitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pollitems` (
  `pollItemId` int NOT NULL AUTO_INCREMENT,
  `pollId` int NOT NULL,
  `menuItemId` int NOT NULL,
  PRIMARY KEY (`pollItemId`),
  KEY `pollId` (`pollId`),
  KEY `menuItemId` (`menuItemId`),
  CONSTRAINT `pollitems_ibfk_1` FOREIGN KEY (`pollId`) REFERENCES `polls` (`pollId`),
  CONSTRAINT `pollitems_ibfk_2` FOREIGN KEY (`menuItemId`) REFERENCES `menu` (`menuId`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pollitems`
--

LOCK TABLES `pollitems` WRITE;
/*!40000 ALTER TABLE `pollitems` DISABLE KEYS */;
INSERT INTO `pollitems` VALUES (1,1,1),(2,1,2),(3,1,1),(4,1,3),(5,1,4),(6,2,1),(7,2,2),(8,2,3),(9,3,1),(10,3,2),(11,3,3),(12,4,3),(13,4,4),(14,4,5),(15,5,9),(16,5,2),(17,5,1),(18,6,9),(19,6,1),(20,6,10),(21,6,27),(22,7,9),(23,7,1),(24,7,10),(25,7,2),(26,8,2),(27,8,1),(28,8,10);
/*!40000 ALTER TABLE `pollitems` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-02  8:05:09
