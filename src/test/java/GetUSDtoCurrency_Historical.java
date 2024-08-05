import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static io.restassured.RestAssured.given;

public class GetUSDtoCurrency_Historical {

    private static Response response;
    private static String date;


    @ParameterizedTest
    @ValueSource(strings = {Consts.USD_EUR,Consts.USD_CAD,Consts.USD_RUB,Consts.USD_NIS})
    void getRateHistorical(String str) {

        date="date=2018-01-01&";
       response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL_HISTORICAL+date+Consts.TOKEN+str);
        response.then().statusCode(200);
        System.out.println(response.asString());
        String errorInfo = response.jsonPath().getString("error.info");
        String currency = str.substring(str.length() - 3);

        if (errorInfo != null && errorInfo.contains("invalid Currency Codes")) {
            System.out.println("Currency doesn't exist: " + currency);
        } else {
            System.out.println("all good");
        }
        response.then().body("success", equalTo(true));


        long timestamp = response.jsonPath().getLong("timestamp");

        // Convert epoch timestamp to human-readable date
        String actualDate = convertEpochToDate(timestamp);

        // Expected date in the response (based on the input date)
        String expectedDate = "2018-01-01";

        // Print and compare the dates
        System.out.println("Actual date: " + actualDate);
        System.out.println("Expected date: " + expectedDate);

        // Verify that the actual date matches the expected date
        assertEquals(expectedDate, actualDate, "The dates do not match.");


    }

    private static String convertEpochToDate(long epochSeconds) {
        // Create a new date object from the epoch timestamp
        Date date = new Date(epochSeconds * 1000); // Convert seconds to milliseconds

        // Create a formatter to convert the date to a string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Set the timezone to UTC for consistency
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Return the formatted date string
        return sdf.format(date);
    }
}
