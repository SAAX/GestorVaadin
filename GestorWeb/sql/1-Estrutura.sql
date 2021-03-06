DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS ParametroAndamentoTarefa CASCADE;
DROP TABLE IF EXISTS Estado CASCADE;
DROP TABLE IF EXISTS Cidade CASCADE;
DROP TABLE IF EXISTS Endereco CASCADE;
DROP TABLE IF EXISTS Empresa CASCADE;
DROP TABLE IF EXISTS FilialEmpresa CASCADE;
DROP TABLE IF EXISTS EmpresaCliente CASCADE;
DROP TABLE IF EXISTS FilialCliente CASCADE;
DROP TABLE IF EXISTS UsuarioEmpresa CASCADE;
DROP TABLE IF EXISTS Departamento CASCADE;
DROP TABLE IF EXISTS CentroCusto CASCADE;
DROP TABLE IF EXISTS HierarquiaProjeto CASCADE;
DROP TABLE IF EXISTS HierarquiaProjetoDetalhe CASCADE;
DROP TABLE IF EXISTS StatusMeta CASCADE;
DROP TABLE IF EXISTS PrioridadeMeta CASCADE;
DROP TABLE IF EXISTS meta CASCADE;
DROP TABLE IF EXISTS StatusTarefa CASCADE;
DROP TABLE IF EXISTS ProjecaoTarefa CASCADE;
DROP TABLE IF EXISTS TipoTarefa CASCADE;
DROP TABLE IF EXISTS PrioridadeTarefa CASCADE;
DROP TABLE IF EXISTS Tarefa CASCADE;
DROP TABLE IF EXISTS Participante CASCADE;
DROP TABLE IF EXISTS AndamentoTarefa CASCADE;
DROP TABLE IF EXISTS BloqueioTarefa CASCADE;
DROP TABLE IF EXISTS Anexo CASCADE;
DROP TABLE IF EXISTS ApontamentoTarefa CASCADE;
DROP TABLE IF EXISTS OrcamentoTarefa CASCADE;
DROP TABLE IF EXISTS AvaliacaoMetaTarefa CASCADE;
DROP TABLE IF EXISTS FavoritosTarefaMeta CASCADE;
DROP TABLE IF EXISTS HistoricoTarefa CASCADE;
DROP TABLE IF EXISTS ChatTarefa CASCADE;
DROP SEQUENCE RecurrencySequence;


-- Usuário
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
CREATE TABLE Estado (
	idEstado SERIAL NOT NULL PRIMARY KEY,
        nome CHARACTER VARYING (100) NOT NULL,
        uf CHARACTER (2) NOT NULL,
        UNIQUE (uf),
        UNIQUE (nome)
);

-- Cidade
CREATE TABLE Cidade (
	idCidade SERIAL NOT NULL PRIMARY KEY,
        idEstado BIGINT NOT NULL,
        nome CHARACTER VARYING (100) NOT NULL,
        FOREIGN KEY (idEstado) REFERENCES Estado(idEstado),	
        UNIQUE (nome,idEstado)
);


-- Empresa
-- Empresa que adquiriu o software: cliente da Saax
CREATE TABLE Empresa (
	idEmpresa SERIAL NOT NULL PRIMARY KEY,
        idEmpresaPrincipal BIGINT,
	nome CHARACTER VARYING (100) NOT NULL ,
	razaoSocial CHARACTER VARYING (150) NOT NULL ,
	tipoPessoa CHARACTER (1) NOT NULL, -- Fisica / Juridica
	cnpj CHARACTER (18),
	cpf CHARACTER (14),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
        FOREIGN KEY (idEmpresaPrincipal) REFERENCES Empresa(idEmpresa),	
	UNIQUE (razaoSocial),
	UNIQUE (cnpj),
	UNIQUE (cpf)
);


-- FilialEmpresa
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
CREATE TABLE EmpresaCliente (
	idEmpresaCliente SERIAL NOT NULL PRIMARY KEY,
        idEmpresa BIGINT NOT NULL,
	nome CHARACTER VARYING (100) NOT NULL ,
	razaoSocial CHARACTER VARYING (150) NOT NULL ,
	tipoPessoa CHARACTER (1) NOT NULL, -- Fisica / Juridica
	cnpj CHARACTER (18),
	cpf CHARACTER (14),
	ativa BOOLEAN NOT NULL,
	idUsuarioInclusao INTEGER NOT NULL,
    	dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    	FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	UNIQUE (nome, idEmpresa),
	UNIQUE (razaosocial, idEmpresa),
	UNIQUE (cnpj, idEmpresa),
	UNIQUE (cpf, idEmpresa)
);

