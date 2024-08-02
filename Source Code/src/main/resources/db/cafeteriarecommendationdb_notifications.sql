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
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `notificationId` int NOT NULL AUTO_INCREMENT,
  `senderId` int DEFAULT NULL,
  `notificationType` enum('Recommendation','NewFoodItem','AvailabilityChange','FeedbackRequest') NOT NULL,
  `menuItemId` int DEFAULT NULL,
  `message` text NOT NULL,
  `notificationDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notificationId`),
  KEY `senderId` (`senderId`),
  KEY `menuItemId` (`menuItemId`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`senderId`) REFERENCES `users` (`employeeId`),
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`menuItemId`) REFERENCES `menu` (`menuId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (3,1,'Recommendation',1,'Try our delicious Masala Dosa!','2024-06-19 01:22:00'),(4,3,'NewFoodItem',4,'We have added a new Paneer Tikka to the menu.','2024-06-19 01:22:00'),(5,1,'AvailabilityChange',5,'Chole Bhature is now available all day.','2024-06-19 01:22:00'),(6,3,'FeedbackRequest',7,'Please provide feedback for our new Palak Paneer dish.','2024-06-19 01:22:00'),(7,1,'Recommendation',3,'Our Veg Sandwich is perfect for lunch.','2024-06-19 01:22:00'),(8,6,'NewFoodItem',8,'Introducing Dal Makhani for dinner.','2024-06-19 01:22:00'),(9,1,'AvailabilityChange',6,'Veg Biryani is now available for dinner.','2024-06-19 01:22:00'),(10,3,'FeedbackRequest',2,'Please let us know your thoughts on Aloo Paratha.','2024-06-19 01:22:00'),(14,2,'Recommendation',4,'must eat item','2024-06-19 02:22:05'),(15,2,'Recommendation',5,'Good To eat.','2024-06-19 06:09:01'),(16,2,'Recommendation',2,'must to have.','2024-06-19 13:13:22'),(17,2,'FeedbackRequest',15,'We are trying to improve your experience with Vada. Please provide your feedback and help us.\nQ1. What didnâ€™t you like about Vada?\nQ2. How would you like Vada to taste?\nQ3. Share your momâ€™s recipe.','2024-07-02 02:31:51'),(18,1,'NewFoodItem',87,'A new item \'chai\' has been added to the menu.','2024-07-15 13:00:58'),(19,1,'NewFoodItem',88,'A new item \'daal chawal\' has been added to the menu.','2024-07-15 14:04:13'),(20,1,'AvailabilityChange',88,'The item \'daal chawal\' has had its All fields updated.','2024-07-15 14:06:57'),(21,2,'Recommendation',3,'This item is not available for next week.','2024-07-15 14:13:32'),(22,2,'FeedbackRequest',15,'We are trying to improve your experience with Vada. Please provide your feedback and help us.\n','2024-07-15 15:20:12'),(23,2,'Recommendation',6,'Menu for Lunch','2024-07-29 06:25:26'),(24,1,'NewFoodItem',89,'A new item \'maggi\' has been added to the menu.','2024-08-01 17:22:41');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
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
