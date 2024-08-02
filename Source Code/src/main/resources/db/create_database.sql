CREATE TABLE Users (
    employeeId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    lastLogin TIMESTAMP
);

CREATE TABLE Roles (
    roleId INT AUTO_INCREMENT PRIMARY KEY,
    roleName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE UserRoles (
    userId INT,
    roleId INT,
    PRIMARY KEY (userId, roleId),
    FOREIGN KEY (userId) REFERENCES Users(employeeId),
    FOREIGN KEY (roleId) REFERENCES Roles(roleId)
);

CREATE TABLE `userloginattempts` (
    attemptId INT AUTO_INCREMENT PRIMARY KEY,
  `attemptId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `attemptTimestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`attemptId`),
  KEY `userId` (`userId`),
  CONSTRAINT `userloginattempts_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`employeeId`)
);

CREATE TABLE FoodCategories (
    categoryId INT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Menu (
    menuId INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    categoryId INT,
    price FLOAT NOT NULL,
    availability BOOLEAN NOT NULL,
    spiceLevel ENUM('High', 'Medium', 'Low') DEFAULT 'Medium',
    cuisineType ENUM('North Indian', 'South Indian', 'Chinese', 'Italian', 'Mexican', 'Other') DEFAULT 'Other',
    isSweet BOOLEAN DEFAULT FALSE,
    dietaryPreference ENUM('Vegetarian', 'Non Vegetarian', 'Eggetarian') DEFAULT 'Vegetarian',
    FOREIGN KEY (categoryId) REFERENCES FoodCategories(categoryId)
);

CREATE TABLE UserPreferences (
    preferenceId INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT,
    dietaryPreference ENUM('Vegetarian', 'Non Vegetarian', 'Eggetarian') DEFAULT 'Vegetarian',
    spiceLevel ENUM('High', 'Medium', 'Low') DEFAULT 'Medium',
    preferredCuisine ENUM('North Indian', 'South Indian', 'Chinese', 'Italian', 'Mexican', 'Other') DEFAULT 'Other',
    sweetTooth BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (employeeId) REFERENCES Users(employeeId)
);

CREATE TABLE Notifications (
    notificationId INT AUTO_INCREMENT PRIMARY KEY,
    senderId INT,
    notificationType ENUM('Recommendation', 'NewFoodItem', 'AvailabilityChange', 'FeedbackRequest') NOT NULL,
    menuItemId INT,
    message TEXT NOT NULL,
    notificationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (senderId) REFERENCES Users(employeeId),
    FOREIGN KEY (menuItemId) REFERENCES Menu(menuId)
);

CREATE TABLE UserNotifications (
    userNotificationId INT AUTO_INCREMENT PRIMARY KEY,
    receiverId INT,
    notificationId INT,
    isRead BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (receiverId) REFERENCES Users(employeeId),
    FOREIGN KEY (notificationId) REFERENCES Notifications(notificationId)
);

CREATE TABLE Feedback (
    feedbackId INT AUTO_INCREMENT PRIMARY KEY,
    employeeId INT,
    menuId INT,
    quality INT NOT NULL,
    valueForMoney INT NOT NULL,
    quantity INT NOT NULL,
    taste INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    feedbackDate DATE,
    FOREIGN KEY (employeeId) REFERENCES Users(employeeId),
    FOREIGN KEY (menuId) REFERENCES Menu(menuId)
);

CREATE TABLE RecommendationEngine (
    recommendationId INT AUTO_INCREMENT PRIMARY KEY,
    menuId INT,
    averageRating FLOAT,
    sentimentAnalysis VARCHAR(255),
    recommendationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (menuId) REFERENCES Menu(menuId)
);

CREATE TABLE FeedbackQuery (
    feedbackQueryId INT AUTO_INCREMENT PRIMARY KEY,
    notificationId INT,
    employeeId INT,
    question TEXT NOT NULL,
    response TEXT NOT NULL,
    responseDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (notificationId) REFERENCES Notifications(notificationId),
    FOREIGN KEY (employeeId) REFERENCES Users(employeeId)
);

CREATE TABLE Polls (
pollId INT AUTO_INCREMENT PRIMARY KEY,
chefId INT NOT NULL,
pollDate DATE NOT NULL,
FOREIGN KEY (chefId) REFERENCES Users(employeeId)
);

CREATE TABLE PollItems (
pollItemId INT AUTO_INCREMENT PRIMARY KEY,
pollId INT NOT NULL,
menuItemId INT NOT NULL,
FOREIGN KEY (pollId) REFERENCES Polls(pollId),
FOREIGN KEY (menuItemId) REFERENCES Menu(menuId)
);

CREATE TABLE Votes (
voteId INT AUTO_INCREMENT PRIMARY KEY,
pollId INT NOT NULL,
menuItemId INT NOT NULL,
employeeId INT NOT NULL,
voteDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UNIQUE KEY UC_Poll_Employee (pollId, employeeId),
FOREIGN KEY (pollId) REFERENCES Polls(pollId),
FOREIGN KEY (menuItemId) REFERENCES Menu(menuId),
FOREIGN KEY (employeeId) REFERENCES Users(employeeId)
);