-- FilialEmpresa
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


-- HierarquiaProjeto 
CREATE TABLE HierarquiaProjeto (
    idHierarquiaProjeto SERIAL NOT NULL PRIMARY KEY, 
    nome CHARACTER VARYING (50) NOT NULL,
    idEmpresa BIGINT, 
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    idUsuarioInclusao INTEGER NOT NULL,
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nome)
);

CREATE TABLE HierarquiaProjetoDetalhe (
    idHierarquiaProjetoDetalhe SERIAL NOT NULL PRIMARY KEY, 
    idHierarquiaProjeto BIGINT NOT NULL, 
    FOREIGN KEY (idHierarquiaProjeto) REFERENCES HierarquiaProjeto(idHierarquiaProjeto),	
    nivel INTEGER NOT NULL,
    categoria CHARACTER VARYING (20) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Status Meta
CREATE TABLE StatusMeta (
    StatusMeta CHARACTER VARYING (50) NOT NULL PRIMARY KEY
);

INSERT INTO statusMeta VALUES ('NAO_INICIADA');
INSERT INTO statusMeta VALUES ('EM_ANDAMENTO');
INSERT INTO statusMeta VALUES ('CONCLUIDA');
INSERT INTO statusMeta VALUES ('CANCELADA');

-- Prioridade Meta
CREATE TABLE PrioridadeMeta (
    PrioridadeMeta CHARACTER VARYING (10) NOT NULL PRIMARY KEY
);

INSERT INTO PrioridadeMeta VALUES ('BAIXA');
INSERT INTO PrioridadeMeta VALUES ('NORMAL');
INSERT INTO PrioridadeMeta VALUES ('ALTA');

-- Meta 
CREATE TABLE Meta (
    idMeta SERIAL NOT NULL PRIMARY KEY, 
    idEmpresa BIGINT NOT NULL, 
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    idFilialEmpresa BIGINT, 
    nome CHARACTER VARYING (150) NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim DATE, 	-- data esperada para o fim da tarefa, pode ser diferente da data real do termino da mesma
    dataTermino DATE, 		-- data real do termino da tarefa, 
    descricao TEXT,
    prioridade CHARACTER VARYING (10) NOT NULL,
    status CHARACTER VARYING (50) NOT NULL,
    idEmpresaCliente BIGINT, 
    template BOOLEAN NOT NULL,
    idDepartamento BIGINT,
    idCentroCusto BIGINT,
    idUsuarioInclusao INTEGER NOT NULL,
    idUsuarioSolicitante INTEGER NOT NULL,
    idUsuarioResponsavel INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idUsuarioRemocao INTEGER ,
    dataHoraRemocao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    idHierarquiaProjetoDetalhe INTEGER NOT NULL,
    FOREIGN KEY (idHierarquiaProjetoDetalhe) REFERENCES HierarquiaProjetoDetalhe(idHierarquiaProjetoDetalhe),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioRemocao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idFilialEmpresa) REFERENCES FilialEmpresa(idFilialEmpresa),	
    FOREIGN KEY (status) REFERENCES statusMeta (statusMeta),	
    FOREIGN KEY (Prioridade) REFERENCES PrioridadeMeta (PrioridadeMeta),	
    FOREIGN KEY (idEmpresaCliente) REFERENCES EmpresaCliente (idEmpresaCliente),	
    FOREIGN KEY (idUsuarioSolicitante) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idUsuarioResponsavel) REFERENCES Usuario(idUsuario),	
    FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento),	
    FOREIGN KEY (idCentroCusto) REFERENCES CentroCusto(idCentroCusto)	
);


-- Status Tarefa
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
INSERT INTO statustarefa VALUES ('RECUSADA');

-- Andamento
CREATE TABLE ParametroAndamentoTarefa (
    idParametroAndamentoTarefa SERIAL NOT NULL PRIMARY KEY,
    idEmpresa INTEGER,
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    percentualAndamento INTEGER NOT NULL,
    descricaoAndamento VARCHAR(10) NOT NULL
);


-- Projecao Tarefa
CREATE TABLE ProjecaoTarefa (
    ProjecaoTarefa CHARACTER VARYING (50) NOT NULL PRIMARY KEY
);

