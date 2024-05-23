package Page;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserPage {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";


    public Response getAllUsers() {
        return given()
                .baseUri(BASE_URL)
                .basePath("/users")
                .when()
                .get();
    }


    public Response getUserById(int userId) {
        return given()
                .baseUri(BASE_URL)
                .basePath("/users/{id}")
                .pathParam("id", userId)
                .when()
                .get();
    }

    public Response createUser(User user) {
        return given()
                .baseUri(BASE_URL)
                .basePath("/users")
                .contentType("application/json")
                .body(user)
                .when()
                .post();
    }

    public Response updateUser(int userId, User user) {
        return given()
                .baseUri(BASE_URL)
                .basePath("/users/{id}")
                .pathParam("id", userId)
                .contentType("application/json")
                .body(user)
                .when()
                .put();
    }

    public Response deleteUser(int userId) {
        return given()
                .baseUri(BASE_URL)
                .basePath("/users/{id}")
                .pathParam("id", userId)
                .when()
                .delete();
    }
}
