

DROP TABLE IF EXISTS RelacionamentoEmpresaCliente CASCADE;



-- Usuário
DROP TABLE IF EXISTS usuario CASCADE;
CREATE TABLE usuario (
	idUsuario SERIAL NOT NULL PRIMARY KEY,
	nome CHARACTER VARYING (100) NOT NULL ,
	sobrenome CHARACTER VARYING (100) NOT NULL ,
	login CHARACTER VARYING (100) NOT NULL,
	senha CHARACTER (32) NOT NULL,
	idUsuarioInclusao INTEGER,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	UNIQUE (login)
);

-- Insert mock data
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('rodrigo', 'moreira','rodrigo.ccn2005@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('fernando', 'stavale','fernando.saax@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('daniel', 'stavale', 'danielstavale@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);

-- Estado
DROP TABLE IF EXISTS Estado CASCADE;
CREATE TABLE Estado (
	idEstado SERIAL NOT NULL PRIMARY KEY,
        nome CHARACTER VARYING (100) NOT NULL,
        uf CHARACTER (2) NOT NULL,
        UNIQUE (uf),
        UNIQUE (nome)
);

-- Cidade
DROP TABLE IF EXISTS Cidade CASCADE;
CREATE TABLE Cidade (
	idCidade SERIAL NOT NULL PRIMARY KEY,
        idEstado BIGINT NOT NULL,
        nome CHARACTER VARYING (100) NOT NULL,
        FOREIGN KEY (idEstado) REFERENCES Estado(idEstado),	
        UNIQUE (nome,idEstado)
);


-- Endereco
-- Tabela criada para armazenar todos os enderecos
DROP TABLE IF EXISTS Endereco CASCADE;
CREATE TABLE Endereco (
	idEndereco SERIAL NOT NULL PRIMARY KEY,
        logradouro CHARACTER VARYING (255) NOT NULL ,
        numero CHARACTER VARYING (10) NOT NULL ,
        complemento CHARACTER VARYING (20) NOT NULL ,
        cep CHARACTER (10) NOT NULL ,
        idCidade BIGINT NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
        FOREIGN KEY (idCidade) REFERENCES Cidade(idCidade)
        
);

-- Empresa
-- Empresa que adquiriu o software: cliente da Saax
DROP TABLE IF EXISTS Empresa CASCADE;
CREATE TABLE Empresa (
	idEmpresa SERIAL NOT NULL PRIMARY KEY,
        idEmpresaPrincipal BIGINT,
	nome CHARACTER VARYING (100) NOT NULL ,
	razaoSocial CHARACTER VARYING (150) NOT NULL ,
	tipoPessoa CHARACTER (1) NOT NULL, -- Fisica / Juridica
	cnpj CHARACTER (18),
	cpf CHARACTER (14),
	ativa BOOLEAN NOT NULL,
	idEndereco BIGINT,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
        FOREIGN KEY (idEmpresaPrincipal) REFERENCES Empresa(idEmpresa),	
        FOREIGN KEY (idEndereco) REFERENCES Endereco(idEndereco),	
	UNIQUE (razaoSocial),
	UNIQUE (cnpj),
	UNIQUE (cpf)
);

INSERT INTO Empresa (tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES ('J', 'DataCompany', 'DataCompany LTDA', '12.345.678/0001-00', TRUE, 1, current_timestamp);
INSERT INTO Empresa (tipopessoa, idEmpresaPrincipal, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES ('J', 1, 'Empresa da corporação DataCompany', 'Empresa LTDA', '12.345.678/0001-01', TRUE, 1, current_timestamp);

-- FilialEmpresa
DROP TABLE IF EXISTS FilialEmpresa CASCADE;
CREATE TABLE FilialEmpresa (
	idFilialEmpresa SERIAL NOT NULL PRIMARY KEY,
        idEmpresa BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
        cnpj CHARACTER (18),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	UNIQUE (idEmpresa,nome)
);

-- Insert mock data
INSERT INTO FilialEmpresa (idEmpresa, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'DataCompany-SP', TRUE, 1, current_timestamp);
INSERT INTO FilialEmpresa (idEmpresa, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'DataCompany-RJ', TRUE, 1, current_timestamp);


-- EmpresaCliente
-- Clientes dos nossos clientes: (Covabra, ... )
DROP TABLE IF EXISTS EmpresaCliente CASCADE;
CREATE TABLE EmpresaCliente (
	idEmpresaCliente SERIAL NOT NULL PRIMARY KEY,
        idEmpresa BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
	razaoSocial CHARACTER VARYING (150) NOT NULL ,
	tipoPessoa CHARACTER (1) NOT NULL, -- Fisica / Juridica
	cnpj CHARACTER (18),
	cpf CHARACTER (14),
	ativa BOOLEAN NOT NULL,
	idEndereco BIGINT,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	FOREIGN KEY (idEndereco) REFERENCES Endereco(idEndereco),	
	UNIQUE (nome, idEmpresa),
	UNIQUE (razaosocial, idEmpresa),
	UNIQUE (cnpj, idEmpresa),
	UNIQUE (cpf, idEmpresa)
);

-- Insert mock data
INSERT INTO EmpresaCliente (idEmpresa, tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'J', 'Cliente 1 - DataCompany', 'Cliente 1 LTDA','12.345.678/0001-00', TRUE, 1, current_timestamp);
INSERT INTO EmpresaCliente (idEmpresa, tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'J', 'Cliente 2 - DataCompany', 'Cliente 2 LTDA','12.345.678/0001-01', TRUE, 1, current_timestamp);

-- FilialEmpresa
DROP TABLE IF EXISTS FilialCliente CASCADE;
CREATE TABLE FilialCliente (
	idFilialCliente SERIAL NOT NULL PRIMARY KEY,
        idEmpresaCliente BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
	cnpj CHARACTER (18),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente(idEmpresaCliente),	
	UNIQUE (idEmpresaCliente,nome),
	UNIQUE (idEmpresaCliente,cnpj)
);

-- Insert mock data
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'Filial (A) do Cliente 1', TRUE, 1, current_timestamp);
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'Filial (B) do Cliente 1', TRUE, 1, current_timestamp);
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (2, 'Filial do Cliente 2', TRUE, 1, current_timestamp);


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
	ativo BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idUsuarioInclusao) REFERENCES usuario(idUsuario),	
	FOREIGN KEY (idEmpresa) REFERENCES empresa(idEmpresa)
);