INSERT INTO Projecaotarefa VALUES ('ASCENDENTE');
INSERT INTO Projecaotarefa VALUES ('NORMAL');
INSERT INTO Projecaotarefa VALUES ('DESCENDENTE');

-- Tipo Tarefa
CREATE TABLE TipoTarefa (
    TipoTarefa CHARACTER VARYING (20) NOT NULL PRIMARY KEY
);

INSERT INTO TipoTarefa VALUES ('UNICA');
INSERT INTO TipoTarefa VALUES ('RECORRENTE');

-- Prioridade Tarefa
CREATE TABLE PrioridadeTarefa (
    PrioridadeTarefa CHARACTER VARYING (10) NOT NULL PRIMARY KEY
);

INSERT INTO PrioridadeTarefa VALUES ('BAIXA');
INSERT INTO PrioridadeTarefa VALUES ('NORMAL');
INSERT INTO PrioridadeTarefa VALUES ('ALTA');

-- Tarefa 
CREATE TABLE Tarefa (
    idTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefaPai BIGINT, 
    idMeta BIGINT,
    idEmpresa BIGINT NOT NULL, 
    idFilialEmpresa BIGINT, 
    nome CHARACTER VARYING (150) NOT NULL,
    prioridade CHARACTER VARYING (10) NOT NULL,
    tipo  CHARACTER VARYING (20) NOT NULL,
    idProximaTarefa BIGINT, 
    recurrencyID BIGINT, 
    status CHARACTER VARYING (50) NOT NULL,
    mensagemRecorrencia CHARACTER VARYING (500),
    projecao CHARACTER VARYING (50),
    andamento INTEGER NOT NULL,
    dataInicio DATE NOT NULL,
    dataFim DATE, 	-- data esperada para o fim da tarefa, pode ser diferente da data real do termino da mesma
    dataTermino DATE, 		-- data real do termino da tarefa, 
    descricao TEXT,
    idEmpresaCliente BIGINT, 
    idUsuarioSolicitante BIGINT NOT NULL,
    idUsuarioResponsavel BIGINT NOT NULL,
    apontamentoHoras BOOLEAN NOT NULL,
    custoHoraApontamento NUMERIC(10,2),
    orcamentoControlado BOOLEAN NOT NULL,
    template BOOLEAN NOT NULL DEFAULT FALSE,
    idDepartamento BIGINT,
    idCentroCusto BIGINT,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    idHierarquiaProjetoDetalhe INTEGER NOT NULL,
    idUsuarioRemocao INTEGER,
    dataHoraRemocao TIMESTAMP,
    FOREIGN KEY (idHierarquiaProjetoDetalhe) REFERENCES HierarquiaProjetoDetalhe(idHierarquiaProjetoDetalhe),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioRemocao) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
    FOREIGN KEY (idFilialEmpresa) REFERENCES FilialEmpresa(idFilialEmpresa),	
    FOREIGN KEY (idTarefaPai) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idProximaTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idMeta) REFERENCES Meta(idMeta),
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

CREATE SEQUENCE RecurrencySequence;



-- Andamento tarefa
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


