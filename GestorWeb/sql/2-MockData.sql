-- Insert mock data
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('rodrigo', 'moreira','rodrigo.ccn2005@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('fernando', 'stavale','fernando.saax@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('daniel', 'stavale', 'danielstavale@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('teste-user', '', 'teste-user@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);

-- Estado
 INSERT INTO Estado (nome, uf) VALUES ('São Paulo', 'SP');

-- Cidade
INSERT INTO Cidade (idEstado, nome) VALUES (1, 'Limeira');

-- Empresa
INSERT INTO Empresa (tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES ('J', 'DataCompany', 'DataCompany LTDA', '12.345.678/0001-00', TRUE, 1, current_timestamp);
INSERT INTO Empresa (tipopessoa, idEmpresaPrincipal, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES ('J', 1, 'Empresa da corporação DataCompany', 'Empresa LTDA', '12.345.678/0001-01', TRUE, 1, current_timestamp);

-- FilialEmpresa 
INSERT INTO FilialEmpresa (idEmpresa, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'DataCompany-SP', TRUE, 1, current_timestamp);
INSERT INTO FilialEmpresa (idEmpresa, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'DataCompany-RJ', TRUE, 1, current_timestamp);

-- EmpresaCliente 
INSERT INTO EmpresaCliente (idEmpresa, tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'J', 'Cliente 1 - DataCompany', 'Cliente 1 LTDA','12.345.678/0001-00', TRUE, 1, current_timestamp);
INSERT INTO EmpresaCliente (idEmpresa, tipopessoa, nome, razaoSocial, cnpj, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'J', 'Cliente 2 - DataCompany', 'Cliente 2 LTDA','12.345.678/0001-01', TRUE, 1, current_timestamp);

-- FilialCliente 
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'Filial (A) do Cliente 1', TRUE, 1, current_timestamp);
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 'Filial (B) do Cliente 1', TRUE, 1, current_timestamp);
INSERT INTO FilialCliente (idEmpresaCliente, nome, ativa, idUsuarioInclusao,  dataHoraInclusao) VALUES (2, 'Filial do Cliente 2', TRUE, 1, current_timestamp);

-- UsuarioEmpresa 
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (1, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (2, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (3, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);
INSERT INTO UsuarioEmpresa (idUsuario, idEmpresa, administrador, contratacao, ativo , idUsuarioInclusao,  dataHoraInclusao) VALUES (4, 1, TRUE, CURRENT_DATE, TRUE, 1, current_timestamp);

-- Departamento 
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

-- CentroCusto 
INSERT INTO CentroCusto (idEmpresa,CentroCusto,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'CC1',true, 1, current_timestamp);
INSERT INTO CentroCusto (idEmpresa,CentroCusto,Ativo, idUsuarioInclusao,  dataHoraInclusao) VALUES (1,'CC2',true, 1, current_timestamp);

-- Tarefa

-- Tarefa "Tarefa Exemplo 1"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 1', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 10, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (1, 2, 1, 'Tarefa', 'Tarefa Exemplo 2 (sub da 1)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (1, 2, 1, 'Tarefa', 'Tarefa Exemplo 3 (sub da 1)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');


-- Tarefa "Tarefa Exemplo 4"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 2, 'Tarefa', 'Tarefa Exemplo 4', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'BLOQUEADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (4, 2, 2, 'Tarefa', 'Tarefa Exemplo 5 (sub da 4)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (5, 3, 2, 'Tarefa', 'Tarefa Exemplo 6 (sub da 5)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 5',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

-- Tarefa "Tarefa Exemplo 7"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 2, 'Tarefa', 'Tarefa Exemplo 7', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'BLOQUEADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 3, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (7, 2, 2, 'Tarefa', 'Tarefa Exemplo 8 (sub da 7)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 3, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');


-- Tarefa "Tarefa Exemplo 9"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 9', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'BLOQUEADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 3, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (9, 2, 1, 'Tarefa', 'Tarefa Exemplo 10 (sub da 9)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 3, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');


-- Tarefa "Tarefa Exemplo 11"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 11', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 10, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (11, 2, 1, 'Tarefa', 'Tarefa Exemplo 12 (sub da 11)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (11, 2, 1, 'Tarefa', 'Tarefa Exemplo 13 (sub da 11)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');


-- Tarefa "Tarefa Exemplo 14"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 14', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'BLOQUEADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (14, 2, 1, 'Tarefa', 'Tarefa Exemplo 15 (sub da 14)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (15, 3, 1, 'Tarefa', 'Tarefa Exemplo 16 (sub da 15)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 5',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE');


-- Tarefa "Tarefa Exemplo 16"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 16', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 10, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (16, 2, 1, 'Tarefa', 'Tarefa Exemplo 17 (sub da 16)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (16, 2, 1, 'Tarefa', 'Tarefa Exemplo 18 (sub da 16)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

-- Tarefa "Tarefa Exemplo 19"
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 'Tarefa', 'Tarefa Exemplo 19', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 10, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (19, 2, 1, 'Tarefa', 'Tarefa Exemplo 20 (sub da 19)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (19, 2, 1, 'Tarefa', 'Tarefa Exemplo 21 (sub da 19)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

-- Tarefas para filiais
INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, idFilialEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (NULL, 1, 1, 1, 'Tarefa', 'Tarefa Exemplo 22', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 10, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, idFilialEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (22, 2, 1, 1, 'Tarefa', 'Tarefa Exemplo 23 (sub da 22)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

INSERT INTO Tarefa (idTarefaPai, nivel, idEmpresa, idFilialEmpresa, titulo, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao )
VALUES (22, 2, 1, 1, 'Tarefa', 'Tarefa Exemplo 24 (sub da 22)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE');

