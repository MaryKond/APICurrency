import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthorizationPositiveNegativeTest {
    @Test
    public void validTokenTest(){
        Response response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL+Consts.TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(200);

    }
    @Test
    public void invalidTokenTest(){
        Response response=given().auth().oauth2(Consts.INVALID_TOKEN).contentType("application/json").get(Consts.URL+Consts.INVALID_TOKEN);
        System.out.println(response.asString());
        response.then().statusCode(401);
        response.then().body("message", equalTo("Invalid authentication credentials"));

    }
}