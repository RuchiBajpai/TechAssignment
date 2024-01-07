package com.ruchi.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class SeleniumUtil {
    private WebDriver driver;
    private String fileDownloadPath = System.getProperty("user.dir") + "/build/downloads";

    public WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        WebDriverManager.chromedriver().setup();

        try {
            File f = new File(fileDownloadPath);
            if(f.exists())
                f.deleteOnExit();
            Files.createDirectories(Paths.get(fileDownloadPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set the default download folder for Chrome
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", fileDownloadPath);
        prefs.put("browser.set_download_behavior", "{behavior : 'allow' , downloadPath: '"+ fileDownloadPath + "'}");

        options.setExperimentalOption("prefs", prefs);

        driver = new ChromeDriver(options);
        return driver;
    }

    public void navigateToPage(String URL) {
        driver.get(URL);
    }

    public String takeScreenshot(WebDriver driver) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotFolder = System.getProperty("user.dir") + "/build/screenshots";
        Files.createDirectories(Paths.get(screenshotFolder));
        String filePath = screenshotFolder + "/screenshot_" + timestamp + ".png";

        TakesScreenshot scrShot = ((TakesScreenshot)driver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(filePath);

        try {
            FileHandler.copy(SrcFile, DestFile);
            System.out.println("Screenshot saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return filePath;
        }
    }

    public void waitForNonEmptyText(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

        wait.until(new Function<WebDriver, Boolean>() {
            public Boolean apply(WebDriver driver) {
                String text = element.getText();
                return text != null && !text.isEmpty();
            }
        });
    }

    public String getDownloadPath(){
        return fileDownloadPath;
    }
}
