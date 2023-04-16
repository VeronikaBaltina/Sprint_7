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

public class CourierLoginTest {
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

    // ТЕСТ ЛОГИНА КУРЬЕРА
    @Test
    @Description("авторизация курьера, получение id")
    public void authorizationCourierWithCorrectDataTest() {
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
    @Description("авторизация курьера под несуществующим пользователем")
    public void authorizationCourierUnderNonExistentUserTest() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        assertEquals("Неверный status code", 404, statusCode);
        assertEquals("Текст сообщения не совпадает", "Учетная запись не найдена", message);
    }

    @Test
    @Description("авторизация курьера, когда неправильно указан логин и пароль")
    public void authorizationCourierWithIncorrectDataTest() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Неверный status code", 201, statusCode);
        assertTrue("Курьер не создан", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        assertTrue("ID курьера не создан", courierId != 0);

        courier.setPassword("afaf");
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        assertEquals("Неверный status code", 404, statusCode);
        assertEquals("Текст сообщения не совпадает", "Учетная запись не найдена", message);
    }

    @Test
    @Description("авторизация курьера, когда какого-то из полей логин или пароль нет")
    public void authorizationCourierWithEmptyFieldTest() {
        Courier courier = CourierGenerator.getRandom();

        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");
        assertEquals("Неверный status code", 201, statusCode);
        assertTrue("Курьер не создан", isCourierCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        assertTrue("ID курьера не создан", courierId != 0);

        courier.setLogin("");
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        assertEquals("Неверный status code", 400, statusCode);
        assertEquals("Текст сообщения не совпадает", "Недостаточно данных для входа", message);
    }
}
