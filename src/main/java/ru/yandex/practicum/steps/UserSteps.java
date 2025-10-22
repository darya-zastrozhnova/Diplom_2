package ru.yandex.practicum.steps;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static final String USER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";

    private Response response;


    @Step("создание пользователя")
    public ValidatableResponse createUser(User user) {
        response = given()
                .body(user)
                .when()
                .post(USER)
                .then()
                .extract().response();
        return response.then();
    }

    @Step("получение accessToken")
    public String extractAccessToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("accessToken");
    }

    @Step("получение refreshToken")
    public String extractRefreshToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("refreshToken");
    }

//    @Step("создание пользователя, который уже зарегистрирован")
//    public ValidatableResponse createUser(User user) {
//        return given()
//                .body(user)
//                .when()
//                .post(USER)
//                .then();
//                .extract().response().then();
//        return response.then();
//    }

    @Step("создание пользователя без обязательных полей")
    public ValidatableResponse createUserWithoutRequiredFields(User user) {
        return given()
                .body(user)
                .when()
                .post(USER)
                .then();
//                .extract().response().then();
//        return response.then();
    }


    @Step("вход под существующем пользователем")
    public ValidatableResponse login(User user) {
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("вход с неверным паролем и неверным логином")
    public ValidatableResponse wrongPasswordAndWrongLogin(User user) {
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }


    @Step("удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Authorization token cannot be null");
        }
        return given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}
