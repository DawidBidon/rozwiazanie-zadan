package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class Task3Test {


    public String cookieAcceptanceButton = "//button[contains(., '%1$s')] | //*[contains(@aria-label, '%1$s')]";
    public String selectOption = "//input[@aria-label='%s']";


    private WebDriver webDriver;

    @Test
    public void Task3Test() {
        DriverFactory driverFactory = new DriverFactory();
        webDriver = driverFactory.initDriver();
        webDriver.get("https://www.google.com/ ");

        cookieAcceptance("Zaakceptuj wszystko");
        searchText("W3Schools");
        selectOption("Szczęśliwy traf");
        verifyTheAddressAndGoToTheCorrectOne("https://www.w3schools.com/tags/tag_select.asp ");
        cookieAcceptance("Potwierdź");


    }

    public void cookieAcceptance(String buttonName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        String xpath = String.format(cookieAcceptanceButton, buttonName);

        try {
            webDriver.switchTo().defaultContent();
            WebElement button = webDriver.findElement(By.xpath(xpath));
            if (button.isDisplayed()) {
                button.click();
                return;
            }
        } catch (Exception ignored) {
        }

        List<WebElement> iframes = webDriver.findElements(By.tagName("iframe"));

        for (int i = 0; i < iframes.size(); i++) {
            try {
                webDriver.switchTo().defaultContent();
                webDriver.switchTo().frame(i);

                WebElement button = webDriver.findElement(By.xpath(xpath));

                if (button.isDisplayed()) {
                    ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", button);
                    button.click();
                    System.out.println("The '" + buttonName + "' was clicked in frame number: " + i);
                    webDriver.switchTo().defaultContent();
                    return;
                }
            } catch (Exception nextFrame) {
            }
        }

        webDriver.switchTo().defaultContent();
        throw new AssertionError("The '" + buttonName + "' button was not found on the homepage or in any of " + iframes.size() + " frames.");
    }

    public void searchText(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));

        searchInput.clear();
        searchInput.sendKeys(text);
    }

    public void selectOption(String buttonName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        By buttonXpath = By.xpath(String.format(selectOption, buttonName));

        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonXpath));
            button.click();
        } catch (Exception e) {
            throw new AssertionError("The button '" + buttonName + "' is not clickable or visible", e);
        }
    }

    public void verifyTheAddressAndGoToTheCorrectOne(String expectedUrl) {
        String actualUrl = webDriver.getCurrentUrl();

        if (actualUrl.equals(expectedUrl)) {
            System.out.println("Correct address ! No forwarding: " + actualUrl);
        } else {
            System.out.println("The wrong address: " + actualUrl);
            System.out.println("Redirects to: " + expectedUrl);

            webDriver.get(expectedUrl);
        }
    }

    public void runTheSelectedTask() {
        selectValueFromTheList("<!-->");
        clickButton("Try it Yourself");

    }

    public void selectValueFromTheList(String taskName) {

    }

    public void clickButton(String buttonName) {

    }


}