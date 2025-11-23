# 🚆 WorkSafe API — Global Solution | DevOps & Cloud  

API desenvolvida para o projeto **WorkSafe**, utilizando **Java 21 + Spring Boot 3**, banco em memória **H2**, documentação **Swagger** e deploy completo na nuvem usando **Render (Docker)**.  

Este backend foi configurado para demonstrar conhecimentos de **DevOps**, **deploy contínuo**, **infraestrutura em nuvem**, **monitoramento**, **containers** e **ambiente híbrido (VM Linux + VM Windows)**.

---

## 📡 URL Pública da API (Render)

A API está online em:

👉 **https://worksafe-java-gs.onrender.com**

Swagger:

👉 **https://worksafe-java-gs.onrender.com/swagger-ui.html**

---

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database (memória)**
- **Spring Validation**
- **Spring Security (config relaxada para testes)**
- **Swagger / OpenAPI 3**
- **Docker**
- **Render Cloud**
- **GitHub + Git**

---

## 🗂️ Endpoints Principais

### **GET /workstations**
Lista todas as estações cadastradas (com paginação automática do Spring).

### **POST /workstations**
Cadastra uma nova estação.

### **DELETE /workstations/{id}**
Remove uma estação pelo ID.

---

## 🔧 Configuração do `application.properties`

```properties
server.port=${PORT:8080}

spring.datasource.url=jdbc:h2:mem:worksafedb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

springdoc.swagger-ui.path=/swagger-ui.html
🐋 Dockerfile Utilizado (Render)
dockerfile
Copy code
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
🔥 Deploy no Render
🟦 Tipo do serviço
Web Service (Docker)

🛠️ Build
O Render executa automaticamente o Dockerfile (multi-stage).

🌐 Start
A aplicação sobe com:

nginx
Copy code
java -jar app.jar
🔌 Porta
Configurada via:

ini
Copy code
server.port=${PORT:8080}
🧪 Testes realizados nas VMs
🐧 VM Linux — Teste via curl
bash
Copy code
curl https://worksafe-java-gs.onrender.com/workstations
Retorno esperado:

json
Copy code
{"content":[],"pageable":{...}, ...}
🪟 VM Windows — Teste via Swagger
Acessar:

arduino
Copy code
https://worksafe-java-gs.onrender.com/swagger-ui.html
Testar:

GET /workstations

POST /workstations

DELETE /workstations/{id}

📹 Roteiro sugerido para o vídeo (GS)
Mostrar o repositório no GitHub.

Mostrar o Dockerfile.

Mostrar o serviço no Render ativo.

VM Linux: rodar:

bash
Copy code
curl https://worksafe-java-gs.onrender.com/workstations
VM Windows: abrir o Swagger e executar o CRUD.

Explicar rapidamente o fluxo DevOps:

Push → Render builda → deploy automatizado

Endpoint acessível globalmente

Youtube: https://youtu.be/ruC1I1w8Sgc

👨‍💻 Autores
Yuri Ferreira
RM: 559223

João Santana
RM: 560781
