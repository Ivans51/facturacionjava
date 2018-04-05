-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 04-04-2018 a las 13:18:52
-- Versión del servidor: 10.1.21-MariaDB
-- Versión de PHP: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `facturacion`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auditoria`
--

CREATE TABLE `auditoria` (
  `idauditoria` int(11) NOT NULL,
  `fecha` date DEFAULT NULL,
  `hora` varchar(45) DEFAULT NULL,
  `accion` varchar(45) DEFAULT NULL,
  `nombreUsuario` varchar(45) DEFAULT NULL,
  `usuario_cedula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `auditoria`
--

INSERT INTO `auditoria` (`idauditoria`, `fecha`, `hora`, `accion`, `nombreUsuario`, `usuario_cedula`) VALUES
(1, '2018-03-27', '07:26 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(2, '2018-03-27', '07:27 AM', 'Registro del servicio', 'admin', 12345678),
(3, '2018-03-27', '07:27 AM', 'Registro del cliente', 'admin', 12345678),
(4, '2018-03-27', '07:29 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(5, '2018-03-27', '07:39 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(6, '2018-03-27', '07:42 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(7, '2018-03-27', '07:46 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(8, '2018-03-27', '07:54 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(9, '2018-03-29', '12:50 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(10, '2018-03-29', '01:22 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(11, '2018-03-29', '01:36 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(12, '2018-03-29', '01:45 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(13, '2018-03-29', '02:33 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(14, '2018-03-29', '03:03 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(15, '2018-03-29', '03:26 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(16, '2018-03-29', '03:37 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(17, '2018-03-29', '03:50 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(18, '2018-03-29', '04:04 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(19, '2018-03-29', '04:06 PM', 'Registro del servicio', 'admin', 12345678),
(20, '2018-03-29', '04:07 PM', 'Registro del servicio', 'admin', 12345678),
(21, '2018-03-29', '04:07 PM', 'Registro del servicio', 'admin', 12345678),
(22, '2018-03-29', '04:36 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(23, '2018-03-29', '04:39 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(24, '2018-03-29', '04:40 PM', 'Registro del servicio', 'admin', 12345678),
(25, '2018-03-29', '04:40 PM', 'Registro del servicio', 'admin', 12345678),
(26, '2018-03-30', '07:30 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(27, '2018-03-30', '08:44 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(28, '2018-03-30', '11:19 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(29, '2018-03-30', '01:02 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(30, '2018-04-02', '07:38 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(31, '2018-04-02', '07:42 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(32, '2018-04-02', '07:50 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(33, '2018-04-02', '09:35 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(34, '2018-04-03', '06:36 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(35, '2018-04-03', '06:41 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(36, '2018-04-03', '06:58 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(37, '2018-04-03', '07:20 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(38, '2018-04-03', '07:22 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(39, '2018-04-03', '07:23 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(40, '2018-04-03', '07:25 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(41, '2018-04-03', '07:27 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(42, '2018-04-03', '07:31 AM', 'Registro del servicio', 'admin', 12345678),
(43, '2018-04-03', '07:31 AM', 'Registro del servicio', 'admin', 12345678),
(44, '2018-04-03', '07:39 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(45, '2018-04-03', '07:43 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(46, '2018-04-03', '07:46 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(47, '2018-04-03', '07:49 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(48, '2018-04-03', '08:23 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(49, '2018-04-03', '08:25 PM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(50, '2018-04-04', '06:28 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(51, '2018-04-04', '06:32 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(52, '2018-04-04', '06:40 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(53, '2018-04-04', '06:46 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(54, '2018-04-04', '06:56 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(55, '2018-04-04', '07:03 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(56, '2018-04-04', '07:08 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(57, '2018-04-04', '07:12 AM', 'Usuario inicio sesionadmin', 'admin', 12345678),
(58, '2018-04-04', '07:15 AM', 'Usuario inicio sesionadmin', 'admin', 12345678);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `cedula` int(11) NOT NULL,
  `nombres` varchar(45) DEFAULT NULL,
  `apellidos` varchar(45) DEFAULT NULL,
  `direccion` varchar(45) DEFAULT NULL,
  `telefono` varchar(45) DEFAULT NULL,
  `usuario_cedula` int(11) NOT NULL,
  `nacionalidad` text,
  `fecha` date NOT NULL,
  `estado` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`cedula`, `nombres`, `apellidos`, `direccion`, `telefono`, `usuario_cedula`, `nacionalidad`, `fecha`, `estado`) VALUES
(21313213, 'Ivans', 'Del Pino', 'Maracay', '12382138293', 12345678, 'V', '2018-03-27', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `factura`
--

CREATE TABLE `factura` (
  `idfactura` int(11) NOT NULL,
  `servicios` varchar(1000) DEFAULT NULL,
  `forma_pago` varchar(45) DEFAULT NULL,
  `fecha_pago` date DEFAULT NULL,
  `duracion` varchar(40) DEFAULT NULL,
  `IVA` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `nameFile` varchar(100) NOT NULL,
  `cliente_cedula` int(11) NOT NULL,
  `usuario_cedula` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `factura`
--

INSERT INTO `factura` (`idfactura`, `servicios`, `forma_pago`, `fecha_pago`, `duracion`, `IVA`, `total`, `nameFile`, `cliente_cedula`, `usuario_cedula`) VALUES
(1, 'Bomba x 1', 'Efectivo', '2018-03-27', '12Hr.', 144000, 1344000, '', 21313213, 12345678),
(2, 'Bomba x 1', 'Efectivo', '2018-03-29', '12Hr.', 144000, 1344000, '', 21313213, 12345678),
(3, 'Bomba x 1', 'Efectivo', '2018-03-29', '12Hr.', 144000, 1344000, 'Factura29-3-2018-49515.pdf', 21313213, 12345678),
(4, 'Bomba x 1', 'Transferencia', '2018-03-29', '12Hr.', 144000, 1344000, 'Factura29-3-2018-52440.pdf', 21313213, 12345678),
(5, 'Bomba x 1', 'Transferencia', '2018-03-29', '12Hr.', 144000, 1344000, 'Factura29-3-2018-54223.pdf', 21313213, 12345678),
(6, 'Capon Libre x 1 ,Bomba x 1 ,Mantenimiento de agua x 2 ,Luz Electrica x 1', 'Efectivo', '2018-03-29', '12Hr.', 5771719.68, 53869383.68, 'Factura29-3-2018-58228.pdf', 21313213, 12345678),
(7, 'Imperial circus x 2', 'Efectivo', '2018-03-29', '12Hr.', 2880000, 26880000, 'Factura29-3-2018-60054.pdf', 21313213, 12345678),
(8, 'Capon Libre x 1 ,Vapor de calo x 1', 'Transferencia', '2018-04-02', '21Hr.', 2414400, 22534400, 'Factura2-4-2018-27790.pdf', 21313213, 12345678),
(9, 'Car Clean x 1, Capon Libre x 1, Car Search x 1, Bomba x 1, Mantenimiento de agua x 1', 'Efectivo', '2018-04-04', '12Hr.', 3168000, 29568000, 'Factura4-4-2018-24438.pdf', 21313213, 12345678);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `servicios`
--

CREATE TABLE `servicios` (
  `idservicio` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `precio` double DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `tiempo_estimado` int(45) DEFAULT NULL,
  `estado` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `servicios`
--

INSERT INTO `servicios` (`idservicio`, `nombre`, `precio`, `fecha`, `tiempo_estimado`, `estado`) VALUES
(1, 'Bomba', 1200000, '2018-03-27', 12, 1),
(2, 'Mantenimiento de agua', 3100000, '2018-03-29', 2, 1),
(3, 'Luz Electrica', 11200000, '2018-03-29', 12, 1),
(4, 'Capon Libre', 20000000, '2018-03-29', 12, 1),
(5, 'Imperial circus', 12000000, '2018-03-29', 10, 1),
(6, 'Vapor de calo', 120000, '2018-03-29', 21, 1),
(7, 'Car Clean', 2000000, '2018-04-03', 1, 1),
(8, 'Car Search', 100000, '2018-04-03', 6, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `cedula` int(11) NOT NULL,
  `nacionalidad` text,
  `nombre` varchar(45) DEFAULT NULL,
  `clave` varchar(45) DEFAULT NULL,
  `correo` varchar(45) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `status` text,
  `estado` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`cedula`, `nacionalidad`, `nombre`, `clave`, `correo`, `fecha`, `status`, `estado`) VALUES
(12345678, 'V', 'admin', '12345678', 'admin@gmail.com', '2018-02-24', 'Gerente', 1),
(20888555, 'V', 'Alejandro', '12345678', 'alevargasdj@gmail.com', '2018-03-19', 'Gerente', 1),
(21232132, 'V', 'Pedro41', '12345678', 'pedro@gmail.com', '2018-03-20', 'Tecnico', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD PRIMARY KEY (`idauditoria`,`usuario_cedula`),
  ADD KEY `fk_auditoria_usuario_idx` (`usuario_cedula`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`cedula`,`usuario_cedula`),
  ADD KEY `fk_cliente_usuario1_idx` (`usuario_cedula`);

--
-- Indices de la tabla `factura`
--
ALTER TABLE `factura`
  ADD PRIMARY KEY (`idfactura`,`cliente_cedula`,`usuario_cedula`),
  ADD KEY `fk_factura_cliente1_idx` (`cliente_cedula`),
  ADD KEY `fk_factura_usuario1_idx` (`usuario_cedula`);

--
-- Indices de la tabla `servicios`
--
ALTER TABLE `servicios`
  ADD PRIMARY KEY (`idservicio`),
  ADD UNIQUE KEY `nombre_UNIQUE` (`nombre`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`cedula`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  MODIFY `idauditoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT de la tabla `factura`
--
ALTER TABLE `factura`
  MODIFY `idfactura` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT de la tabla `servicios`
--
ALTER TABLE `servicios`
  MODIFY `idservicio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD CONSTRAINT `fk_auditoria_usuario` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `fk_cliente_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `factura`
--
ALTER TABLE `factura`
  ADD CONSTRAINT `fk_factura_cliente1` FOREIGN KEY (`cliente_cedula`) REFERENCES `cliente` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_factura_usuario1` FOREIGN KEY (`usuario_cedula`) REFERENCES `usuario` (`cedula`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
