

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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	UNIQUE (login)
);


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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
        FOREIGN KEY (idEmpresaPrincipal) REFERENCES Empresa(idEmpresa),	
        FOREIGN KEY (idEndereco) REFERENCES Endereco(idEndereco),	
	UNIQUE (razaoSocial),
	UNIQUE (cnpj),
	UNIQUE (cpf)
);


-- FilialEmpresa
DROP TABLE IF EXISTS FilialEmpresa CASCADE;
CREATE TABLE FilialEmpresa (
	idFilialEmpresa SERIAL NOT NULL PRIMARY KEY,
        idEmpresa BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
        cnpj CHARACTER (18),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	UNIQUE (idEmpresa,nome)
);


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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	FOREIGN KEY (idEndereco) REFERENCES Endereco(idEndereco),	
	UNIQUE (nome, idEmpresa),
	UNIQUE (razaosocial, idEmpresa),
	UNIQUE (cnpj, idEmpresa),
	UNIQUE (cpf, idEmpresa)
);

-- FilialEmpresa
DROP TABLE IF EXISTS FilialCliente CASCADE;
CREATE TABLE FilialCliente (
	idFilialCliente SERIAL NOT NULL PRIMARY KEY,
        idEmpresaCliente BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
	cnpj CHARACTER (18),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente(idEmpresaCliente),	
	UNIQUE (idEmpresaCliente,nome),
	UNIQUE (idEmpresaCliente,cnpj)
);


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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idUsuarioInclusao) REFERENCES usuario(idUsuario),	
	FOREIGN KEY (idEmpresa) REFERENCES empresa(idEmpresa)
);

-- Departamento
DROP TABLE IF EXISTS Departamento CASCADE;
CREATE TABLE Departamento (
	idDepartamento SERIAL NOT NULL PRIMARY KEY,
	idEmpresa BIGINT NOT NULL, 
	Departamento CHARACTER VARYING (50) NOT NULL,
	Ativo BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	unique (idEmpresa,Departamento)
) ;

-- Centro de Custo
DROP TABLE IF EXISTS CentroCusto CASCADE;
CREATE TABLE CentroCusto (
	idCentroCusto SERIAL NOT NULL PRIMARY KEY,
	idEmpresa BIGINT NOT NULL, 
	CentroCusto CHARACTER VARYING (50) NOT NULL,
	Ativo BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	unique (idEmpresa,CentroCusto)
) ;

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
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

INSERT INTO statustarefa VALUES ('NAO_ACEITA');
INSERT INTO statustarefa VALUES ('NAO_INICIADA');
INSERT INTO statustarefa VALUES ('EM_ANDAMENTO');
INSERT INTO statustarefa VALUES ('ADIADA');
INSERT INTO statustarefa VALUES ('BLOQUEADA');
INSERT INTO statustarefa VALUES ('CONCLUIDA');
INSERT INTO statustarefa VALUES ('AVALIADA');
INSERT INTO statustarefa VALUES ('CANCELADA');

-- Projecao Tarefa
DROP TABLE IF EXISTS ProjecaoTarefa CASCADE;
CREATE TABLE ProjecaoTarefa (
    ProjecaoTarefa CHARACTER VARYING (50) NOT NULL PRIMARY KEY
);

INSERT INTO Projecaotarefa VALUES ('ASCENDENTE');
INSERT INTO Projecaotarefa VALUES ('NORMAL');
INSERT INTO Projecaotarefa VALUES ('DESCENDENTE');

-- Tipo Tarefa
DROP TABLE IF EXISTS TipoTarefa CASCADE;
CREATE TABLE TipoTarefa (
    TipoTarefa CHARACTER VARYING (20) NOT NULL PRIMARY KEY
);

INSERT INTO TipoTarefa VALUES ('UNICA');
INSERT INTO TipoTarefa VALUES ('RECORRENTE');

-- Prioridade Tarefa
DROP TABLE IF EXISTS PrioridadeTarefa CASCADE;
CREATE TABLE PrioridadeTarefa (
    PrioridadeTarefa CHARACTER VARYING (10) NOT NULL PRIMARY KEY
);

