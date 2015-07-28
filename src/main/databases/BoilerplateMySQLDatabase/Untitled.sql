CREATE DATABASE  IF NOT EXISTS `MySqlDevDB` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `MySqlDevDB`;
-- MySQL dump 10.13  Distrib 5.6.22, for osx10.8 (x86_64)
--
-- Host: 127.0.0.1    Database: MySqlDevDB
-- ------------------------------------------------------
-- Server version	5.6.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Configurations`
--

DROP TABLE IF EXISTS `Configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Configurations` (
  `Id` bigint(10) NOT NULL,
  `ConfigurationKey` varchar(64) NOT NULL,
  `ConfigurationValue` varchar(512) DEFAULT NULL,
  `Version` varchar(64) NOT NULL,
  `Enviornment` varchar(64) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MethodRoles`
--

DROP TABLE IF EXISTS `MethodRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MethodRoles` (
  `Id` bigint(10) NOT NULL,
  `MethodName` varchar(1024) DEFAULT NULL,
  `RoleId` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Roles_Id_To_MethodRoles_RoleId_idx` (`RoleId`),
  CONSTRAINT `Roles_Id_To_MethodRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `Roles` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Roles`
--

DROP TABLE IF EXISTS `Roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Roles` (
  `Id` bigint(10) NOT NULL,
  `RoleName` varchar(128) NOT NULL,
  `RoleDescription` varchar(1024) DEFAULT NULL,
  `IsSystemRole` bit(1) NOT NULL,
  `IsSelfAssign` bit(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Sessions`
--

DROP TABLE IF EXISTS `Sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Sessions` (
  `ID` bigint(10) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(10) NOT NULL,
  `CreatedDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdateDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `SessionId` varchar(128) NOT NULL,
  `SessionEntity` longtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4214 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserEntityPermissions`
--

DROP TABLE IF EXISTS `UserEntityPermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserEntityPermissions` (
  `Id` bigint(10) NOT NULL,
  `UserId` bigint(10) NOT NULL,
  `EntityId` varchar(512) NOT NULL,
  `Create` int(11) DEFAULT NULL,
  `Read` int(11) DEFAULT NULL,
  `Update` int(11) DEFAULT NULL,
  `Delete` int(11) DEFAULT NULL,
  `Owner` int(11) DEFAULT NULL,
  `Visible` int(11) DEFAULT NULL,
  `Custom` varchar(1024) DEFAULT NULL,
  KEY `UserEntityPermission_Idx_UserId` (`UserId`),
  CONSTRAINT `User_UserId_To_UserEntityPermissions_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserMetaData`
--

DROP TABLE IF EXISTS `UserMetaData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserMetaData` (
  `ID` bigint(10) NOT NULL AUTO_INCREMENT,
  `UserId` bigint(10) NOT NULL,
  `MetaDataKey` varchar(128) NOT NULL,
  `MetaDataValue` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Idx_UserId` (`UserId`),
  CONSTRAINT `Users_Id_To_UserMetaData_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserRoles`
--

DROP TABLE IF EXISTS `UserRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserRoles` (
  `Id` bigint(10) NOT NULL AUTO_INCREMENT,
  `RoleId` bigint(10) DEFAULT NULL,
  `UserId` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `UserRoles_Idx_RoleId` (`RoleId`),
  KEY `UserRoles_Idx_UserId` (`UserId`),
  CONSTRAINT `Role_RoleId_To_UserRoles_RoleId` FOREIGN KEY (`RoleId`) REFERENCES `Roles` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `User_UserId_To_UserRoles_UserId` FOREIGN KEY (`UserId`) REFERENCES `Users` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `Id` bigint(10) NOT NULL AUTO_INCREMENT,
  `UserId` varchar(64) NOT NULL,
  `PasswordHash` varchar(64) NOT NULL,
  `AuthenticationProvider` varchar(64) NOT NULL,
  `ExternalSystemId` varchar(64) NOT NULL,
  `CreatedDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdateDateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UserId_UNIQUE` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=6233 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-27 21:58:43
