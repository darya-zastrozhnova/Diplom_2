package ru.yandex.practicum.steps;

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

    @Step
    public String extractAccessToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("accessToken");
    }

    @Step
    public String extractRefreshToken() {
        if (response == null) {
            throw new IllegalStateException("Response is not initialized. Did you call createUser or login?");
        }
        return response
                .path("refreshToken");
    }

    @Step
    public ValidatableResponse createUser(DuplicateUser user) {
        return given()
                .body(user)
                .when()
                .post(USER)
                .then();
    }

    @Step
    public ValidatableResponse createUserWithoutEmail(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(USER)
                .then();
    }

    @Step
    public ValidatableResponse createUserWithoutPassword(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(USER)
                .then();
    }

    @Step
    public ValidatableResponse createUserWithoutName(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(USER)
                .then();
    }

    @Step
    public ValidatableResponse login(User user) {
        return given()
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step
    public ValidatableResponse wrongPassword(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step
    public ValidatableResponse wrongLogin(DuplicateUser duplicateUser) {
        return given()
                .body(duplicateUser)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}
