package com.gitlab.rmarzec.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage {

    public String cookieAcceptanceButton = "//button[contains(., '%1$s')] | //*[contains(@aria-label, '%1$s')]";

    private WebDriver webDriver;


    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void goToTheWebsite(String url) {
        webDriver.get(url);
    }

    public void cookieAcceptance(String buttonName) {
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
                    System.out.println("The '" + buttonName + "' button was clicked in frame number: " + i);
                    webDriver.switchTo().defaultContent();
                    return;
                }
            } catch (Exception nextFrame) {
            }
        }

        webDriver.switchTo().defaultContent();
        throw new AssertionError("The '" + buttonName + "' button was not found on the homepage or in any of " + iframes.size() + " frames.");
    }


}
