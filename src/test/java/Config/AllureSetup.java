package Config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class AllureSetup {

    @BeforeAll
    public static void setUp() {
        RestAssured.filters(new AllureRestAssured());
    }
}