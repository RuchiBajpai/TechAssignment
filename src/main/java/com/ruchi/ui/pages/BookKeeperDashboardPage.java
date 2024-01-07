package com.ruchi.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BookKeeperDashboardPage {
    WebDriver driver;

    @FindBy(id = "tax_relief_btn")
    WebElement generateTaxReliefFileButton;

    //tax_relief_status_id
    //Egress Tax Relief file process in progress

    public BookKeeperDashboardPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step
    public void clickOnGenerateTaxReliefFile() {
        Actions action = new Actions(driver);
        action.moveToElement(generateTaxReliefFileButton).click().build().perform();
    }

    // Additional methods for other interactions as necessary
}

