import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


import io.restassured.response.Response;
        import org.junit.jupiter.api.BeforeAll;
        import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static io.restassured.RestAssured.given;

public class GetUSDtoCADrate {

    private static Response response;
    @BeforeAll
    public static void setup(){
        response=given().auth().oauth2(Consts.TOKEN).contentType("application/json").get(Consts.URL+Consts.TOKEN+Consts.USD_CAD);
        System.out.println(response.asString());
    }
    @Test
    public void getUSDtoCADrate(){

        response.then().statusCode(200);
//        //get timestamp from response
//        Integer actualMs = response.path("timestamp");

//
//
//        long epoch = actualMs;
//
//        // Convert epoch time to Instant
//        Instant instant = Instant.ofEpochSecond(epoch);
//
//        // Convert Instant to LocalDateTime
//        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//
//        // Define the desired format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        // Format LocalDateTime to string
//        String formattedDate = dateTime.format(formatter);
//
//        // Print the formatted date
//        System.out.println("Formatted Date: " + formattedDate);
//
//
//
////        //create format to match expected String date
////        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////
////        //get Date from the timestamp in request - a hard part, we need to set it to long and multiply by 1000
////        //as in this case API returns UNIX time and not epoch time
////        Date date2 = new Date((long)actualMs*1000);
////
////        //format date from response to match expected String date
////        String actual = format.format(date2.getTime());
////        //getting time from ime converter
////        response=given().contentType("application/json").get(Consts.TIME_CONVERTER+actualMs);
////        System.out.println(response.asString());
////
//////        String epoch=;
//////        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
//////        System.out.println(date);
    }
}
