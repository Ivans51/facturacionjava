-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: facturacion
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.21-MariaDB

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
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria`
--

LOCK TABLES `auditoria` WRITE;
/*!40000 ALTER TABLE `auditoria` DISABLE KEYS */;
INSERT INTO `auditoria` VALUES (88,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(89,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(90,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(91,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(92,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(93,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(94,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(95,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(96,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(97,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(98,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(99,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(100,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(101,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(102,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(103,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(104,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(105,'2018-02-25',NULL,'Registro usuario admin','admin',1234),(106,'2018-02-26',NULL,'Registro usuario admin','admin',1234),(107,'2018-02-26',NULL,'Registro usuario admin','admin',1234),(108,'2018-02-26',NULL,'Registro usuario admin','admin',1234),(109,'2018-02-26',NULL,'Registro usuario admin','admin',1234),(110,'2018-02-26',NULL,'Registro usuario admin','admin',1234),(111,'2018-02-28',NULL,'Registro usuario admin','admin',1234),(112,'2018-02-28',NULL,'Registro usuario admin','admin',1234),(113,'2018-02-28',NULL,'Registro usuario admin','admin',1234),(114,'2018-03-10',NULL,'Registro usuario Pedro','Pedro',1234567),(115,'2018-03-10',NULL,'Registro usuario Juancho','Juancho',123456),(116,'2018-03-10',NULL,'Registro usuario admin','admin',1234),(117,'2018-03-10',NULL,'Registro usuario admin','admin',1234),(118,'2018-03-10','13','Registro usuario admin','admin',1234),(119,'2018-03-10','13','Registro usuario admin','admin',1234),(120,'2018-03-10','15','Registro usuario admin','admin',1234),(121,'2018-03-10','15','Registro usuario admin','admin',1234),(122,'2018-03-10','15','Registro usuario Juancho','Juancho',123456),(123,'2018-03-10','15','Registro usuario Juancho','Juancho',123456),(124,'2018-03-10','15','Registro usuario Juancho','Juancho',123456),(125,'2018-03-10','15','Registro usuario admin','admin',1234),(126,'2018-03-10','16','Registro usuario admin','admin',1234),(127,'2018-03-10','16','Registro usuario admin','admin',1234),(128,'2018-03-10','16','Registro usuario admin','admin',1234),(129,'2018-03-10','17','Usuario inicio sesionadmin','admin',1234),(130,'2018-03-10','17','Registro del cliente','admin',1234),(131,'2018-03-10','17','Usuario inicio sesionadmin','admin',1234),(132,'2018-03-10','17','Factura generada','admin',1234),(133,'2018-03-10','18','Usuario inicio sesionadmin','admin',1234),(134,'2018-03-10','18','Usuario inicio sesionadmin','admin',1234),(135,'2018-03-10','19','Usuario inicio sesionadmin','admin',1234),(136,'2018-03-10','19','Usuario inicio sesionadmin','admin',1234),(137,'2018-03-10','19','Usuario inicio sesionadmin','admin',1234),(138,'2018-03-10','19','Usuario inicio sesionadmin','admin',1234),(139,'2018-03-10','19','Factura generada','admin',1234),(140,'2018-03-10','20','Usuario inicio sesionadmin','admin',1234),(141,'2018-03-12','7','Usuario inicio sesionadmin','admin',1234),(142,'2018-03-12','7','Usuario inicio sesionadmin','admin',1234),(143,'2018-03-12','7','Usuario inicio sesionadmin','admin',1234),(144,'2018-03-12','7','Usuario inicio sesionadmin','admin',1234),(145,'2018-03-12','8','Usuario inicio sesionadmin','admin',1234);
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
  `nacionalidad` text,
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
INSERT INTO `cliente` VALUES (123,'Ivans','del pino','marcay','23123',1234,'V'),(1234,'Pedro','dlkfj','Maracay','12321',1234,NULL),(1323,'Ivans','Perez','alkdf','12321',1234,'V'),(12345,'Perez','fpasdf','Marca','12321',1234,'V'),(1232415,'Ivans','Del Pino','Marcay','123213',1234,NULL),(1234545,'Pedro','Del ','Maraca','2132',1234,'V'),(2313413,'Juancho','Perez','Maracay','02432345024',1234,'E');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
INSERT INTO `factura` VALUES (6,'Prada, ','','2018-03-10','2018-03-21',1.56,14.56,1234,1234),(7,'','','2018-03-10','2018-03-21',0,0,1232415,1234);
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
  `estado` text,
  PRIMARY KEY (`idservicio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,'Carro',14,'2018-02-25',15,'1'),(2,'Camioneta',12,'2018-02-25',4,'1'),(3,'Moto',15,'2018-02-28',20,'0'),(4,'Prada',12,'2018-02-25',42,'0'),(5,'Radio',12,'2018-02-28',20,'1'),(6,'Mantenimiento',1500000,'2018-03-10',1,'1');
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
  `estado` text,
  `usuario_cedula` int(11) NOT NULL,
  `subservicio_idsubservicio` int(11) NOT NULL,
  PRIMARY KEY (`idsubservicios`,`usuario_cedula`,`subservicio_idsubservicio`),
  UNIQUE KEY `nombre_UNIQUE` (`nombreSub`),
  KEY `fk_servicios_usuario1_idx` (`usuario_cedula`),
  KEY `fk_serviciosub_servicios1_idx` (`subservicio_idsubservicio`),
  CONSTRAINT `fk_servicios_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_serviciosub_servicios1` FOREIGN KEY (`subservicio_idsubservicio`) REFERENCES `servicios` (`idservicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviciosub`
--

LOCK TABLES `serviciosub` WRITE;
/*!40000 ALTER TABLE `serviciosub` DISABLE KEYS */;
INSERT INTO `serviciosub` VALUES (1,'Default',0,'2018-02-24','0','1',1234,1),(5,'Ventana',15,'2018-02-28','10','1',1234,2),(6,'Pared',13,'2018-02-25','13','1',1234,3),(7,'Prada',13,'2018-02-25','11','1',1234,4),(8,'Techo',14,'2018-02-26','21','1',1234,2),(9,'Ventilador',13,'2018-02-26','20','1',1234,2),(10,'Cajon',10,'2018-02-26','24','0',1234,1),(11,'Taurus',13,'2018-02-25','2','0',1234,3),(12,'Pergamino',10,'2018-02-26','29','1',1234,1),(13,'Anime',12,'2018-02-25','12','1',1234,3),(14,'Tablet',12,'2018-02-26','12','1',1234,3),(15,'Compu',12,'2018-02-26','12','0',1234,3),(16,'Samurai',12,'2018-02-28','10','0',1234,2),(17,'Radio',12,'2018-02-28','20','1',1234,5),(18,'Mantenimiento',1500000,'2018-03-10','1','1',1234,6),(19,'Condensador',1000000,'2018-03-10','1','1',1234,6);
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
  `nacionalidad` text,
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
INSERT INTO `usuario` VALUES (1234,'V','admin','1234','desarrollo.theroom@gmail.com','2018-02-24','Gerente'),(12345,'V','Ivans','1234','Ivans1041@mail.com','2018-02-25','Tecnico'),(123456,'V','Juancho','1234','Juancho@gmail.cm','2018-02-25','Asistente'),(1234567,'V','Pedro','1234','Pedro@gmail.com','2018-02-25','Gerente');
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

-- Dump completed on 2018-03-13  1:32:25
