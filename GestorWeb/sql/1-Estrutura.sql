/*
Script para criação da estrutra do projeto Gestor.

--------------------------------------------------------------------------------

Versão inicial

Autor: 	Rodrigo M.
Data: 	21/05/2014


--------------------------------------------------------------------------------

Evolução em 29/05/2014:

Criadas entidades: 
    Empresa, 
    UsuarioEmpresa, 
    RelacionamentoEmpresaCliente, 
    Prioridade, 
    StatusMeta, 
    Meta,
    ParticipanteMeta

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

-- Prioridade
DROP TABLE IF EXISTS Prioridade CASCADE;
CREATE TABLE Prioridade (
	idPrioridade SERIAL NOT NULL PRIMARY KEY,
	Prioridade CHARACTER VARYING (50) NOT NULL,
	unique (Prioridade)
) ;

-- Insert mock data 
INSERT INTO Prioridade (Prioridade) VALUES ('MINIMA');
INSERT INTO Prioridade (Prioridade) VALUES ('BAIXA');
INSERT INTO Prioridade (Prioridade) VALUES ('NORMAL');
INSERT INTO Prioridade (Prioridade) VALUES ('ALTA');
INSERT INTO Prioridade (Prioridade) VALUES ('URGENTE');

-- StatusMeta
DROP TABLE IF EXISTS StatusMeta CASCADE;
CREATE TABLE StatusMeta (
	idStatusMeta SERIAL NOT NULL PRIMARY KEY,
	StatusMeta CHARACTER VARYING (50) NOT NULL,
	unique (StatusMeta)
) ;

-- Insert mock data 
INSERT INTO StatusMeta (StatusMeta) VALUES ('Não Iniciada');
INSERT INTO StatusMeta (StatusMeta) VALUES ('Em Andamento');
INSERT INTO StatusMeta (StatusMeta) VALUES ('Bloqueada');
INSERT INTO StatusMeta (StatusMeta) VALUES ('Suspensa');
INSERT INTO StatusMeta (StatusMeta) VALUES ('Concluída');

-- Meta 
DROP TABLE IF EXISTS meta CASCADE;
CREATE TABLE meta (
    idMeta SERIAL NOT NULL PRIMARY KEY, 
    idEmpresa BIGINT NOT NULL, 
    nome CHARACTER VARYING (100) NOT NULL,
    descricao TEXT NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim DATE NOT NULL,
    idPrioridade BIGINT NOT NULL,
    idStatusMeta BIGINT NOT NULL,
    idUsuarioResponsavel BIGINT NOT NULL,
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idPrioridade) REFERENCES Prioridade(idPrioridade),	
    FOREIGN KEY (idStatusMeta) REFERENCES StatusMeta(idStatusMeta),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario)	
);

-- Insert mock data 
INSERT INTO Meta (idEmpresa, nome, descricao, dataInicio, dataFim, idPrioridade, idStatusMeta, idUsuarioResponsavel)
    VALUES (1, 'Nome da meta', '<h1>descricao</h1', CURRENT_DATE, CURRENT_DATE+10, 1, 1, 1);


-- Participantes da Meta
DROP TABLE IF EXISTS ParticipanteMeta CASCADE;
CREATE TABLE ParticipanteMeta (
	idParticipanteMeta SERIAL NOT NULL PRIMARY KEY,
	idMeta BIGINT NOT NULL,
	idUsuario BIGINT NOT NULL,
	FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario),	
	FOREIGN KEY (idMeta) REFERENCES Meta(idMeta)
);

-- Insert mock data 
INSERT INTO ParticipanteMeta (idMeta, idUsuario) VALUES (1, 2);
INSERT INTO ParticipanteMeta (idMeta, idUsuario) VALUES (1, 3);

-- 100 chars
-- 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
--          1         1         1         1         1         1         1         1         1         1

