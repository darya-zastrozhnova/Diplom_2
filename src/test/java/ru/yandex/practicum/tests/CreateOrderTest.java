package ru.yandex.practicum.tests;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.steps.OrderSteps;
import ru.yandex.practicum.steps.UserSteps;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;
    private OrderSteps orderSteps = new OrderSteps();
    private String accessToken;
    private Order order;

    @Before
    public void setUp() {
        user = new User();
        user.withPassword(RandomStringUtils.randomAlphabetic(12));
        user.withEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        user.withName(RandomStringUtils.randomAlphabetic(12));
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("success", equalTo(true));

        accessToken = userSteps.extractAccessToken();
        order = new Order();
        order.setIngredients(new ArrayList<>());
    }

    @Test
    public void shouldCreateOrderSuccessfully() {

        ValidatableResponse getResponse = OrderSteps.getIngredients();
        String firstIngredient = getResponse.extract().path("data[0]._id");
        String secondIngredient = getResponse.extract().path("data[1]._id");
        List<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);
        Order order = new Order();
        order.setIngredients(ingredients);

        orderSteps.createOrder(accessToken, order)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void shouldNotCreateOtderWithoutIngredients() {
        orderSteps.createOrder(accessToken, order)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void shouldNotCreateOrderWithInvalidIngredients() {
        List<String> invalidIngredients = new ArrayList<>();
        invalidIngredients.add("invalidHash1");
        invalidIngredients.add("invalidHash2");
        order.setIngredients(invalidIngredients);
        orderSteps.createOrder(accessToken, order)
                .statusCode(500);
    }

    @Test
    public void shouldNotCreateOrderWithoutAuthorization() {
        ValidatableResponse getResponse = OrderSteps.getIngredients();
        String firstIngredient = getResponse.extract().path("data[0]._id");
        String secondIngredient = getResponse.extract().path("data[1]._id");
        List<String> ingredients = new ArrayList<>();
        ingredients.add(firstIngredient);
        ingredients.add(secondIngredient);
        Order order = new Order();
        order.setIngredients(ingredients);

        orderSteps.createOrder(null, order)
                .statusCode(401);
    }

    @After
    public void tearDown() {
        if (accessToken == null) {
            accessToken = userSteps.login(user).extract().path("accessToken");
            if (accessToken == null) {
                return;
            }
        }
        userSteps.deleteUser(accessToken);
    }
}
