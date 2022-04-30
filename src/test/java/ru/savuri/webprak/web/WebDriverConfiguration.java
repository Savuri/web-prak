package ru.savuri.webprak.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfiguration {
    @Bean
    public WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        System.setProperty("webdriver.firefox.driver", "geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);

        return new FirefoxDriver(options);
    }
}
