package com.ruchi.ui.pages;

import com.ruchi.utils.SeleniumUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ClerkDashboardPage {
    WebDriver driver;

    @FindBy(id = "dropdownMenuButton2")
    WebElement addHeroMenu;

    @FindBy(xpath = "//a[contains(@href, '/clerk/upload-csv')]")
    WebElement uploadCsvFileOption;

    @FindBy(id = "upload-csv-file")
    WebElement uploadCsvField;

    @FindBy(xpath = "//button[contains(@onclick, 'uploadCsv')]")
    WebElement createButton;

    @FindBy(id = "notification-block")
    WebElement notificationBlock;



    public ClerkDashboardPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step
    public void openAddHeroMenu(){
        addHeroMenu.click();
    }

    @Step
    public void selectUploadCsvFile(){
        uploadCsvFileOption.click();
    }

    @Step
    public void uploadCsvFile(String filePath){
        uploadCsvField.sendKeys(filePath);
    }

    @Step
    public void clickCreateButton(){
        createButton.click();
    }

    @Step
    public String getNotificationMessage(){
        SeleniumUtil seleniumUtil = new SeleniumUtil();
        seleniumUtil.waitForNonEmptyText(driver,notificationBlock);
        return notificationBlock.getText();
    }
}