-- Insert mock data 
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (2, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (3, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);

-- Departamento
DROP TABLE IF EXISTS Departamento CASCADE;
CREATE TABLE Departamento (
	idDepartamento SERIAL NOT NULL PRIMARY KEY,
	idEmpresa BIGINT NOT NULL, 
	Departamento CHARACTER VARYING (50) NOT NULL,
	Ativo BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	unique (idEmpresa,Departamento)
) ;

-- Insert mock data 
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Financeiro',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Administrativo',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Contábil',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Fiscal',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Controlatoria',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Recursos Humanos',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Jurídico',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Marketing',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Comercial',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Compras',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Vendas',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Operacional',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Almoxarifado',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Estoque',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Qualidade',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'P&D',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Produção',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'Manutenção',true, 1, current_timestamp);
INSERT INTO Departamento (idEmpresa,Departamento,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'PCP',true, 1, current_timestamp);

-- Centro de Custo
DROP TABLE IF EXISTS CentroCusto CASCADE;
CREATE TABLE CentroCusto (
	idCentroCusto SERIAL NOT NULL PRIMARY KEY,
	idEmpresa BIGINT NOT NULL, 
	CentroCusto CHARACTER VARYING (50) NOT NULL,
	Ativo BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	unique (idEmpresa,CentroCusto)
) ;

-- Insert mock data 
INSERT INTO CentroCusto (idEmpresa,CentroCusto,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'CC1',true, 1, current_timestamp);
INSERT INTO CentroCusto (idEmpresa,CentroCusto,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'CC2',true, 1, current_timestamp);

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
	idUsuarioInclusao INTEGER,
    	dataHoraInclusao TIMESTAMP NOT NULL,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente (idEmpresaCliente),	
    FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento),	
    FOREIGN KEY (idCentroCusto) REFERENCES CentroCusto(idCentroCusto),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario)	
);

-- Status Tarefa
DROP TABLE IF EXISTS StatusTarefa CASCADE;
CREATE TABLE StatusTarefa (
    StatusTarefa CHARACTER VARYING (50) NOT NULL PRIMARY KEY
);

