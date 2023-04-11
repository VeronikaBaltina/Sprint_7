import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.example.client.OrderClient;
import org.example.generator.OrderGenerator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// ТЕСТ ПОЛУЧЕНИЯ СПИСКА ЗАКАЗОВ
public class GetAListOfOrdersTest {
    private OrderClient client;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void init() {
        client = new OrderClient();
    }
    @Test
    @Description("Получаем лист заказов")
    public  void getAListOfOrdersTest(){
        ValidatableResponse responseCreate = client.getListOrder();
        int status = responseCreate.extract().statusCode();
        List<HashMap> body = responseCreate.extract().path("orders");
        assertEquals(200, status);
        assertNotNull(body);
    }
}
