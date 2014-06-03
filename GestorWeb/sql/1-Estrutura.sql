﻿/*

Script para criação da estrutra do projeto Gestor.

--------------------------------------------------------------------------------

Data: 	21/05/2014
Autor: 	Rodrigo M.

Versão inicial


--------------------------------------------------------------------------------

Data: 	02/06/2014
Autor: 	Rodrigo M.

Criadas entidades: 
    Empresa, 
    UsuarioEmpresa, 
    RelacionamentoEmpresaCliente, 
    Departamento, 
    CentroCusto, 
    Meta,

Inseridos dados para teste (mockdata)

--------------------------------------------------------------------------------

 */



-- Usuário
DROP TABLE IF EXISTS usuario CASCADE;
CREATE TABLE usuario (
	idUsuario SERIAL NOT NULL PRIMARY KEY,
	nome CHARACTER VARYING (200) NOT NULL ,
	login CHARACTER VARYING (100) NOT NULL,
	senha CHARACTER (32) NOT NULL,
	UNIQUE (login)
);

-- Insert mock data
INSERT INTO usuario ( nome, login, senha ) VALUES ('rodrigo', 'rodrigo.ccn2005@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==');
INSERT INTO usuario ( nome, login, senha ) VALUES ('fernando', 'fernando.saax@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==');
INSERT INTO usuario ( nome, login, senha ) VALUES ('daniel', 'danielstavale@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==');

-- Empresa
DROP TABLE IF EXISTS empresa CASCADE;
CREATE TABLE empresa (
	idempresa SERIAL NOT NULL PRIMARY KEY,
	nome CHARACTER VARYING (100) NOT NULL ,
	cnpj CHARACTER (18) NOT NULL,
	ativa BOOLEAN NOT NULL,
	UNIQUE (nome),
	UNIQUE (cnpj)
);

-- Insert mock data
INSERT INTO empresa (nome, cnpj, ativa) VALUES ('SAAX', '12.345.678/0001-00', TRUE);
INSERT INTO empresa (nome, cnpj, ativa) VALUES ('Algum cliente SAAX', '98.765.432/0001-00', TRUE);

-- Empresa atual do usuário
ALTER TABLE usuario ADD COLUMN idEmpresaAtual BIGINT;
ALTER TABLE usuario ADD CONSTRAINT usuario_idEmpresaAtual_fkey FOREIGN KEY (idEmpresaAtual) REFERENCES Empresa (idEmpresa);

-- Insert mock data 
UPDATE usuario SET idEmpresaAtual = 1;

-- Relacionamento: Usuario <-> Empresa 
-- ( supondo que um usuário poderá migrar de empresa )
DROP TABLE IF EXISTS UsuarioEmpresa CASCADE;
CREATE TABLE UsuarioEmpresa (
	idUsuarioEmpresa SERIAL NOT NULL PRIMARY KEY,
	idUsuario BIGINT NOT NULL,
	idEmpresa BIGINT NOT NULL,
        administrador BOOLEAN NOT NULL,
	contratacao DATE NOT NULL,
	desligamento DATE,
	FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),	
	FOREIGN KEY (idEmpresa) REFERENCES empresa(idEmpresa)
);

-- Insert mock data 
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao ) VALUES (1, 1, TRUE, CURRENT_DATE);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao ) VALUES (2, 1, TRUE, CURRENT_DATE);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao ) VALUES (3, 1, TRUE, CURRENT_DATE);

-- Empresas "Cliente"
-- Relacionamento entre as empresas gestoras (Datacompany) e empresas cliente (Covabra)
DROP TABLE IF EXISTS RelacionamentoEmpresaCliente CASCADE;
CREATE TABLE RelacionamentoEmpresaCliente (
	idRelacionamentoEmpresaCliente SERIAL NOT NULL PRIMARY KEY,
	idEmpresaGestora BIGINT NOT NULL,
	idEmpresaCliente BIGINT NOT NULL,
	contratoAtivo BOOLEAN NOT NULL,
	inicioContrato DATE NOT NULL,
	terminoContrato DATE,
	FOREIGN KEY (idEmpresaGestora) REFERENCES Empresa(idEmpresa),	
	FOREIGN KEY (idEmpresaCliente) REFERENCES empresa(idEmpresa)
);

-- Insert mock data 
INSERT INTO RelacionamentoEmpresaCliente (idEmpresaGestora, idEmpresaCliente, contratoAtivo, inicioContrato) 
    VALUES (1, 2, TRUE, CURRENT_DATE);

-- Departamento
DROP TABLE IF EXISTS Departamento CASCADE;
CREATE TABLE Departamento (
	idDepartamento SERIAL NOT NULL PRIMARY KEY,
	Departamento CHARACTER VARYING (50) NOT NULL,
	unique (Departamento)
) ;

-- Insert mock data 
INSERT INTO Departamento (Departamento) VALUES ('JURIDICO');
INSERT INTO Departamento (Departamento) VALUES ('ADMINISTRATIVO');
INSERT INTO Departamento (Departamento) VALUES ('PESSOAL');

-- Centro de Custo
DROP TABLE IF EXISTS CentroCusto CASCADE;
CREATE TABLE CentroCusto (
	idCentroCusto SERIAL NOT NULL PRIMARY KEY,
	CentroCusto CHARACTER VARYING (50) NOT NULL,
	unique (CentroCusto)
) ;

-- Insert mock data 
INSERT INTO CentroCusto (CentroCusto) VALUES ('CC1');
INSERT INTO CentroCusto (CentroCusto) VALUES ('CC2');

-- Meta 
DROP TABLE IF EXISTS meta CASCADE;
CREATE TABLE meta (
    idMeta SERIAL NOT NULL PRIMARY KEY, 
    idEmpresa BIGINT NOT NULL, 
    nome CHARACTER VARYING (100) NOT NULL,
    descricao TEXT NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim DATE NOT NULL, 	-- data esperada para o fim da meta, pode ser diferente da data real do termino da mesma
    dataTermino DATE, 		-- data real do termino da tarefa, portando pode ser nulo
    idEmpresaCliente BIGINT NOT NULL, 
    horasEstimadas TIME NOT NULL,
    horasRealizadas TIME,
    idUsuarioResponsavel BIGINT NOT NULL,
    idDepartamento BIGINT,
    idCentroCusto BIGINT,
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idEmpresaCliente) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento),	
    FOREIGN KEY (idCentroCusto) REFERENCES CentroCusto(idCentroCusto),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario)	
);




-- 100 chars
-- 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
--          1         1         1         1         1         1         1         1         1         1

