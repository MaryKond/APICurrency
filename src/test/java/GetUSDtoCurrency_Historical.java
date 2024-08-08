import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class GetUSDtoCurrency_Historical {

    private static Response response;
    private static String date;

@Test
public void incorrectDateSent(){
//_____________________________________________________________________________________________
    //CHECKING IF THE REQUESTED DATE IS CORRECT
    date="date=1999-12-41&";
    response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL_HISTORICAL+date+Consts.TOKEN+Consts.USD_CAD);
    response.then().statusCode(200);
    System.out.println(response.asString());
   String errorInfo = response.jsonPath().getString("error.info");
    if (errorInfo != null ) {
        assertTrue(errorInfo.contains("blsa "),"error message is missing");
        //System.out.println("You have entered an invalid date. Required format: date=YYYY-MM-DD");
    }
    response.then().body("success", equalTo(true));

//_____________________________________________________________________________________________
    //VALIDATING RECEIVED DATE
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
@Test
public void severalCurrenciesHistorical (){

    date="date=2018-01-01&";
    response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL_HISTORICAL+date+Consts.TOKEN+Consts.SEVERAL);
    response.then().statusCode(200);
    System.out.println(response.asString());
    String errorInfo = response.jsonPath().getString("error.info");


//_____________________________________________________________________________________________
    //VALIDATING RECEIVED DATE
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
//_____________________________________________________________________________________________
    // CHECKING IF EVERY CURRENCY IS PROCESSED
    // List of requested currencies
    List<String> requestedCurrencies = Arrays.asList("EUR", "CAD", "RUB", "NIS");

    // Extract the returned currencies from the response
    Set<String> quoteKeys = response.jsonPath().getMap("quotes").keySet().stream()
            .map(Object::toString)  // Convert each key to a string
            .collect(Collectors.toSet());  // Collect as a set of strings


    // Ensure the keys are treated as strings and remove the 'USD' prefix
    List<String> returnedCurrencies = quoteKeys.stream()
            .map(Object::toString) // Ensure each key is treated as a string
            .map(key -> key.substring(3)) // Remove the 'USD' prefix
            .collect(Collectors.toList());


    // Print the returned currencies
    System.out.println("Returned currencies: " + returnedCurrencies);

    // Verify that all requested currencies are present in the response
    for (String currency : requestedCurrencies) {
        try {
            assertTrue(returnedCurrencies.contains(currency),
                    "Currency " + currency + " is missing in the response.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
            System.out.println("Invalid Currency Codes");
            verifyMissingCurrencyError(Arrays.asList(currency));
            fail("One or more currencies are missing in the response.");
        }}}

    private static void verifyMissingCurrencyError(List<String> missingCurrencies) {
        // Example of extracting and verifying error information from the response
       String errorInfo = response.jsonPath().getString("error.info");

        System.out.println("Error Info: " + errorInfo);

        // Check if the error info contains expected message for each missing currency
        for (String missingCurrency : missingCurrencies) {
            assertTrue(errorInfo.contains("You have provided one or more invalid Currency Codes."),
                    "Expected error message for missing currency " + missingCurrency + " is not present in the response.");
        }

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
