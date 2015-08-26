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

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Norma', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 1, 'Norma',          1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (2, 3, 'Rotina',         1, CURRENT_TIMESTAMP);

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Norma c/ Tarefa', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 1, 'Norma c/ Tarefa',1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 2, 'Tarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 3, 'Rotina',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 3, 'SubTarefa',         1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (3, 4, 'SubRotina',         1, CURRENT_TIMESTAMP);

INSERT INTO HierarquiaProjeto (nome, idusuarioinclusao, datahorainclusao) VALUES ('Projeto', 1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 1, 'Projeto',          1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 2, 'Procedimento',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 3, 'Tarefa',   1, CURRENT_TIMESTAMP);
INSERT INTO HierarquiaProjetoDetalhe (idHierarquiaProjeto, nivel, categoria, idusuarioinclusao, datahorainclusao) VALUES (4, 4, 'SubTArefa',   1, CURRENT_TIMESTAMP);


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
