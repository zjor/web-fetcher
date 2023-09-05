package com.github.zjor.webfetcher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
public class WebFetcher {

    private WebDriver driver;

    private void initDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        driver = new FirefoxDriver(options);
    }

    private void closeDriver() {
        Objects.requireNonNull(driver, "driver is not initialized");
        driver.close();
    }

    private void fetchAndSave(String url, String outFilename) {
        Objects.requireNonNull(driver, "driver is not initialized");
        log.info("fetching URL: {}", url);
        driver.get(url);
        saveToFile(driver.getPageSource(), new File(outFilename));
    }

    private static void saveToFile(String data, File outFile) {
        try (var out = new FileOutputStream(outFile)) {
            log.info("Saving {} bytes to {}", data.length(), outFile.getName());
            out.write(data.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var random = new Random(System.currentTimeMillis());
        var urls = List.of(
                Pair.of("https://nicheparfum.cz/en/women-s-perfumes/83-montale-sweet-flowers-edp.html", "p1"),
                Pair.of("https://www.amazon.com/dp/1587361086/ref=nosim?tag=qrshare05-20", "p2"),
                Pair.of("https://www.amazon.com/dp/1942788819/ref=nosim?tag=qrshare05-20", "p3"));
        var app = new WebFetcher();
        app.initDriver(false);
        for (var url : urls) {
            app.fetchAndSave(url.a, ".tmp/" + url.b + ".html");
            long delay = random.nextLong(5000);
            log.info("Sleeping for {}ms before the next request", delay);
            Thread.sleep(delay);
        }
        app.closeDriver();
    }
}

@AllArgsConstructor
class Pair<A, B> {
    public final A a;
    public final B b;

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }
}
