/**

Script para criação da estrutra do projeto teamer.

Autor: 	Rodrigo M.
Data: 	21/05/2014


 */


-- Usuário
drop table if exists usuario cascade;
create table usuario (
	idUsuario SERIAL not null primary key,
	nome character varying (200) not null ,
	login character varying (100) not null,
	senha character (32) not null,
	unique (login)
) ;

