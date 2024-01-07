package com.ruchi;

import com.ruchi.ui.pages.ClerkDashboardPage;
import com.ruchi.ui.pages.LoginPage;
import com.ruchi.utils.CsvUtil;
import com.ruchi.utils.DBUtil;
import com.ruchi.utils.SeleniumUtil;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;


public class UserStory2Tests {

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
    @DisplayName("US2:AC1,AC2 and AC3 : Clerk can upload a csv file of required format to upload bulk working heroes from the UI")
    public void BulkUploadFromCSVTest_UI_AC1_AC2_AC3() throws IOException {
        util.navigateToPage("http://localhost:9997/login");

        LoginPage loginPage = new LoginPage(driver);
        ClerkDashboardPage clerkDashboardPage = loginPage.loginToApp("clerk", "clerk");
        clerkDashboardPage.openAddHeroMenu();
        clerkDashboardPage.selectUploadCsvFile();
        clerkDashboardPage.uploadCsvFile(CsvUtil.getSingleRecordCSV());
        clerkDashboardPage.clickCreateButton();
        String message = clerkDashboardPage.getNotificationMessage();
        Assertions.assertEquals("Created Successfully!", message);
        Allure.addAttachment("CSV Upload Message", message);

        File screenshotFile = new File(util.takeScreenshot(driver));
        Allure.addAttachment("Message Screenshot", new FileInputStream(screenshotFile));
    }

    @Test
    @DisplayName("US2:AC4 : Clerk can upload a csv file with erroneous records but the correct records should persist in DB excluding erroneous records")
    public void BulkUploadFromCSVTest_UI_AC4() throws IOException {
        util.navigateToPage("http://localhost:9997/login");

        LoginPage loginPage = new LoginPage(driver);
        ClerkDashboardPage clerkDashboardPage = loginPage.loginToApp("clerk", "clerk");
        clerkDashboardPage.openAddHeroMenu();
        clerkDashboardPage.selectUploadCsvFile();
        Map<String, String> multiRecordCsvDetails = CsvUtil.getMultiRecordCSVWithOneErroneousRecord();
        clerkDashboardPage.uploadCsvFile(multiRecordCsvDetails.get("FilePath"));
        clerkDashboardPage.clickCreateButton();
        String message = clerkDashboardPage.getNotificationMessage();
        File screenshotFile = new File(util.takeScreenshot(driver));
        Allure.addAttachment("Message Screenshot", new FileInputStream(screenshotFile));
        Assertions.assertTrue(message.contains("Unable to create hero!"));
        Assertions.assertTrue(message.contains("There are 1 records which were not persisted! Please contact tech support for help!"));
        Allure.addAttachment("CSV Upload Message", message);

        //Asserting the Erroneous Record should not persist in DB
        Assertions.assertFalse(isWorkingHeroExistInDB(multiRecordCsvDetails.get("ErroneousRecordId")), "Erroneous record persist in DB which is not expected");

        //Asserting the Valid Record should persist in DB
        Assertions.assertTrue(isWorkingHeroExistInDB(multiRecordCsvDetails.get("ValidRecordId")), "Correct record does not persist in DB which is not expected");
    }

    private boolean isWorkingHeroExistInDB(String natId){
        String sqlQueryForHero = "SELECT * FROM `working_class_heroes` where natid='" + natId + "';";
        ResultSet rs = DBUtil.getInstance().executeQuery(sqlQueryForHero);
        try {
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            Assertions.fail("Error while connecting to the DB : " + e.getMessage());
        }finally {
            return false;
        }
    }
}
