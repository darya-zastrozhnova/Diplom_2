package ru.yandex.practicum.steps;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.model.DuplicateUser;
import ru.yandex.practicum.model.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static final String USER = "/api/auth/register";
    public static final String LOGIN = "/api/auth/login";

    private Response response;

    //создание пользователя
    @Step
    public ValidatableResponse createUser(User user) {
        response = given()
                .body(user)
                .when()
                .post(USER)
                .then()
                .extract().response();
        return response.then();
    }
//получение accessToken
    @Step
    public String extractAccessToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("accessToken");
    }
//получение refreshToken
    @Step
    public String extractRefreshToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("refreshToken");
    }
//создание пользователя, который уже зарегистрирован
    @Step
    public ValidatableResponse createUser(DuplicateUser user) {
        return given()
                .body(user)
                .when()
                .post(USER)
                .then();
//                .extract().response().then();
//        return response.then();
    }
//создание пользователя без обязательных полей
    @Step
    public ValidatableResponse createUserWithoutRequiredFields(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(USER)
                .then();
//                .extract().response().then();
//        return response.then();
    }

//вход под существующим пользователем
    @Step
    public ValidatableResponse login(User user) {
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }
//вход с неверным паролем и неверным логином
    @Step
    public ValidatableResponse wrongPasswordAndWrongLogin(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(LOGIN)
                .then();
    }

//удаление пользователя
    @Step
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
