package ru.netology.cases;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.helpers.DataHelper.User.*;

public class LoginPageTest {


    SelenideElement titleHeader = element(byText("Интернет Банк"));
    SelenideElement titleLK = element(byText("Личный кабинет"));
    SelenideElement inputLogin = element("[data-test-id='login'] .input__control");
    SelenideElement inputPassword = element("[data-test-id='password'] .input__control");
    SelenideElement buttonSubmit = element("[data-test-id='action-login'] .button__text");
    SelenideElement errorMessage = element("[data-test-id='error-notification'] .notification__content");

    @BeforeEach
    void setUp() {
        userRegistration("active");
        open("http://localhost:9999/");
        titleHeader.shouldBe(visible);
        inputLogin.setValue(getLogin());
        inputPassword.setValue(getPassword());
    }

    @Test
    @DisplayName("Проверка успешного логина")
    void shouldSuccessLogin() {
        buttonSubmit.click();
        titleLK.shouldBe(visible);
    }

    @Test
    @DisplayName("Проверка ошибки при вводе другого имени")
    void shouldNotLoginWrongName() {
        inputLogin.setValue(getAnotherLogin());
        buttonSubmit.click();
        errorMessage.shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Проверка ошибки при вводе другого пароля")
    void shouldNotLoginWrongPass() {
        inputPassword.setValue(getAnotherPassword());
        buttonSubmit.click();
        errorMessage.shouldBe(visible)
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Проверка входа под заблокированным пользователем")
    void shouldNotLoginBlockedUser() {
        userRegistration("blocked");
        buttonSubmit.click();
        errorMessage.shouldBe(visible)
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }
}
