-- Tarefa
SELECT * FROM tarefa
SELECT * FROM tarefa WHERE idtarefa = 23;

SELECT * FROM usuario;
SELECT * FROM anexotarefa;
SELECT * FROM usuarioempresa WHERE idusuario = 4;
SELECT * FROM usuarioempresa WHERE idempresa = 1;
SELECT * FROM apontamentotarefa WHERE idtarefa = 23;
SELECT * FROM apontamentotarefa WHERE idtarefa = 23;

SELECT * FROM andamentotarefa WHERE idtarefa = 23;
SELECT * FROM historicotarefa

SELECT * FROM bloqueiotarefa
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
