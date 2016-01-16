-- Insert mock data
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('rodrigo', 'moreira','rodrigo.ccn2005@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('fernando', 'stavale','fernando.saax@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('daniel', 'stavale', 'danielstavale@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);
INSERT INTO usuario ( nome, sobrenome, login, senha , idUsuarioInclusao,  dataHoraInclusao) VALUES ('teste-user', 'teste', 'teste-user@gmail.com', 'ICy5YqxZB1uWSwcVLSNLcA==', null, current_timestamp);

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


-- HierarquiaProjeto
INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Meta',1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 1, 'Meta',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 2, 'Tarefa',       1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 3, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 4, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 5, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 6, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 7, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 8, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 9, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (1, 10, 'SubTarefa',   1, CURRENT_TIMESTAMP);

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Norma', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 1, 'Norma',          1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 3, 'Rotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 4, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 5, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 6, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 7, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 8, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 9, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 10, 'SubRotina',        1, CURRENT_TIMESTAMP);

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Norma c/ Tarefa', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 1, 'Norma c/ Tarefa',1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 2, 'Tarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 3, 'Rotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 3, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 4, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 4, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 5, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 5, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 6, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 6, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 7, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 7, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 8, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 8, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 9, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 9, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 10, 'SubRotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 10, 'SubTarefa',         1, CURRENT_TIMESTAMP);

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Projeto', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 1, 'Projeto',          1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 3, 'Tarefa',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 4, 'SubTarefa',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 5, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 6, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 7, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 8, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 9, 'SubTarefa',    1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 10, 'SubTarefa',   1, CURRENT_TIMESTAMP);


INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento) VALUES (null, 0, '0%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (null, 25, '25%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (null, 50, '50%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (null, 75, '75%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (null, 100, '100%');

INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 0, '0%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 10, '10%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 20, '20%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 30, '30%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 40, '40%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 50, '50%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 60, '60%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 70, '70%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 80, '80%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 90, '90%');
INSERT INTO ParametroAndamentoTarefa (idempresa, percentualandamento, descricaoandamento)VALUES (1, 100, '100%');

-- Tarefa

-- Tarefa "Tarefa Exemplo 1"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 1', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (1, 3, 1,  'Tarefa Exemplo 2 (sub da 1)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (2, 4, 1,  'Tarefa Exemplo 3 (sub da 2)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);


-- Tarefa "Tarefa Exemplo 4"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 2,  'Tarefa Exemplo 4', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'NAO_ACEITA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (4, 3, 2,  'Tarefa Exemplo 5 (sub da 4)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (5, 4, 2,  'Tarefa Exemplo 6 (sub da 5)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 5',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

-- Tarefa "Tarefa Exemplo 7"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 2,  'Tarefa Exemplo 7', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'NAO_ACEITA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 3, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (7, 3, 2,  'Tarefa Exemplo 8 (sub da 7)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 3, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);


-- Tarefa "Tarefa Exemplo 9"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 9', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'NAO_ACEITA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 3, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (9, 3, 1,  'Tarefa Exemplo 10 (sub da 9)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 3, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);


-- Tarefa "Tarefa Exemplo 11"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 11', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (11, 3, 1,  'Tarefa Exemplo 12 (sub da 11)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (11, 3, 1,  'Tarefa Exemplo 13 (sub da 11)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);


-- Tarefa "Tarefa Exemplo 14"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 14', 'Tarefa de prioridade baixa, bloqueada',
    'BAIXA', 'UNICA', NULL, 'NAO_ACEITA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (14, 3, 1,  'Tarefa Exemplo 15 (sub da 14)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 4',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (15, 4, 1,  'Tarefa Exemplo 16 (sub da 15)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 5',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-06-01', '2014-06-30', NULL, 
    1, 1, 2, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'DESCENDENTE', false);


-- Tarefa "Tarefa Exemplo 16"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 17', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (17, 3, 1,  'Tarefa Exemplo 18 (sub da 17)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (18, 4, 1,  'Tarefa Exemplo 19 (sub da 18)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 3, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

-- Tarefa "Tarefa Exemplo 19"
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1,  'Tarefa Exemplo 20', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (20, 3, 1,  'Tarefa Exemplo 21 (sub da 20)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (20, 3, 1,  'Tarefa Exemplo 22 (sub da 20)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 2, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

-- Tarefas para filiais
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, idFilialEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1, 1,  'Tarefa Exemplo 23', 'Tarefa de prioridade alta, em andamento, com 10% concluido',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, idFilialEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (23, 3, 1, 1,  'Tarefa Exemplo 24 (sub da 23)', 'Tarefa de prioridade alta, nao iniciada, sub tarefa da 1',
    'ALTA', 'UNICA', NULL, 'NAO_INICIADA', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 1, 4, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

-- Tarefas onde o solicitante é o test-user 
INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, idFilialEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1, 1,  'Tarefa Exemplo 25', 'Tarefa 1 onde o solicitante eh o test',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 4, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, idFilialEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1, 1,  'Tarefa Exemplo 26', 'Tarefa 2 onde o solicitante eh o test',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 4, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);

INSERT INTO Tarefa (idTarefaPai, idHierarquiaProjetoDetalhe, idEmpresa, idFilialEmpresa, nome, descricao,
    prioridade, tipo, idProximaTarefa, status, andamento, dataInicio, dataFim, dataTermino,
    idEmpresaCliente, idUsuarioSolicitante, idUsuarioResponsavel, apontamentoHoras, orcamentoControlado, 
    idDepartamento, idCentroCusto, idUsuarioInclusao, dataHoraInclusao, projecao, template )
VALUES (NULL, 2, 1, 1,  'Tarefa Exemplo 27', 'Tarefa 3 onde o solicitante eh o test',
    'ALTA', 'UNICA', NULL, 'EM_ANDAMENTO', 0, '2014-05-01', '2014-05-31', NULL, 
    1, 4, 1, FALSE, FALSE, NULL, NULL, 1, '2014-05-01 10:12:34', 'ASCENDENTE', true);


-- PublicacaoReferencia
/** INSERT INTO PublicacaoReferencia ( publicacaoreferencia, idusuario ) VALUES ( 'SPED', 1 );
INSERT INTO PublicacaoReferencia ( publicacaoreferencia, idusuario ) VALUES ( 'NFE', 1 );

INSERT INTO PublicacaoTipo ( PublicacaoTipo, idusuario ) VALUES ( 'NORMA TECNICA', 1 );
INSERT INTO PublicacaoTipo ( PublicacaoTipo, idusuario ) VALUES ( 'LEI', 1 );

INSERT INTO Publicacao ( nome, idempresa, idpublicacaoreferencia, idpublicacaotipo, datareuniao, deadlineleitura, descricao, 
    datapublicacao, idpublicacaostatus, idusuario ) VALUES ( 'Publicacao Teste 02', 1, 1, 1, now(), '2015-10-21', 'Descricao',
    '2015-10-21', 1, 1);

INSERT INTO Publicacao ( nome, idempresa, idpublicacaoreferencia, idpublicacaotipo, datareuniao, deadlineleitura, descricao, 
    datapublicacao, idpublicacaostatus, idusuario  ) VALUES ( 'Publicacao Teste 02', 1, 2, 2, now(), '2015-10-21', 'Descricao',
    '2015-10-21', 1, 1);


*/