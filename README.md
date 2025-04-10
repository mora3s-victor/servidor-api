# Sistema de Gerenciamento de Servidores

## Dados do Candidato
- **Nome:** Victor Cesar Mercado Moraes
- **Email:** vcmboard5@gmail.com
- **Telefone:** (65) 99927-7967

Este é um sistema de gerenciamento de servidores desenvolvido com Spring Boot, que permite o cadastro e gerenciamento de servidores efetivos e temporários, incluindo suas lotações, endereços e fotos.

## 🚀 Tecnologias Utilizadas

- **Backend:**
  - Java 17
  - Spring Boot 3.x
  - Spring Security com JWT
  - Spring Data JPA
  - PostgreSQL
  - MinIO (armazenamento de arquivos)
  - Swagger/OpenAPI (documentação da API)
  - Docker e Docker Compose

## 📋 Pré-requisitos

Antes de começar, você precisará ter instalado:

- Java 17
- Maven
- Docker e Docker Compose
- Git

## 🚀 Execução da Aplicação

### 1. Clone o Repositório

```bash
git clone [https://github.com/mora3s-victor/servidor-api.git]
cd servidor-api
# Compile o projeto
mvn clean package
```

### 2. Execute com Docker Compose

O projeto utiliza Docker Compose para orquestrar todos os serviços necessários. Para executar:

1. Certifique-se que o Docker Desktop está em execução
   - Verifique se o ícone do Docker na bandeja do sistema está estável
   - Se necessário, inicie o Docker Desktop manualmente

2. Execute o comando:
```bash
docker-compose up --build
```

Este comando irá:
1. Construir a aplicação Spring Boot
2. Criar e configurar o banco de dados PostgreSQL
3. Configurar o MinIO para armazenamento de arquivos
4. Iniciar todos os serviços

Após a execução, a aplicação estará disponível em:
- API: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui/index.html`
- MinIO Console: `http://localhost:9001`

### 3. Primeiro Acesso

1. Acesse a documentação da API em `http://localhost:8080/api/swagger-ui/index.html`
2. Realize o login com as credenciais iniciais:
   - Email: admin@exemplo.com
   - Senha: password
3. Utilize o token retornado para acessar os endpoints protegidos

## 📝 Documentação da API

### Endpoints Principais

#### Autenticação
- `POST /auth/login` - Login de usuário
  - Credenciais iniciais do administrador:
    - Email: admin@exemplo.com
    - Senha: password
  - Este usuário possui perfil de administrador e pode criar outros usuários
- `POST /auth/refresh-token` - Renovação do token de acesso

#### Servidores
- `GET /servidores/efetivos` - Lista servidores efetivos
- `GET /servidores/temporarios` - Lista servidores temporários
- `POST /servidores/efetivos` - Cria um servidor efetivo
  - Observação: É necessário ter uma unidade cadastrada previamente, pois o ID da unidade é obrigatório para criar um servidor, tanto temporário quanto efetivo
- `POST /servidores/temporarios` 
- `PUT /servidores/efetivos/{id}` - Atualiza um servidor efetivo
- `PUT /servidores/temporarios/{id}` - Atualiza um servidor temporário
- `DELETE /servidores/efetivos/{id}` - Remove um servidor efetivo
- `DELETE /servidores/temporarios/{id}` - Remove um servidor temporário

#### Unidades
- `GET /unidades` - Lista todas as unidades
- `POST /unidades` - Cria uma nova unidade
- `PUT /unidades/{id}` - Atualiza uma unidade
- `DELETE /unidades/{id}` - Remove uma unidade

### Exemplos de Payloads

#### Login
```json
{
  "email": "admin@exemplo.com",
  "password": "password"
}
```

#### Cadastro de Unidade
```json
{
  "nome": "Secretaria de Educação",
  "sigla": "SEDUC",
  "endereco": {
    "tipoLogradouro": "RUA",
    "logradouro": "Das Siriemas",
    "numero": 1213,
    "bairro": "Centro",
    "cidade": {
      "nome": "Cuiabá",
      "uf": "MT"
    }
  }
}
```

#### Cadastro de Servidor Efetivo
```json
{
  "nome": "Joao",
  "dataNascimento": "2025-04-10",
  "sexo": "M",
  "mae": "Joana",
  "pai": "Marcelo",
  "matricula": "2235",
  "unidadeId": 1,
  "portariaLotacao": "PORT 15/2022",
  "enderecos": [
    {
      "tipoLogradouro": "Avenida",
      "logradouro": "Brasil",
      "numero": 33,
      "bairro": "Centro Sul",
      "cidade": {
        "nome": "Várzea Grande",
        "uf": "MT"
      }
    }
  ]
}
```

#### Cadastro de Servidor Temporário
```json
{
  "nome": "Mayara",
  "dataNascimento": "2025-04-10",
  "sexo": "F",
  "mae": "Cristina",
  "pai": "Guilherme",
  "dataAdmissao": "2025-04-10",
  "unidadeId": 1,
  "portariaLotacao": "PORT 07/2024",
  "enderecos": [
    {
      "tipoLogradouro": "rua",
      "logradouro": "Rua dos bem te canários",
      "numero": 12,
      "bairro": "Imperial",
      "cidade": {
        "nome": "Cuiabá",
        "uf": "MT"
      }
    }
  ]
}
```

#### Cadastro de Lotação
```json
{
  "pessoaId": 1,
  "unidadeId": 1,
  "dataLotacao": "2025-04-10",
  "portaria": "PORT 12/2025",
}
```

## 🔐 Segurança

O sistema utiliza autenticação JWT com as seguintes características:
- Token de acesso expira em 5 minutos
- Token de refresh expira em 1 hora
- Todas as requisições (exceto login) precisam incluir o token no header:
  ```
  Authorization: Bearer <token>
  ```

## 🔧 Configurações do Ambiente

### Banco de Dados
- Nome: `servidor_db`
- Usuário: `postgres`
- Senha: `postgres`
- Porta: `5432`

### MinIO
- Bucket: `servidores`
- Usuário: `minioadmin`
- Senha: `minioadmin`
- Porta API: `9000`
- Porta Console: `9001`

## 🛠️ Desenvolvimento

### Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/seletivo/servidor/
│   │       ├── config/         # Configurações do Spring
│   │       ├── controller/     # Controladores REST
│   │       ├── dto/           # Objetos de Transferência de Dados
│   │       ├── model/         # Entidades JPA
│   │       ├── repository/    # Repositórios JPA
│   │       ├── security/      # Configurações de Segurança
│   │       ├── service/       # Lógica de Negócio
│   │       └── util/          # Utilitários
│   └── resources/
│       ├── application.yml    # Configurações da Aplicação
│       └── init-scripts/      # Scripts SQL de inicialização
```

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Merge Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 