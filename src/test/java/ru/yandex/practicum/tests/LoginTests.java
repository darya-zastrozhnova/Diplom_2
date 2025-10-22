package ru.yandex.practicum.tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.steps.UserSteps;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginTests extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;
    private String refreshToken;
    private User wrongPassword;
    private User wrongLogin;


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
        refreshToken = userSteps.extractRefreshToken();
        wrongPassword = new User();
        wrongPassword.withEmail(user.getEmail());
        wrongPassword.withPassword("wrongPassword");
        wrongLogin = new User();
        wrongLogin.withEmail("wrongLogin");
        wrongLogin.withPassword(user.getPassword());
    }

    @Test
    public void shouldLoginUserTest() {

        userSteps
                .login(user)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", notNullValue())
                .body("user.name", notNullValue());
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {

        userSteps
                .wrongPasswordAndWrongLogin(wrongPassword)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void shouldNotLoginWithWrongLogin() {

        userSteps
                .wrongPasswordAndWrongLogin(wrongLogin)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
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
