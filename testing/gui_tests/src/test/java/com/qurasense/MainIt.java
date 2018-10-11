package com.qurasense;

import com.qurasense.page.MainPage;
import org.assertj.core.api.Assertions;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Ignore;
import org.junit.Test;

@Wait
@Ignore
public class MainIt extends AbstractIt {

    @Page
    private MainPage mainPage;

    @Test
    public void testMainPageContainsSignupButton() {
        goTo(getBaseUrl());
        Assertions.assertThat($(".main-btn").text().equalsIgnoreCase("sign up"));
    }

    @Test
    public void testAdminCreateUserLinkExistAfterLogin() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("_admin@qurasense.com", "secret");
        mainPage.assertCreateUserLinkDisplayed();
    }

    @Test
    public void testMarieLoginSuccess() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("_marie@qurasense.com", "secret");
        mainPage.assertLoginSuccess();
    }

    @Test
    public void testLiseLoginSuccess() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("_lise@qurasense.com", "secret");
        mainPage.assertLoginSuccess();
    }

    @Test
    public void testAdaLoginNotSuccess() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("_ada@qurasense.com", "secret");
        mainPage.assertLoginNotSuccess();
    }

    @Test
    public void testInvalidLoginNotSuccess() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("invalid", "secret");
        mainPage.assertLoginNotSuccess();
    }

    @Test
    public void testLiseLoginInvalidPasswordNotSuccess() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.login("_lise@qurasense.com", "wrong");
        mainPage.assertLoginNotSuccess();
    }

    @Test
    public void testMarieSuccessfullySendForgotPassword() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.showForgotPassowordPage();
        $(".input").write("_marie@qurasense.com");
        $(".btn-reset material-ripple").click();
        Assertions.assertThat($(".success-box").text()).isNotEmpty();
        Assertions.assertThat($(".error-box").text()).isEmpty();
    }

    @Test
    public void testInvalidNotSuccessfullySendForgotPassword() {
        goTo(mainPage);
        mainPage.showLoginPage();
        mainPage.showForgotPassowordPage();
        $(".input").write("_invalid@qurasense.com");
        $(".btn-reset material-ripple").click();
        Assertions.assertThat($(".success-box").text()).isEmpty();
        Assertions.assertThat($(".error-box").text()).isNotEmpty();
    }

}
