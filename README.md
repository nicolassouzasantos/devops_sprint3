devops_sprint3

API REST de gestão de Pátios, Automóveis e Operadores (Java 17 + Spring Boot + JPA + PostgreSQL). Contém endpoints públicos para CRUD, paginação e filtros.

Integrantes: Nicolas Souza dos Santos (RM 555571), Oscar Arias Neto (RM 556936), Julia Martins Rebelles (RM 554516).
Endpoints e exemplos foram inspirados nos rascunhos já presentes no repositório. 
GitHub

🧱 Tech Stack

Java 17, Spring Boot (Web, Validation, Data JPA)

PostgreSQL 16 (local/Docker/Azure Database for PostgreSQL Flexible Server)

Build: Maven

Deploy: Azure App Service (Linux/Java 17)

📦 Requisitos

Java 17

Maven 3.9+

PostgreSQL 13+ (ou Docker)

# ☁️ Deploy no Azure (CLI)
# No terminal Azure

Passo 1:

export PREFIX="note$RANDOM"            
export LOC="brazilsouth"              
export RG="${PREFIX}-rg"
export PGSERVER="${PREFIX}-pg"         
export PGADMIN="pgadmin"
export PGPASS="$(openssl rand -base64 18)Aa1!"
export DBNAME="note"

export PLAN="${PREFIX}-plan"
export APP="${PREFIX}-app"            


Passo 2:

az group create -n $RG -l $LOC


Passo 3:

az postgres flexible-server create \
  -g $RG -n $PGSERVER -l $LOC \
  --admin-user $PGADMIN --admin-password "$PGPASS" \
  --tier Burstable --sku-name Standard_B1ms --version 16 --yes
  
az postgres flexible-server db create -g $RG -s $PGSERVER -d $DBNAME


Passo 4:

az postgres flexible-server firewall-rule create \
  -g $RG -n $PGSERVER \
  -r AllowAllAzureIPs --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0


Passo 5:

az appservice plan create -g $RG -n $PLAN --is-linux --sku B1

az webapp create -g $RG -p $PLAN -n $APP --runtime "JAVA|17-java17"


Passo 6:

export JDBC_URL="jdbc:postgresql://$PGSERVER.postgres.database.azure.com:5432/$DBNAME?sslmode=require"

az webapp config appsettings set -g $RG -n $APP --settings \
  SPRING_DATASOURCE_URL="$JDBC_URL" \
  SPRING_DATASOURCE_USERNAME="$PGADMIN" \
  SPRING_DATASOURCE_PASSWORD="$PGPASS" \
  JAVA_OPTS="-Xms256m -Xmx512m" \
  SCM_DO_BUILD_DURING_DEPLOYMENT=false


Passo 7:

git clone https://github.com/nicolassouzasantos/devops_sprint3
cd devops_sprint3

chmod +x mvnw || true
./mvnw -ntp -DskipTests clean package

ls -lh target/*.jar


Passo 8:

JAR_PATH=$(ls target/*.jar | head -n1)

az webapp deploy -g $RG -n $APP --src-path "$JAR_PATH" --type jar


Passo 9:

https://$PGSERVER-app.azurewebsites.net/
anotar URL no retorno para os testes


📚 Convenções da API

Base URL (local): http://localhost:8080

Base URL (Azure): https://<seu-app>.azurewebsites.net

Content-Type: application/json

Paginação padrão: page=0&size=10

Ordenação: sort=campo,asc|desc

🧩 Entidades
Pátio
{
  "id": 1,
  "nome": "Pátio Central",
  "endereco": "Av. Exemplo, 123 - São Paulo/SP"
}

Operador
{
  "id": 1,
  "nome": "João Silva",
  "login": "joao",
  "senha": "hash",
  "papel": "ROLE_USER" // ou ROLE_ADMIN
}

Automóvel
{
  "id": 1,
  "placa": "ABC1D23",
  "chassi": "9BWZZZ377VT004251",
  "tipo": "Moto",
  "cor": "Preto",
  "localizacaoNoPatio": "Fileira A - Vaga 05",
  "comentarios": "Observações",
  "patio": { "id": 1 }   // ou patioId: 1, conforme seu DTO
}

🔗 Endpoints e Testes (HTTP)

A seguir estão testes de POST, GET, PUT e DELETE para cada entidade.
Alguns desses endpoints e exemplos já existiam no README do projeto (p.ex. listar automóveis com paginação e filtros por placa). 
GitHub

🧪 TESTES HTTP

POST {URL-AZURE}/operadores
1)
{
  "nome": "João Silva",
  "login": "joao",
  "senha": "SenhaForte@123",
  "papel": "ROLE_ADMIN"
}

2)
{
  "nome": "Maria Souza",
  "login": "maria",
  "senha": "Segredo!456"
}

POST {URL-AZURE}/patios
1)
{
  "nome": "Pátio Central"
}

2)
{
  "nome": "Pátio Zona Norte",
  "endereco": "Av. Exemplo, 123 - São Paulo/SP"
}


POST {URL-AZURE}/automoveis
1)
{
  "placa": "ABC1D23",
  "chassi": "9BWZZZ377VT004251",
  "tipo": "Moto",
  "cor": "Preto",
  "localizacaoNoPatio": "Fileira A - Vaga 05",
  "comentarios": "Chegou com arranhões no tanque",
  "patioId": 1
}


2)
{
  "placa": "FTL7Y54",
  "chassi": "ECECECV7VT004251",
  "tipo": "Moto",
  "cor": "Azul",
  "localizacaoNoPatio": "Fileira B - Vaga 0",
  "comentarios": "Chegou inteiro",
  "patioId": 2
}

GET:
{URL-AZURE}/operadores
{URL-AZURE}/patios
{URL-AZURE}/automoveis

✅ Casos de erro sugeridos (para validar a API)

400 Bad Request: corpo inválido / campos obrigatórios ausentes:

Operador sem login ou senha

Automóvel sem placa/chassi/tipo

Pátio com nome vazio

404 Not Found:

Atualizar/Deletar um ID inexistente

Automóvel com patioId que não existe

409 Conflict:

Cadastrar placa ou chassi já existentes (se tiver UNIQUE)



