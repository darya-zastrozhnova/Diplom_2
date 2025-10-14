package ru.yandex.practicum.tests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.model.DuplicateUser;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.steps.UserSteps;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginTests extends BaseTest {

    private UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;
    private String refreshToken;
    private DuplicateUser wrongPassword;
    private DuplicateUser wrongLogin;


    @Before
    public void setUp() {
        user = new User();
        user.setPassword(RandomStringUtils.randomAlphabetic(12));
        user.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        user.setName(RandomStringUtils.randomAlphabetic(12));
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("success", equalTo(true));

        accessToken = userSteps.extractAccessToken();
        refreshToken = userSteps.extractRefreshToken();
        wrongPassword = new DuplicateUser();
        wrongPassword.setEmail(user.getEmail());
        wrongPassword.setPassword("wrongPassword");
        wrongLogin = new DuplicateUser();
        wrongLogin.setEmail("wrongLogin");
        wrongLogin.setPassword(user.getPassword());
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
                .wrongPassword(wrongPassword)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void shouldNotLoginWithWrongLogin() {

        userSteps
                .wrongLogin(wrongLogin)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        accessToken = userSteps.login(user).extract().path("accessToken");
        userSteps.deleteUser(accessToken);
    }
}
