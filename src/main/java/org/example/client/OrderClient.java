package org.example.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.ValidatableResponse;
import org.example.client.base.ScooterRestClient;
import org.example.model.Courier;
import org.example.model.OrderData;
import org.example.model.OrderTrackNumber;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {
    private static final String CREATE_ORDER_URL = BASE_URL + "orders/";
    private static final String CANCEL_ORDER_URL = BASE_URL + "orders/cancel/";

    public ValidatableResponse create(OrderData orderData) {
        return given()
                .spec(getBaseReqSpec())
                .body(orderData)
                .when()
                .post(CREATE_ORDER_URL)
                .then();
    }

    public ValidatableResponse cancel(int trackId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OrderTrackNumber trackNumber = new OrderTrackNumber(trackId);
        return given()
                .spec(getBaseReqSpec())
                .body(gson.toJson(trackNumber))
                .when()
                .put(CANCEL_ORDER_URL)
                .then();
    }

    public ValidatableResponse getListOrder() {
        return given()
                .when()
                .get(CREATE_ORDER_URL)
                .then();
    }
}