INSERT INTO StatusTarefa VALUES ('Não Aceita');
INSERT INTO StatusTarefa VALUES ('Não Iniciada');
INSERT INTO StatusTarefa VALUES ('Em Andamento');
INSERT INTO StatusTarefa VALUES ('Adiada');
INSERT INTO StatusTarefa VALUES ('Bloqueada');
INSERT INTO StatusTarefa VALUES ('Concluída');
INSERT INTO StatusTarefa VALUES ('Cancelada');

-- Tarefa 
DROP TABLE IF EXISTS Tarefa CASCADE;
CREATE TABLE Tarefa (
    idTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefaPai BIGINT, 
    nivel INTEGER NOT NULL,
    idEmpresa BIGINT NOT NULL, 
    titulo CHARACTER VARYING (50) NOT NULL,
    nome CHARACTER VARYING (150) NOT NULL,
    prioridade CHARACTER VARYING (30) NOT NULL,
    tipo  CHARACTER VARYING (30) NOT NULL,
    idTarefaAnterior BIGINT NOT NULL, 
    idTarefaProxima BIGINT NOT NULL, 
    status CHARACTER VARYING (50) NOT NULL,
    andamento INTEGER NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim DATE NOT NULL, 	-- data esperada para o fim da tarefa, pode ser diferente da data real do termino da mesma
    dataTermino DATE, 		-- data real do termino da tarefa, 
    descricao TEXT NOT NULL,
    idEmpresaCliente BIGINT, 
    idUsuarioSolicitante BIGINT NOT NULL,
    idUsuarioResponsavel BIGINT NOT NULL,
    apontamentoHoras BOOLEAN NOT NULL,
    orcamentoControlado BOOLEAN NOT NULL,
    idDepartamento BIGINT,
    idCentroCusto BIGINT,
    idUsuarioInclusao INTEGER,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idTarefaPai) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idTarefaAnterior) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idTarefaProxima) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (status) REFERENCES statusTarefa (statusTarefa),	
    FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente (idEmpresaCliente),	
    FOREIGN KEY (idUsuarioSolicitante) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento),	
    FOREIGN KEY (idCentroCusto) REFERENCES CentroCusto(idCentroCusto)	
);


-- Participante tarefa
DROP TABLE IF EXISTS ParicipanteTarefa CASCADE;
CREATE TABLE ParicipanteTarefa (
    idParicipanteTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    idUsuarioParticipante INTEGER NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioParticipante) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Anexos Tarefa
DROP TABLE IF EXISTS AnexoTarefa CASCADE;
CREATE TABLE AnexoTarefa (
    idAnexoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    arquivo BYTEA NOT NULL,
    nome  CHARACTER VARYING (255) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);


-- Apontamento Tarefa
DROP TABLE IF EXISTS ApontamentoTarefa CASCADE;
CREATE TABLE ApontamentoTarefa (
    idApontamentoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    horas TIME NOT NULL,
    sentido CHAR(1) NOT NULL,
    observacoes  CHARACTER VARYING (60) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Orcamento Tarefa
DROP TABLE IF EXISTS OrcamentoTarefa CASCADE;
CREATE TABLE OrcamentoTarefa (
    idOrcamentoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    valor NUMERIC(10,2) NOT NULL,
    sentido CHAR(1) NOT NULL,
    observacoes  CHARACTER VARYING (60) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Avaliacao Meta Tarefa
DROP TABLE IF EXISTS AvaliacaoMetaTarefa CASCADE;
CREATE TABLE AvaliacaoMetaTarefa (
    idAvaliacaoMetaTarefa SERIAL NOT NULL PRIMARY KEY, 
    idMeta BIGINT NOT NULL, 
    idTarefa BIGINT NOT NULL, 
    idUsuarioAvaliado INTEGER NOT NULL,
    idUsuarioAvaliador INTEGER NOT NULL,
    avaliacao INTEGER NOT NULL,
    comentario  CHARACTER VARYING (150) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idMeta) REFERENCES Meta(idMeta),
    FOREIGN KEY (idUsuarioAvaliado) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioAvaliador) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Favoritos Tarefa Meta
DROP TABLE IF EXISTS FavoritosTarefaMeta CASCADE;
CREATE TABLE FavoritosTarefaMeta  (
    idFavoritosTarefaMeta  SERIAL NOT NULL PRIMARY KEY, 
    idMeta BIGINT NOT NULL, 
    idTarefa BIGINT NOT NULL, 
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idMeta) REFERENCES Meta(idMeta),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- 100 chars
-- 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
--          1         1         1         1         1         1         1         1         1         1