INSERT INTO PrioridadeTarefa VALUES ('BAIXA');
INSERT INTO PrioridadeTarefa VALUES ('NORMAL');
INSERT INTO PrioridadeTarefa VALUES ('ALTA');

-- Tarefa 
DROP TABLE IF EXISTS Tarefa CASCADE;
CREATE TABLE Tarefa (
    idTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefaPai BIGINT, 
    nivel INTEGER NOT NULL,
    idEmpresa BIGINT NOT NULL, 
    idFilialEmpresa BIGINT, 
    titulo CHARACTER VARYING (50) NOT NULL,
    nome CHARACTER VARYING (150) NOT NULL,
    prioridade CHARACTER VARYING (10) NOT NULL,
    tipo  CHARACTER VARYING (20) NOT NULL,
    idProximaTarefa BIGINT, 
    status CHARACTER VARYING (50) NOT NULL,
    projecao CHARACTER VARYING (50) NOT NULL,
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
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idFilialEmpresa) REFERENCES FilialEmpresa(idFilialEmpresa),	
    FOREIGN KEY (idTarefaPai) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idProximaTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (status) REFERENCES statusTarefa (statusTarefa),	
    FOREIGN KEY (projecao) REFERENCES projecaoTarefa (projecaoTarefa),	
    FOREIGN KEY (tipo) REFERENCES tipoTarefa (tipoTarefa),	
    FOREIGN KEY (Prioridade) REFERENCES PrioridadeTarefa (PrioridadeTarefa),	
    FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente (idEmpresaCliente),	
    FOREIGN KEY (idUsuarioSolicitante) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento),	
    FOREIGN KEY (idCentroCusto) REFERENCES CentroCusto(idCentroCusto)	
);


-- Participante tarefa
DROP TABLE IF EXISTS ParticipanteTarefa CASCADE;
CREATE TABLE ParticipanteTarefa (
    idParticipanteTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    idUsuarioParticipante INTEGER NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioParticipante) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);


-- Andamento tarefa
DROP TABLE IF EXISTS AndamentoTarefa CASCADE;
CREATE TABLE AndamentoTarefa (
    idAndamentoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    comentario CHARACTER VARYING (50),
    andamentoAtual INTEGER NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Bloqueio tarefa
DROP TABLE IF EXISTS BloqueioTarefa CASCADE;
CREATE TABLE BloqueioTarefa (
    idBloqueioTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    motivo CHARACTER VARYING (50),
    status CHARACTER VARYING (50) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    idUsuarioRemocao INTEGER,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dataHoraRemocao TIMESTAMP,
    FOREIGN KEY (status) REFERENCES statusTarefa (statusTarefa),	
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioRemocao) REFERENCES Usuario(idUsuario)
);

-- Anexos Tarefa
DROP TABLE IF EXISTS AnexoTarefa CASCADE;
CREATE TABLE AnexoTarefa (
    idAnexoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    arquivo BYTEA NOT NULL,
    nome  CHARACTER VARYING (255) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Avaliacao Meta Tarefa
DROP TABLE IF EXISTS AvaliacaoMetaTarefa CASCADE;
CREATE TABLE AvaliacaoMetaTarefa (
    idAvaliacaoMetaTarefa SERIAL NOT NULL PRIMARY KEY, 
    idMeta BIGINT, 
    idTarefa BIGINT, 
    idUsuarioAvaliado INTEGER NOT NULL,
    idUsuarioAvaliador INTEGER NOT NULL,
    avaliacao INTEGER NOT NULL,
    comentario  CHARACTER VARYING (150) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idMeta) REFERENCES Meta(idMeta),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);


-- Historico tarefa
DROP TABLE IF EXISTS HistoricoTarefa CASCADE;
CREATE TABLE HistoricoTarefa (
    idHistoricoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    evento CHARACTER VARYING (100),
    idUsuario BIGINT NOT NULL,
    dataHora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);


-- 100 chars
-- 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
--          1         1         1         1         1         1         1         1         1         1


