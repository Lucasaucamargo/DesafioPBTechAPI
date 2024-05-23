package tests;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import Page.*;
import Config.AllureSetup;
import java.io.*;

public class runTest {

    private UserPage userPage;
    private ByteArrayOutputStream consoleOut;
    private ByteArrayOutputStream consoleErr;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    public void setUp() {
        userPage = new UserPage();
        redirectSystemStreams();
    }

    @AfterEach
    public void tearDown() {
        attachConsoleLogs();
        restoreSystemStreams();
    }

    @BeforeAll
    public static void globalSetUp() {
        // Configuração global para Allure
        AllureSetup.setUp();
    }

    private void redirectSystemStreams() {
        originalOut = System.out;
        originalErr = System.err;
        consoleOut = new ByteArrayOutputStream();
        consoleErr = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOut));
        System.setErr(new PrintStream(consoleErr));
    }

    private void restoreSystemStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    private void attachConsoleLogs() {
        Allure.addAttachment("System Out", new ByteArrayInputStream(consoleOut.toByteArray()));
        Allure.addAttachment("System Err", new ByteArrayInputStream(consoleErr.toByteArray()));
    }

    @Test
    @Order(1)
    @DisplayName("Validação do Schema de Todos os Usuários, Exibindo Id e username")
    public void testValidateAllUsersSchema() {
        Response response = userPage.getAllUsers();
        Assertions.assertEquals(200, response.getStatusCode(), "O status code da resposta não é 200 OK");

        response.then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/UsersSchema.json"));

        // Pega todos os usuários da resposta
        User[] users = response.as(User[].class);

        // Exibe todos os usernames junto com seus IDs
        StringBuilder message = new StringBuilder();
        message.append("Foram encontrados ").append(users.length).append(" usuários.\n");
        message.append("São esses usuários:\n");

        for (User user : users) {
            message.append("id: ").append(user.getId()).append("\n");
            message.append("Username: ").append(user.getUsername()).append("\n");
        }

        System.out.println(message);
    }

    @Test
    @Order(2)
    @DisplayName("Buscando o primeiro Usuário da lista")
    public void testGetUser() {
        int userId = 1; // ID do usuário que deseja testar
        Response response = userPage.getUserById(userId);
        Assertions.assertEquals(200, response.getStatusCode());
        User user = response.getBody().as(User.class);


        System.out.println("O Usuário buscado é:");
        System.out.println("ID: " + user.getId());
        System.out.println("Nome: " + user.getName());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Endereço: " + user.getAddress().getStreet() + ", " +
                user.getAddress().getSuite() + ", " +
                user.getAddress().getCity() + ", " +
                user.getAddress().getZipcode());
        System.out.println("Geo: " + user.getAddress().getGeo().getLat() + ", " + user.getAddress().getGeo().getLng());
        System.out.println("Telefone: " + user.getPhone());
        System.out.println("Website: " + user.getWebsite());
        System.out.println("Companhia: " + user.getCompany().getName() + ", " +
                user.getCompany().getCatchPhrase() + ", " +
                user.getCompany().getBs());

        response.then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/SingleUser.json"));

        Assertions.assertEquals(userId, user.getId());
        Assertions.assertNotNull(user.getName());
        Assertions.assertNotNull(user.getUsername());
        Assertions.assertNotNull(user.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Criando usuário")
    public void testCreateUser() {
        User.Address.Geo geo = new User.Address.Geo("-37.3159", "81.1496");
        User.Address address = new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", geo);
        User.Company company = new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets");

        User user = new User(11, "Lacam Augusto", "Lacam", "newuser@exemplo.com", address, "1-770-736-8031 x56442", "hildegard.org", company);
        Response response = userPage.createUser(user);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(201, response.getStatusCode());
        if (statusCode == 201) {
            System.out.println("Usuário criado com sucesso.");
        } else {
            System.err.println("Falha ao criar usuário");
        }

        // Validação do schema JSON
        response.then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/SingleUser.json"));
    }

    @Test
    @Order(4)
    @DisplayName("Alterando usuário")
    public void testUpdateUser() {
        int userId = 2;

        User.Address.Geo geo = new User.Address.Geo("-37.3159", "81.1496");
        User.Address address = new User.Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874", geo);
        User.Company company = new User.Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets");

        User user = new User(userId, "Updated User", "updateduser", "updateduser@exemplo.com", address, "1-770-736-8031 x56442", "hildegard.org", company);

        Response response = userPage.updateUser(userId, user);
        int statusCode = response.getStatusCode();

        // Verificação e mensagens de sucesso/falha
        if (statusCode == 200) {
            System.out.println("Usuário com ID " + userId + " foi atualizado com sucesso.");
        } else {
            System.err.println("Falha ao atualizar o usuário com ID " + userId + ". Status code: " + statusCode);
        }

        // Asserção para garantir que o teste falhe se o status não for 200
        Assertions.assertEquals(200, statusCode, "Falha ao atualizar o usuário. Status code diferente de 200.");

        // Verificação do schema do JSON da resposta
        response.then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/SingleUser.json"));
    }

    @Test
    @Order(5)
    @DisplayName("Excluindo um usuário")
    public void testDeleteUser() {
        int userId = 4;
        Response response = userPage.deleteUser(userId);
        int statusCode = response.getStatusCode();

        // Validação de sucesso ou falha
        if (statusCode == 200) {
            System.out.println("Usuário com ID " + userId + " foi deletado com sucesso.");
        } else {
            System.err.println("Falha ao excluir o usuário com ID " + userId + ". Status code: " + statusCode);
        }

        Assertions.assertEquals(200, statusCode, "Falha ao excluir o usuário. Status code diferente de 200.");
    }
}