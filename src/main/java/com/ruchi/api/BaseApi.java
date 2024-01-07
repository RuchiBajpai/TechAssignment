package com.ruchi.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseApi {

    private final String BASE_URL = System.getProperty("baseUrl", "http://localhost:9997/api/v1");

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .addFilter(new AllureRestAssured())
            .build();

    protected RequestSpecification requestSender = RestAssured.given(requestSpecification);
}
