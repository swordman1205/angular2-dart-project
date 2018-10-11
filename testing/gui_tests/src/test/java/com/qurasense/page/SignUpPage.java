package com.qurasense.page;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;

@PageUrl("/#/site/register/step/1/1")
public class SignUpPage extends FluentPage {

//    @FindBy(css = ".btn-next material-ripple")
//    private FluentWebElement nextButton;


    public void clickNextStep1() {
//        FluentList<FluentWebElement> fluentWebElements = find("signup-step1 section.active .btn-next material-ripple");
////        System.out.println("size: " + fluentWebElements.size());
//
////        FluentList<FluentWebElement> els = $("section.active .btn-next material-ripple");
//        await().atMost(5, TimeUnit.SECONDS).until(() -> fluentWebElements.one().clickable());
////        await().atMost(5, TimeUnit.SECONDS).until(fluentWebElements.find(FilterConstructor.withPredicate((e)->e.clickable())))
//        FluentList<FluentWebElement> clickableNext = fluentWebElements.find(FilterConstructor.withPredicate((e) -> e.clickable()));
//        FluentWebElement first = clickableNext.first();
////        System.out.println("displayed: " + first.displayed());
////        System.out.println("clickable: " + first.clickable());
//        first.click();
        FluentList<FluentWebElement> fluentWebElements = find("signup-step1 section.active .btn-next material-ripple");
        fluentWebElements.first().click();
    }

    public void clickNextStep2() {
        FluentList<FluentWebElement> fluentWebElements = find("signup-step2 section.active .btn-next material-ripple");
        fluentWebElements.first().click();
    }

    public void clickNextStep3() {
        FluentList<FluentWebElement> fluentWebElements = find("signup-step3 section.active .btn-next material-ripple");
        fluentWebElements.first().click();
    }

    public void clickNextStep4() {
        FluentList<FluentWebElement> fluentWebElements = find("signup-step4 section.active .btn-next material-ripple");
        fluentWebElements.first().click();
    }

}
