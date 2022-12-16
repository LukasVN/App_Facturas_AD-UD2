# App_Facturas_AD-UD2
Es necesario modificar el archivo Pool estableciendo la ip y nombre de la base de datos para que funcionen las consultas de la aplicación
Implementación de la base de datos con ejemplos:
drop database if exists practicaud2;
create schema practicaud2;

Use practicaud2;

create table productos (
  idproducto varchar(6) not null primary key,
  nomproducto varchar(25) not null unique,
  stock int not null default 0,
  precio float(8,2),
  categoria varchar(10),
  iva float(4,2)
);

create table cliente (
  idcliente varchar(10) not null primary key,
  nombrecli varchar(20),
  apellidocli varchar (25),
  dircli  varchar(30)
);

create table empleado (
  idempleado varchar(10) not null primary key,
  nombreemp varchar(20),
  salario float(6,2),
  incentivo float(6,2),
  operativas int
);

create table factura (
  numfactura varchar(8)  not null primary key,
  idcliente varchar(10) not null references cliente (idcliente),
  idempleado varchar(10) not null references empleado (idempleado),
  fecha datetime,
  cobrada boolean,
  iva float(4,2),
  constraint `fk_idcliente` FOREIGN KEY (`idcliente` ) REFERENCES `practicaud2`.`cliente` (`idcliente` ),
  constraint `fk_idempleado` FOREIGN KEY (`idempleado`) REFERENCES `practicaud2`.`empleado` (`idempleado`)
);


create table detalle ( 
  numfactura varchar(8) not null references factura(numfactura),
  numdetalle smallint(4) unsigned not null, 
  idproducto varchar(25) references productos(idproducto),
  cantidad int,
  precio float (8,2), 
  primary key (numfactura,numdetalle),
  constraint `fk_idproducto` FOREIGN KEY (`idproducto` ) REFERENCES `practicaud2`.`productos` (`idproducto` ),
  constraint `fk_numfactura` FOREIGN KEY (`numfactura` ) REFERENCES `practicaud2`.`factura` (`numfactura` )
);

create table historicofacturadoporcliente (
    idcliente varchar(10) not null primary key,
    nomapecli varchar(45) not null unique,
    importefacturado float (8,2),
    observaciones varchar(600)
);

create table historicocliente (
  hidcliente varchar(10) not null primary key,
  hnombrecli varchar(20),
  hapellidocli varchar (25),
  hdircli  varchar(30)
);

insert into productos values ('A001','ProductoA1',10,3.5,'Categoria1',0.04);
insert into productos values ('A002','ProductoA2',13,4,'Categoria1',0.04);
insert into productos values ('B001','ProductoB1',30,3.7,'Categoria2',0.10);
insert into productos values ('A003','ProductoA3',40,1.5,'Categoria1',0.04);
insert into productos values ('A004','ProductoA4',16,2.5,'Categoria1',0.04);
insert into productos values ('B002','ProductoB3',4,3,'Categoria2',0.10);
insert into productos values ('C001','ProductoC1',2,2,'Categoria3',0.21);

insert into cliente values ('C001','José','Alvariza','C/Ourense , 3 4ºD');
insert into cliente values ('C002','Alma','Santos','C/Lugo , 123 1ºF');
insert into cliente values ('C003','Tania','Pedrosa','C/Pontevedra , 5 bajo');

insert into empleado values ('E001','Jupiter',1000.0,0.491,3);
insert into empleado values ('E002','Saturno',800.0,null,0);
insert into empleado values ('E003','Marte',1200.0,null,0);


/* 14.5, 23,5, 11,1 */
insert into factura values ('F001-21','C002','E001','2022-10-18',false,21);
insert into factura values ('F002-21','C002','E001','2022-10-18',true,10);
insert into factura values ('F003-21','C003','E001','2022-10-18',false,21);


insert into detalle values ('F001-21',1,'A001',3,3.5);
insert into detalle values ('F001-21',2,'A002',1,4);

insert into detalle values ('F002-21',1,'A001',4,3.5);
insert into detalle values ('F002-21',2,'A004',3,2.5);
insert into detalle values ('F002-21',3,'C001',1,2);

insert into detalle values ('F003-21',1,'B001',3,3.7);



insert into historicofacturadoporcliente values ('C999','Manolo borrado',345.6,"F902-15:F923-15:F876-15");

insert into historicocliente values ('C999','Cliente','Borrado','AV Castelao 64');
