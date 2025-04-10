-- Configurar autenticação MD5
ALTER SYSTEM SET password_encryption = 'md5';

-- Permitir conexões com senha para o usuário postgres
ALTER USER postgres WITH PASSWORD 'postgres';

-- Configurar pg_hba.conf para permitir conexões com MD5
ALTER SYSTEM SET hba_file = '/var/lib/postgresql/data/pg_hba.conf';

-- Criar extensão uuid-ossp se não existir
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Configurar para suprimir avisos do datlastsysoid
ALTER SYSTEM SET log_min_messages = 'warning'; 