-- Apontamento Tarefa
CREATE TABLE ApontamentoTarefa (
    idApontamentoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    creditoHoras INTEGER,
    debitoHoras INTEGER,
    saldoHoras INTEGER NOT NULL,
    custoHora NUMERIC(10,2),
    creditoValor NUMERIC(10,2),
    debitoValor NUMERIC(10,2),
    saldoValor NUMERIC(10,2),
    observacoes  CHARACTER VARYING (60),
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Orcamento Tarefa
CREATE TABLE OrcamentoTarefa (
    idOrcamentoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    credito NUMERIC(10,2),
    debito NUMERIC(10,2),
    saldo NUMERIC(10,2) NOT NULL,
    observacoes  CHARACTER VARYING (60),
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);

-- Avaliacao Meta Tarefa
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
CREATE TABLE HistoricoTarefa (
    idHistoricoTarefa SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT NOT NULL, 
    evento CHARACTER VARYING (200) NOT NULL,
    comentario CHARACTER VARYING (100),
    idUsuario BIGINT NOT NULL,
    dataHora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- ChatTarefa
CREATE TABLE ChatTarefa (
    idChat SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT, 
    mensagem CHARACTER VARYING (200) NOT NULL,
    idUsuario INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa)
       
);



DROP TABLE IF EXISTS PublicacaoParticipante CASCADE;
DROP TABLE IF EXISTS PublicacaoReferencia CASCADE;
DROP TABLE IF EXISTS PublicacaoTipo CASCADE;
DROP TABLE IF EXISTS PublicacaoStatus CASCADE;
DROP TABLE IF EXISTS Publicacao CASCADE;
DROP TABLE IF EXISTS PublicacaoAnexo CASCADE;


CREATE TABLE PublicacaoTipo (
	idPublicacaoTipo SERIAL NOT NULL PRIMARY KEY,
	PublicacaoTipo VARCHAR(150) NOT NULL, 
	DataHoraInclusao  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	idUsuario INTEGER NOT NULL, 
	FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE PublicacaoStatus (
	idPublicacaoStatus SERIAL NOT NULL PRIMARY KEY,
	PublicacaoStatus VARCHAR(150) NOT NULL
);

INSERT INTO PublicacaoStatus (PublicacaoStatus) VALUES ('PENDENTE');
INSERT INTO PublicacaoStatus (PublicacaoStatus) VALUES ('EM ANDAMENTO');
INSERT INTO PublicacaoStatus (PublicacaoStatus) VALUES ('FECHADO');

CREATE TABLE publicacaoreferencia (
	idPublicacaoReferencia SERIAL NOT NULL PRIMARY KEY,
	PublicacaoReferencia VARCHAR(150) NOT NULL
);



CREATE TABLE Publicacao (
	idPublicacao SERIAL NOT NULL PRIMARY KEY,
        idEmpresa BIGINT NOT NULL, 
        FOREIGN KEY (idEmpresa) REFERENCES Empresa(idEmpresa),	
	nome VARCHAR(150) NOT NULL, 
	idPublicacaoReferencia INTEGER,
	FOREIGN KEY (idPublicacaoReferencia) REFERENCES PublicacaoReferencia (idPublicacaoReferencia),
	idPublicacaoTipo INTEGER NOT NULL,
	FOREIGN KEY (idPublicacaoTipo) REFERENCES PublicacaoTipo (idPublicacaoTipo),
	DataReuniao DATE,
	DeadLineLeitura DATE,
	Descricao TEXT NOT NULL, 
	DataPublicacao DATE,
	idPublicacaoStatus INTEGER NOT NULL,
	FOREIGN KEY (idPublicacaoStatus) REFERENCES PublicacaoStatus (idPublicacaoStatus),
	DataHoraInclusao  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	idUsuario INTEGER NOT NULL, 
	FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

CREATE TABLE PublicacaoParticipante (
	idPublicacaoParticipante SERIAL NOT NULL PRIMARY KEY,
	idPublicacao INTEGER NOT NULL, 
	FOREIGN KEY (idPublicacao) REFERENCES Publicacao (idPublicacao),
	idParticipante INTEGER NOT NULL, 
	FOREIGN KEY (idParticipante) REFERENCES Usuario(idUsuario),
	DataLeitura DATE NOT NULL,
	DataHoraInclusao  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	idUsuario INTEGER NOT NULL, 
	FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);


-- Participante meta / tarefa
CREATE TABLE Participante (
    idParticipante SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT, 
    idMeta BIGINT, 
    idPublicacao BIGINT, 
    idUsuarioParticipante INTEGER NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idMeta) REFERENCES Meta(idMeta),
    FOREIGN KEY (idPublicacao) REFERENCES Publicacao(idPublicacao),
    FOREIGN KEY (idUsuarioParticipante) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);


-- Anexos 
CREATE TABLE Anexo (
    idAnexo SERIAL NOT NULL PRIMARY KEY, 
    idTarefa BIGINT , 
    idPublicacao BIGINT , 
    nome  CHARACTER VARYING (255) NOT NULL,
    caminhocompleto CHARACTER VARYING (255) NOT NULL,
    idUsuarioInclusao INTEGER NOT NULL,
    dataHoraInclusao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTarefa) REFERENCES Tarefa(idTarefa),
    FOREIGN KEY (idPublicacao) REFERENCES Publicacao(idPublicacao),
    FOREIGN KEY (idUsuarioInclusao) REFERENCES Usuario(idUsuario)
);



-- 100 chars
-- 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
--          1         1         1         1         1         1         1         1         1         1

