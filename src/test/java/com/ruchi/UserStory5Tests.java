package com.ruchi;

import com.ruchi.api.HeroApi;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;


public class UserStory5Tests {
    private HeroApi heroApi = new HeroApi();
    private final int natId = 1;
    @Test
    @DisplayName("US5:AC1 : Get the money owed for an existing hero")
    public void heroOwedMoneyTest_AC1(){
        heroApi.getOwedMoney(natId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("US5:AC2 : Get error for non numeric natId")
    public void heroOwedMoneyTest_AC2(){
        heroApi.getOwedMoney("abc")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("US5:AC3 and AC4 : Verify the response structure")
    public void heroOwedMoneyTest_AC3_And_AC4(){
        Response response = heroApi.getOwedMoney(natId)
                .then()
                .statusCode(200)
                .body("message.data", equalTo("natid-" + natId))
                .body("message.status", equalTo("OWE"))
                .extract()
                .response();

        String timestamp = response.path("timestamp");
        assert timestamp != null && !timestamp.isEmpty();
    }
}
