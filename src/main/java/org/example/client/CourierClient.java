package org.example.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.client.base.ScooterRestClient;
import org.example.model.Courier;
import org.example.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient {
    private static final String CREATE_COURIER_URL = BASE_URL + "courier/";
    private static final String LOGIN_COURIER_URL = BASE_URL + "courier/login/";

    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(CREATE_COURIER_URL)
                .then();

    }

    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(LOGIN_COURIER_URL)
                .then();
    }

    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(CREATE_COURIER_URL + id)
                .then();
    }
}
