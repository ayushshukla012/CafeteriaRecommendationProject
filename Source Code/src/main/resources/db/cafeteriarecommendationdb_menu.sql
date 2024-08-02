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
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `menuId` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `categoryId` int DEFAULT NULL,
  `price` float NOT NULL,
  `availability` tinyint(1) NOT NULL,
  `spiceLevel` enum('High','Medium','Low') DEFAULT 'Medium',
  `cuisineType` enum('North Indian','South Indian','Chinese','Italian','Mexican','Other') DEFAULT 'Other',
  `isSweet` tinyint(1) DEFAULT '0',
  `dietaryPreference` enum('Vegetarian','Non Vegetarian','Eggetarian') DEFAULT 'Vegetarian',
  PRIMARY KEY (`menuId`),
  KEY `categoryId` (`categoryId`),
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`categoryId`) REFERENCES `foodcategories` (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'Masala Dosa',1,150,1,'Medium','South Indian',0,'Vegetarian'),(2,'Aloo Paratha',1,120,1,'Medium','North Indian',0,'Vegetarian'),(3,'Veg Sandwich',2,130,1,'Medium','North Indian',0,'Vegetarian'),(4,'Paneer Tikka',2,180,1,'Medium','North Indian',0,'Vegetarian'),(5,'Chole Bhature',2,190,1,'Medium','North Indian',0,'Vegetarian'),(6,'Veg Biryani',3,220,1,'Medium','North Indian',0,'Vegetarian'),(7,'Palak Paneer',3,180,1,'Medium','North Indian',0,'Vegetarian'),(8,'Dal Makhani',3,160,1,'Medium','North Indian',0,'Vegetarian'),(9,'Idli Sambar',1,90,1,'Medium','South Indian',0,'Vegetarian'),(10,'Fruit Salad',1,100,1,'Low','South Indian',1,'Vegetarian'),(12,'Pongal',1,80,1,'Medium','South Indian',0,'Vegetarian'),(13,'Upma',1,70,1,'Medium','South Indian',0,'Vegetarian'),(14,'Poha',1,70,1,'Medium','South Indian',0,'Vegetarian'),(15,'Vada',1,60,1,'Medium','South Indian',0,'Vegetarian'),(16,'Appam',1,100,1,'Medium','South Indian',0,'Vegetarian'),(17,'Sabudana Khichdi',1,80,1,'Medium','South Indian',0,'Vegetarian'),(18,'Medu Vada',1,70,1,'Medium','South Indian',0,'Vegetarian'),(19,'Uttapam',1,90,1,'Medium','South Indian',0,'Vegetarian'),(20,'Thepla',1,70,1,'Medium','North Indian',0,'Vegetarian'),(21,'Dhokla',1,80,1,'Low','South Indian',1,'Vegetarian'),(22,'Puri Bhaji',1,100,1,'Medium','North Indian',0,'Vegetarian'),(23,'Methi Paratha',1,80,1,'Medium','North Indian',0,'Vegetarian'),(24,'Besan Chilla',1,70,1,'Medium','North Indian',0,'Vegetarian'),(25,'Idiyappam',1,100,1,'Medium','South Indian',0,'Vegetarian'),(26,'Misal Pav',1,110,1,'Medium','South Indian',0,'Vegetarian'),(27,'Bread Omelette',1,80,1,'Medium','North Indian',0,'Vegetarian'),(28,'Paneer Butter Masala',2,150,1,'Medium','North Indian',0,'Vegetarian'),(29,'Veg Pulao',2,120,1,'Medium','North Indian',0,'Vegetarian'),(30,'Bhindi Masala',2,100,1,'Medium','North Indian',0,'Vegetarian'),(31,'Rajma Chawal',2,130,1,'Medium','North Indian',0,'Vegetarian'),(32,'Aloo Gobi',2,100,1,'Medium','North Indian',0,'Vegetarian'),(33,'Kadhi Chawal',2,120,1,'Medium','North Indian',0,'Vegetarian'),(34,'Dal Tadka',2,110,1,'Medium','North Indian',0,'Vegetarian'),(35,'Jeera Aloo',2,90,1,'Medium','North Indian',0,'Vegetarian'),(36,'Matar Paneer',2,130,1,'Medium','North Indian',0,'Vegetarian'),(37,'Baingan Bharta',2,120,1,'Medium','North Indian',0,'Vegetarian'),(38,'Malai Kofta',2,140,1,'Medium','North Indian',0,'Vegetarian'),(39,'Dum Aloo',2,120,1,'Medium','North Indian',0,'Vegetarian'),(40,'Veg Kofta',2,130,1,'Medium','North Indian',0,'Vegetarian'),(41,'Aloo Matar',2,100,1,'Medium','North Indian',0,'Vegetarian'),(42,'Kofta Curry',2,140,1,'Medium','North Indian',0,'Vegetarian'),(43,'Vegetable Curry',2,110,1,'Medium','North Indian',0,'Vegetarian'),(44,'Chana Masala',2,120,1,'Medium','North Indian',0,'Vegetarian'),(45,'Butter Chicken',3,180,1,'High','North Indian',0,'Non Vegetarian'),(46,'Chicken Biryani',3,200,1,'High','North Indian',0,'Non Vegetarian'),(47,'Fish Curry',3,220,1,'High','North Indian',0,'Non Vegetarian'),(48,'Lamb Curry',3,240,1,'High','North Indian',0,'Non Vegetarian'),(49,'Prawn Masala',3,260,1,'High','North Indian',0,'Non Vegetarian'),(50,'Mutton Rogan Josh',3,280,1,'High','North Indian',0,'Non Vegetarian'),(51,'Chicken Tikka Masala',3,200,1,'High','North Indian',0,'Non Vegetarian'),(52,'Paneer Butter Masala',3,150,1,'Medium','North Indian',0,'Vegetarian'),(53,'Egg Curry',3,140,1,'Medium','North Indian',0,'Eggetarian'),(54,'Keema Curry',3,250,1,'High','North Indian',0,'Non Vegetarian'),(55,'Tandoori Chicken',3,220,1,'High','North Indian',0,'Non Vegetarian'),(56,'Mutton Biryani',3,250,1,'High','North Indian',0,'Non Vegetarian'),(57,'Fish Fry',3,230,1,'High','North Indian',0,'Non Vegetarian'),(58,'Shrimp Biryani',3,270,1,'High','North Indian',0,'Non Vegetarian'),(59,'Egg Biryani',3,180,1,'Medium','North Indian',0,'Eggetarian'),(78,'Gulab Jamun',6,50,1,'Low','North Indian',1,'Vegetarian'),(79,'Rasgulla',6,40,1,'Low','North Indian',1,'Vegetarian'),(80,'Kheer',6,60,1,'Low','North Indian',1,'Vegetarian'),(81,'Jalebi',6,30,1,'Low','North Indian',1,'Vegetarian'),(82,'Ladoo',6,20,1,'Low','North Indian',1,'Vegetarian'),(83,'Payasam',6,70,1,'Low','South Indian',1,'Vegetarian'),(84,'Mysore Pak',6,80,1,'Low','South Indian',1,'Vegetarian'),(85,'Peda',6,50,1,'Low','North Indian',1,'Vegetarian'),(86,'Halwa',6,60,1,'Low','North Indian',1,'Vegetarian'),(87,'chai',2,10,1,'Low','Other',1,'Vegetarian'),(88,'Daal Chawal',3,90,1,'Medium','North Indian',1,'Vegetarian'),(89,'maggi',1,30,1,'Medium','North Indian',0,'Vegetarian');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
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
