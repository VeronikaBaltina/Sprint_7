import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.client.CourierClient;
import org.example.generator.CourierGenerator;
import org.example.model.Courier;
import org.example.model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


// ТЕСТ СОЗДАНИЯ КУРЬЕРА
public class CreatingCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    @Description("Удаление созданного курьера")
    public void clearData() {
        courierClient.delete(courierId);
    }

    // первое создание курьера
    @Test
    @Description("Создание курьера")
    public void createCourierWithCorrectDataTest() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Неверный status code", 201, statusCode);
        assertTrue("Курьер не создан", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        assertTrue("ID курьера не создан", courierId != 0);
    }

    @Test
    @Description("Создание курьера с логином, который уже есть")
    public void createExistingCourierTest() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse successResponse = courierClient.create(courier);
        int successStatusCode = successResponse.extract().statusCode();
        boolean isCourierCreated = successResponse.extract().path("ok");
        assertEquals("Неверный status code", 201, successStatusCode);
        assertTrue("Курьер не создан", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        assertTrue("ID курьера не создан", courierId != 0);

        ValidatableResponse failedResponse = courierClient.create(courier);
        int failedStatusCode = failedResponse.extract().statusCode();
        String message = failedResponse.extract().path("message");
        assertEquals("Неверный status code", 409, failedStatusCode);
        assertEquals("Текст сообщения не совпадает", "Этот логин уже используется. Попробуйте другой.", message);
    }

    //если одного из полей нет
    @Test
    @Description("Создание курьера, когда поле Login пустое")
    public void createCourierWithEmptyLoginTest() {
        Courier courier = CourierGenerator.getRandom();
        courier.setLogin("");

        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Неверный status code", 400, statusCode);
        assertEquals("Текст сообщения не совпадает", "Недостаточно данных для создания учетной записи", message);
    }

    @Test
    @Description("Создание курьера, когда поле Password пустое")
    public void createCourierWithEmptyPasswordTest() {
        Courier courier = CourierGenerator.getRandom();
        courier.setPassword("");

        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Неверный status code", 400, statusCode);
        assertEquals("Текст сообщения не совпадает", "Недостаточно данных для создания учетной записи", message);
    }
}
