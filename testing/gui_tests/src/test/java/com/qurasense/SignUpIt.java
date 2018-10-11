package com.qurasense;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import com.qurasense.page.SignUpPage;
import com.qurasense.page.ThanksRegisterPage;
import org.assertj.core.api.Assertions;
import org.fluentlenium.core.annotation.Page;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Test;

@Wait
public class SignUpIt extends AbstractIt {

    @Page
    private SignUpPage signUpPage;

    @Page
    private ThanksRegisterPage thanksRegisterPage;

    @Test
    public void testSignupUsingValidData() {
        goTo(signUpPage);

        //DID YOU MENSTRUATE WITHIN THE LAST 6 MONTHS?
        $(".btn-yes material-ripple").click();

        //WHAT IS YOUR FULL NAME?
        $("[ngcontrol=fullName] input").write("Madeleine Albright");
        signUpPage.clickNextStep1();

        //WHEN IS YOUR BIRTHDAY?
        $("date-selector[ngcontrol=birthDate] input").write("01/01/1990");
        signUpPage.clickNextStep1();

        //FIRST DAY OF YOUR LAST PERIOD
        $("date-selector[ngcontrol=firstDateOfLastPeriod] input").write(DateUtils.formatDate(LocalDate.now().minusDays(31)));
        signUpPage.clickNextStep2();

        //TYPICAL LENGTH OF PERIOD
        $(".number-picker-increment material-ripple").click();
        signUpPage.clickNextStep2();

        //TYPICAL LENGTH OF CYCLE
        $("signup-step2 section.active .choice-selector span").index(3).click();

        //SEXUAL ACTIVITY
        $("signup-step3 section.active .choice-selector span").index(1).click();

        //BIRTH CONTROL
        $("signup-step3 section.active .choice-selector span").index(4).click();
        signUpPage.clickNextStep3();

        //width
        $("signup-step3 section.active .input").write("130");
        signUpPage.clickNextStep3();

        //height
        $("signup-step3 section.active dropdown-button").index(0).click();
        $("material-select-item").index(3).click();
        await().atMost(1, TimeUnit.SECONDS).until($("material-select-item")).not().present();
        $("signup-step3 section.active dropdown-button").index(1).click();
//        await().atMost(1, TimeUnit.SECONDS).until(el(".acx-overlay-container div[pane-id=default-2] material-select-item")).clickable();
//        $(".acx-overlay-container div[pane-id=default-2] material-select-item").index(5).click();
        $("material-select-item").index(5).click();
        signUpPage.clickNextStep3();

        //ADDRESS
        $("signup-step4 section.active material-input input").index(0).write("1600 Amphitheatre Pkwy");
        $("signup-step4 section.active material-input input").index(1).write("Mountain View");
        $("signup-step4 section.active material-input input").index(2).write("CA");
        $("signup-step4 section.active material-input input").index(3).write("94043");
        signUpPage.clickNextStep4();

        //PHONE
        $("signup-step4 section.active input").index(0).write("+1 650-253-0000");
        signUpPage.clickNextStep4();

        //WHEN CAN WE CALL YOU?
        $("signup-step4 section.active .choice-selector").index(0).find("span").index(0).click();
        $("signup-step4 section.active .choice-selector").index(1).find("span").index(0).click();
        signUpPage.clickNextStep4();

        //EMAIL
        $("signup-step4 section.active input").index(0).write("_madeleine@qurasense.com");
        signUpPage.clickNextStep4();

        //PASSWORD
        await().atMost(1, TimeUnit.SECONDS).until(el("password-set")).displayed();
        $("signup-step4 section.active input").index(0).write("secret");
        $("signup-step4 section.active input").index(1).write("secret");
        signUpPage.clickNextStep4();

        //TERMS AND CONDITIONS
        $("signup-step4 section.active two-label-toggle").click();
        $("signup-step4 section.active material-button material-ripple").click();

        await().atMost(5, TimeUnit.SECONDS).untilPage(thanksRegisterPage).isAt();
        Assertions.assertThat($(".page-article_subtitle").text()).isEqualTo("THANK YOU!");
    }

}
