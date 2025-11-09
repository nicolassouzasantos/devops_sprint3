# devops_sprint4

# LINK VÍDEO DO PROJETO DEVOPS: https://youtu.be/YGUXJc8oY7I?si=PcTfL5iYmBmq6pm8

# LINK VÍDEO DO PROJETO JAVA: https://youtu.be/ISrdKnZzQEE?si=CwyzRKHcDqVVNHWh

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
No terminal Azure

# Passo 1:

export PREFIX="notesolutions"    

export LOC="brazilsouth"          

export RG="${PREFIX}-rg"

export PGSERVER="${PREFIX}-pg"         

export PGADMIN="pgadmin"

export PGPASS="Teste123@"

export DBNAME="note"

export PLAN="${PREFIX}-plan"

export APP="${PREFIX}-app"            


# Passo 2:

az group create -n $RG -l $LOC


# Passo 3:

az postgres flexible-server create -g $RG -n $PGSERVER -l $LOC --admin-user $PGADMIN --admin-password "$PGPASS" --tier Burstable --sku-name Standard_B1ms --version 16 --yes
  
az postgres flexible-server db create -g $RG -s $PGSERVER -d $DBNAME


# Passo 4:

az postgres flexible-server firewall-rule create -g $RG -n $PGSERVER -r AllowAllAzureIPs --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0


# Passo 5:

az appservice plan create -g $RG -n $PLAN --is-linux --sku B1

az webapp create -g $RG -p $PLAN -n $APP --runtime "JAVA|17-java17"


# Passo 6:

export JDBC_URL="jdbc:postgresql://$PGSERVER.postgres.database.azure.com:5432/$DBNAME?sslmode=require"

az webapp config appsettings set -g $RG -n $APP --settings SPRING_DATASOURCE_URL="$JDBC_URL" SPRING_DATASOURCE_USERNAME="$PGADMIN" SPRING_DATASOURCE_PASSWORD="$PGPASS" JAVA_OPTS="-Xms256m -Xmx512m" SCM_DO_BUILD_DURING_DEPLOYMENT=false


# Passo 7:

git clone https://github.com/nicolassouzasantos/devops_sprint3

cd devops_sprint3

chmod +x mvnw || true

./mvnw -ntp -DskipTests clean package

ls -lh target/*.jar


# Passo 8:

JAR_PATH=$(ls target/*.jar | head -n1)

az webapp deploy -g $RG -n $APP --src-path "$JAR_PATH" --type jar

# Para conectar ao banco no azure CLI:
FQDN=$(az postgres flexible-server show -g "$RG" -n "$PGSERVER" --query fullyQualifiedDomainName -o tsv)

psql "host=$FQDN port=5432 dbname=$DBNAME user=${PGADMIN} sslmode=require"


# Selects no banco de dados:

select * from operador;

select * from patio;

select * from automovel;


# 🧪 TESTES HTTP

# POST http://notesolutions-app.azurewebsites.net/operadores

JSON:

{
  "nome": "João Silva",
  "login": "joao",
  "senha": "SenhaForte@123",
  "papel": "ROLE_ADMIN"
}

JSON:

{
  "nome": "Maria Souza",
  "login": "maria",
  "senha": "Segredo!456"
}

# PUT http://notesolutions-app.azurewebsites.net/operadores/id

{
  "nome": "Teste",
  "login": "teste",
  "senha": "Segredo!456"
}

# DELETE http://notesolutions-app.azurewebsites.net/operadores/id

# POST http://notesolutions-app.azurewebsites.net/patios

JSON:

{
  "nome": "Pátio Central"
}

JSON:

{
  "nome": "Pátio Zona Norte",
  "endereco": "Av. Exemplo, 123 - São Paulo/SP"
}


# POST http://notesolutions-app.azurewebsites.net/automoveis

JSON:

{
  "placa": "ABC1D23",
  "chassi": "9BWZZZ377VT004251",
  "tipo": "Moto",
  "cor": "Preto",
  "localizacaoNoPatio": "Fileira A - Vaga 05",
  "comentarios": "Chegou com arranhões no tanque",
  "patioId": 1
}


JSON:

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

http://notesolutions-app.azurewebsites.net/operadores

http://notesolutions-app.azurewebsites.net/patios

http://notesolutions-app.azurewebsites.net/automoveis


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



