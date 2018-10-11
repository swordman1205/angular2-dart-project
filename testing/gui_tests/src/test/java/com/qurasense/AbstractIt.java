package com.qurasense;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.fluentlenium.adapter.junit.FluentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AbstractIt extends FluentTest {

    private String baseUrl;

    @Override
    public String getBaseUrl() {
        if (Objects.isNull(baseUrl)) {
            baseUrl = Stream.of(Optional.ofNullable(System.getenv("E2E_TEST_URL")),
                    Optional.of("http://localhost:52444"))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst().get();
            System.out.println("!!!e2e test url is: " + baseUrl);
        }
        return baseUrl;
    }

    @Override
    public WebDriver newWebDriver() {
        String webDriverPath = Stream.of(Optional.ofNullable(System.getenv("CHROME_DRIVER_PATH")),
                    Optional.ofNullable(System.getProperty("webdriver.chrome.driver")),
                    Optional.of("C:\\Infrastructure\\chromedriver_win32\\chromedriver.exe"))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst().get();
        System.out.println("!!!Web driver is: " + webDriverPath);
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();

        ChromeOptions options = new ChromeOptions();
        //uncomment this if don`t want tests be visible
        String e2ETestHeadless = System.getenv("E2E_TEST_HEADLESS");
        System.out.println("!!!e2e headless is: " + e2ETestHeadless);
        if (Boolean.TRUE.toString().equalsIgnoreCase(e2ETestHeadless)) {
            options.addArguments("headless");
        }
        options.addArguments("--window-size=1920,1080");

        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver(options);
    }
}
