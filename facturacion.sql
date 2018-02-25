-- MySQL dump 10.16  Distrib 10.1.21-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.1.21-MariaDB

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
-- Table structure for table `auditoria`
--

DROP TABLE IF EXISTS `auditoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auditoria` (
  `idauditoria` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `hora` varchar(45) DEFAULT NULL,
  `accion` varchar(45) DEFAULT NULL,
  `nombreUsuario` varchar(45) DEFAULT NULL,
  `usuario_cedula` int(11) NOT NULL,
  PRIMARY KEY (`idauditoria`,`usuario_cedula`),
  KEY `fk_auditoria_usuario_idx` (`usuario_cedula`),
  CONSTRAINT `fk_auditoria_usuario` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
INSERT INTO `auditoria` VALUES (88,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(89,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(90,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(91,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(92,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(93,'2018-02-23',NULL,'Registro usuario admin','admin',2345678),(94,'2018-02-24',NULL,'Registro usuario admin','admin',2345678),(95,'2018-02-24',NULL,'Registro usuario admin','admin',2345678),(96,'2018-02-24',NULL,'Registro usuario admin','admin',2345678),(97,'2018-02-24',NULL,'Registro usuario admin','admin',2345678);
/*!40000 ALTER TABLE `auditoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cliente` (
  `cedula` int(11) NOT NULL,
  `nombres` varchar(45) DEFAULT NULL,
  `apellidos` varchar(45) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `usuario_cedula` int(11) NOT NULL,
  PRIMARY KEY (`cedula`,`usuario_cedula`),
  KEY `fk_cliente_usuario1_idx` (`usuario_cedula`),
  CONSTRAINT `fk_cliente_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (1234,'Pepito','Rodriguez','Maracya','12343',2345678);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factura`
--

DROP TABLE IF EXISTS `factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `factura` (
  `idfactura` int(11) NOT NULL AUTO_INCREMENT,
  `servicios` varchar(45) DEFAULT NULL,
  `forma_pago` varchar(45) DEFAULT NULL,
  `fecha_pago` date DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `IVA` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `cliente_cedula` int(11) NOT NULL,
  `usuario_cedula` int(11) NOT NULL,
  PRIMARY KEY (`idfactura`,`cliente_cedula`,`usuario_cedula`),
  KEY `fk_factura_cliente1_idx` (`cliente_cedula`),
  KEY `fk_factura_usuario1_idx` (`usuario_cedula`),
  CONSTRAINT `fk_factura_cliente1` FOREIGN KEY (`cliente_cedula`) REFERENCES `cliente` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_factura_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenes`
--

DROP TABLE IF EXISTS `ordenes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ordenes` (
  `idordenes` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `factura_idfactura` int(11) NOT NULL,
  PRIMARY KEY (`idordenes`,`factura_idfactura`),
  KEY `fk_ordenes_factura1_idx` (`factura_idfactura`),
  CONSTRAINT `fk_ordenes_factura1` FOREIGN KEY (`factura_idfactura`) REFERENCES `factura` (`idfactura`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenes`
--

LOCK TABLES `ordenes` WRITE;
/*!40000 ALTER TABLE `ordenes` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordenes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicios` (
  `idservicio` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `precio` double DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `tiempo_estimado` int(45) DEFAULT NULL,
  PRIMARY KEY (`idservicio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Carro',20,'2018-02-23',12);
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serviciosub`
--

DROP TABLE IF EXISTS `serviciosub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serviciosub` (
  `idsubservicios` int(11) NOT NULL AUTO_INCREMENT,
  `nombreSub` varchar(45) NOT NULL,
  `precioSub` double DEFAULT NULL,
  `fechaSub` date DEFAULT NULL,
  `tiempo_estimadoSub` varchar(45) DEFAULT NULL,
  `usuario_cedula` int(11) NOT NULL,
  `subservicio_idsubservicio` int(11) NOT NULL,
  PRIMARY KEY (`idsubservicios`,`usuario_cedula`,`subservicio_idsubservicio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombreSub`),
  KEY `fk_servicios_usuario1_idx` (`usuario_cedula`),
  KEY `fk_serviciosub_servicios1_idx` (`subservicio_idsubservicio`),
  CONSTRAINT `fk_servicios_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_serviciosub_servicios1` FOREIGN KEY (`subservicio_idsubservicio`) REFERENCES `servicios` (`idservicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviciosub`
--

LOCK TABLES `serviciosub` WRITE;
/*!40000 ALTER TABLE `serviciosub` DISABLE KEYS */;
INSERT INTO `serviciosub` VALUES (5,'Aire',20,'2018-02-23','12',2345678,1);
/*!40000 ALTER TABLE `serviciosub` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `cedula` int(11) NOT NULL,
  `nombre` varchar(45) DEFAULT NULL,
  `clave` varchar(45) DEFAULT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `status` text,
  PRIMARY KEY (`cedula`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (2345678,'admin','1234','desarrollo.theroom@gmail.com','2018-02-22','Administrador');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-02-24 18:03:03
