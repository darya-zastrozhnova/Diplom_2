package ru.yandex.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    public static final String ORDER = "/api/orders";

    //создание заказа с авторизацией
    @Step
    public ValidatableResponse createOrder(String token, Order order) {
        RequestSpecification requestSpecification = given();
        if (token != null) {
            requestSpecification.header("Authorization", token);
        }
        return given()
                .body(order)
                .post(ORDER)
                .then();
    }
    //получение ингредиентов
    @Step
    public static ValidatableResponse getIngredients() {
        return given()
                .when()
                .get("/api/ingredients")
                .then();
    }

}
