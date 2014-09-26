-- cadastro usuario:

-- usuario
SELECT * FROM usuario WHERE nome = 'nome';

-- empresa principal
SELECT * FROM usuarioempresa WHERE idusuario = 13;
SELECT * FROM empresa WHERE idempresa = 7;

-- coligadas
SELECT * FROM empresa WHERE idempresaprincipal = 7;

-- filiais
SELECT * FROM filialempresa WHERE idempresa = 7;

-- demais usuarios
SELECT * FROM usuarioempresa WHERE idempresa = 7;
