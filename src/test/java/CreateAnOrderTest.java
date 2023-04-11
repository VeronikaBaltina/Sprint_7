import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.client.OrderClient;
import org.example.generator.OrderGenerator;
import org.example.model.OrderData;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreateAnOrderTest {
    // ТЕСТ СОЗДАНИЯ ЗАКАЗА
    private OrderData order;
    private OrderClient client;
    private int track;
    private final List<String> color;

    public CreateAnOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] Colour() {
        return new Object[][]{
                {List.of("BLACK", "GREY")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void init() {
        order = OrderGenerator.getNewOrder(color);
        client = new OrderClient();
    }

    @After
    @Description("Отмена заказа")
    public void clearData() {
        client.cancel(track);
    }

    @Test
    @Description("Создание заказа")
    public void createAnOrderTest() {
        ValidatableResponse response = client.create(order);
        int statusCode = response.extract().statusCode();
        track = response.extract().path("track");
        assertTrue("Заказ не создан", track != 0);
        assertEquals("Неверный status code", 201, statusCode);
    }
}
