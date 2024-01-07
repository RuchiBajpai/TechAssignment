package com.ruchi.ui.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;

    @FindBy(id = "username-in")
    WebElement usernameField;

    @FindBy(id = "password-in")
    WebElement passwordField;

    @FindBy(css = "input[type='submit']")
    WebElement loginButton;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setUsername(String strUsername){
        usernameField.sendKeys(strUsername);
    }

    public void setPassword(String strPassword){
        passwordField.sendKeys(strPassword);
    }

    public void clickLogin(){
        loginButton.click();
    }

    @Step
    public ClerkDashboardPage loginToApp(String strUsername, String strPassword){
        this.setUsername(strUsername);
        this.setPassword(strPassword);
        this.clickLogin();
        return new ClerkDashboardPage(driver);
    }
}
