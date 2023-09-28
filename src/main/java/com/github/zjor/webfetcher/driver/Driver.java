package com.github.zjor.webfetcher.driver;

import com.github.zjor.webfetcher.property.ScrapeProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class Driver implements AutoCloseable {

    private final RemoteWebDriver webDriver;

    @SneakyThrows
    public static Driver DriverBuilder(ScrapeProperty property) {
        var options = new ChromeOptions();
        options.addArguments("--headless=" + property.driver().headless());

        var webDriver = new RemoteWebDriver(new URL(property.driver().url()),
                options,
                false);
        log.info("Instantiated remote web driver");
        return builder()
                .webDriver(webDriver)
                .build();
    }

    public String fetchPageSource(@NotEmpty String url) {
        webDriver.navigate().to(url);
        return webDriver.getPageSource();
    }

    @Override
    public void close() throws Exception {
        if (webDriver != null) {
            webDriver.quit();
            log.info("Closed remote web driver");
        }
    }
}
