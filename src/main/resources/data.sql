-- Inserir usuário ADMIN se não existir
INSERT INTO usuario (email, senha, role, ativo)
SELECT 'admin@exemplo.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN', true
WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@exemplo.com'); 