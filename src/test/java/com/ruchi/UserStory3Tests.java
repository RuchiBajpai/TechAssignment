package com.ruchi;

import com.ruchi.ui.pages.BookKeeperDashboardPage;
import com.ruchi.ui.pages.ClerkDashboardPage;
import com.ruchi.ui.pages.LoginPage;
import com.ruchi.utils.CsvUtil;
import com.ruchi.utils.SeleniumUtil;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserStory3Tests {
    private WebDriver driver;
    private SeleniumUtil util;

    @BeforeEach
    public void beforeTest(){
        util = new SeleniumUtil();
        driver = util.getDriver();
    }

    @AfterEach
    public void afterTest(){
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("US3:AC1 : generate a tax relief egress file")
    public void GenerateTaxReliefEgressFileTest_UI_AC1() throws IOException {
        util.navigateToPage("http://localhost:9997/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApp("bk", "bk");

        BookKeeperDashboardPage bookKeeperDashboardPage = new BookKeeperDashboardPage(driver);

        Path taxReliefFilePath =  Paths.get(util.getDownloadPath() + "/" + "tax_relief_file.txt");

        //Deleting the old file in case it is present
        Files.deleteIfExists(taxReliefFilePath);

        //Downloading the file
        bookKeeperDashboardPage.clickOnGenerateTaxReliefFile();

        File taxReliefFile = new File(taxReliefFilePath.toString());
        boolean fileExist = waitForFileDownload(taxReliefFilePath.toString(), 5);
        if(!fileExist){
            File screenshotFile = new File(util.takeScreenshot(driver));
            Allure.addAttachment("Error Screenshot", new FileInputStream(screenshotFile));
        }
        Assertions.assertTrue(fileExist, "Tax Relief File is not downloaded.");

        String fileContent = Files.readString(taxReliefFilePath);

        Allure.addAttachment("Tax Relief File Content", fileContent);

        Assertions.assertTrue(assertFileContent(taxReliefFilePath.toString()), "Tax relief file content format is not correct.");
    }

    public static boolean waitForFileDownload(String filePath, int timeoutInSeconds) {
        File file = new File(filePath);
        int waited = 0;
        try {
            while (!file.exists() || file.length() == 0) {
                Thread.sleep(1000); // Wait for 1 second
                waited++;
                if (waited > timeoutInSeconds) {
                    return false; // File didn't download within the timeout
                }
            }
            return true; // File downloaded
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public boolean assertFileContent(String filePath) {
        int lineCount = 0;
        int footerValue = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if it's not the last line (footer)
                if (reader.ready()) {
                    lineCount++;
                } else {
                    footerValue = Integer.parseInt(line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        // Compare the counted lines with the footer value
        return lineCount == footerValue;
    }
}
