import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Currency;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class GetUSDtoCurrencyRate {

    private static Response response;


    @ParameterizedTest
    @ValueSource(strings = {Consts.USD_EUR,Consts.USD_CAD,Consts.USD_RUB,Consts.USD_NIS})
    void getRate(String str) {
        response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL+Consts.TOKEN+str);
        System.out.println(response.asString());
        response.then().statusCode(200);


        String errorInfo = response.jsonPath().getString("error.info");
        String currency = str.substring(str.length() - 3);

        if (errorInfo != null && errorInfo.contains("invalid Currency Codes")) {
                    System.out.println("Currency doesn't exist: " + currency);
                } else {
                    System.out.println("all good");
                }
        response.then().body("success", equalTo(true));

    }
    @Test
    public void severalCurrencies (){
        response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL+Consts.TOKEN+Consts.SEVERAL);
        response.then().statusCode(200);
        response.then().body("success", equalTo(true));
        System.out.println(response.asString());
    }
}
