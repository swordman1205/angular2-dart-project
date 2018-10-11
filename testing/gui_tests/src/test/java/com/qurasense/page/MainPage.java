package com.qurasense.page;

import org.assertj.core.api.Assertions;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

@PageUrl("/")
public class MainPage extends FluentPage {

    @FindBy(linkText = "LOGIN")
    private FluentWebElement loginLink;
    @FindBy(css = "#loginInput input")
    private FluentWebElement userNameInput;
    @FindBy(css = "#passwordInput input")
    private FluentWebElement passwordInput;
    @FindBy(id = "loginButton")
    private FluentWebElement submitLoginButton;

    @FindBy(linkText = "create user")
    private FluentWebElement createUserLink;

    @FindBy(css = ".register-card")
    private FluentWebElement registerCard;

    @FindBy(css = ".error-box>.error")
    private FluentWebElement errorMessage;

    @FindBy(linkText = "forgot password?")
    private FluentWebElement forgotPasswordLink;


    public MainPage showLoginPage() {
        loginLink.click();
        return this;
    }

    public MainPage showForgotPassowordPage() {
        forgotPasswordLink.click();
        return this;
    }

    public MainPage login(String username, String password) {
        userNameInput.write(username);
        passwordInput.write(password);
        submitLoginButton.click();
        return this;
    }

    public void assertCreateUserLinkDisplayed() {
        Assertions.assertThat(createUserLink.displayed()).isTrue();
    }

    public void assertLoginSuccess() {
        Assertions.assertThat(registerCard.displayed()).isTrue();
    }

    public void assertLoginNotSuccess() {
        Assertions.assertThat(errorMessage.displayed()).isTrue();
        Assertions.assertThat(errorMessage.text()).isNotEmpty();
    }

}