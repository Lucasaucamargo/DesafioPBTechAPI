# DesafioPBTechAPI

Este projeto é um conjunto de testes automatizados utilizando a biblioteca RestAssured para testar uma API REST. 
Os testes cobrem operações CRUD (Create, Read, Update, Delete) em usuários, além de validar o esquema JSON das respostas usando a biblioteca JsonSchemaValidator. 
O Allure Framework é usado para gerar relatórios detalhados dos testes.


## Estrutura do Projeto

DesafioPBTechAPI/
├── src/
│ ├── main/
│ │ └── java/
│ │ └── Page/
│ │ └── UserPage.java
│ ├── test/
│ │ └── java/
│ │ ├── Config/
│ │ │ └── AllureSetup.java
│ │ └── tests/
│ │ └── runTest.java
│ └── resources/
│ └── schemas/
│ ├── UsersSchema.json
│ └── SingleUser.json
├── pom.xml
└── README.md



## Requisitos

- Java 8 ou superior
- Maven
- jUnit 5

## Configuração

1. Clone este repositório para sua máquina local:

    ```sh
    git clone https://github.com/Lucasaucamargo/DesafioPBTechAPI.git
    cd DesafioPBTechAPI
    ```

2. Instale as dependências do Maven:

    ```sh
    mvn clean install
    ```

## Executando os Testes

Para executar os testes, use o seguinte comando:

```sh
mvn test

Os relatórios de teste do Allure serão gerados no diretório target/allure-results.

Gerando Relatórios com Allure
Para visualizar os relatórios do Allure, você precisará do Allure CLI. Instale o Allure CLI seguindo as instruções da documentação oficial.

Depois de instalar o Allure CLI, gere e sirva os relatórios com os comandos:
allure serve target/allure-results

Contato
Para mais informações, entre em contato pelo email: lcamargo12@gmail.com

Este projeto é apenas para fins de demonstração e aprendizado. O uso das informações e códigos aqui fornecidos é de responsabilidade do usuário.