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

public class UserTests extends BaseTest {
    private UserSteps userSteps = new UserSteps();
    private User user;
    private DuplicateUser duplicateUser;
    private String accessToken;
    private DuplicateUser createUserWithoutEmail;
    private DuplicateUser createUserWithoutPassword;
    private DuplicateUser createUserWithoutName;


    @Before
    public void setUp() {
        user = new User();
        user.withPassword(RandomStringUtils.randomAlphabetic(12));
        user.withEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        user.withName(RandomStringUtils.randomAlphabetic(12));
        duplicateUser = new DuplicateUser();
        duplicateUser.setPassword(user.getPassword());
        duplicateUser.setEmail(user.getEmail());
        duplicateUser.setName(user.getName());
        createUserWithoutEmail = new DuplicateUser();
        createUserWithoutEmail.setEmail("");
        createUserWithoutEmail.setPassword(RandomStringUtils.randomAlphabetic(12));
        createUserWithoutEmail.setName(RandomStringUtils.randomAlphabetic(12));
        createUserWithoutPassword = new DuplicateUser();
        createUserWithoutPassword.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        createUserWithoutPassword.setPassword("");
        createUserWithoutPassword.setName(RandomStringUtils.randomAlphabetic(12));
        createUserWithoutName = new DuplicateUser();
        createUserWithoutName.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        createUserWithoutName.setPassword(RandomStringUtils.randomAlphabetic(12));
        createUserWithoutName.setName("");
    }

    @Test
    public void shouldCreateUserTest() {
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("user.email", notNullValue())
                .body("user.name", notNullValue())
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());

    }

    @Test
    public void shouldCreateUserDuplicate() {
        userSteps
                .createUser(duplicateUser)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void shouldNotCreateUserWithoutEmail() {
        userSteps
                .createUserWithoutRequiredFields(createUserWithoutEmail)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void shouldNotCreateUserWithoutPassword() {
        userSteps
                .createUserWithoutRequiredFields(createUserWithoutPassword)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void shouldNotCreateUserWithoutName() {
        userSteps
                .createUserWithoutRequiredFields(createUserWithoutName)
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
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
