package com.gitlab.rmarzec.framework.utils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GooglePage {

    private static final String SELECT_OPTION = "//input[@aria-label='%s']";
    private static final String SELECT_TASK_FROM_SIDEBAR = "//*[contains(@class, 'sidebar')]//*[contains(text(), '%s')]";
    private static final By TRY_YOURSELF_BUTTON = By.xpath("//a[contains(text(), 'Try it Yourself')]");
    private static final String HEADER_RESULT = "//h1[contains(text(), 'The select element')]/parent::body";
    private static final String CARS_LIST = "//select[@id='cars']";
    private static final String OPEL_BRAND = "//select[@id='cars']/option[text()='Opel']";

    private WebDriver webDriver;

    public GooglePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public GooglePage searchText(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
        searchInput.clear();
        searchInput.sendKeys(text);

        return this;
    }

    public GooglePage selectOption(String buttonName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        By buttonXpath = By.xpath(String.format(SELECT_OPTION, buttonName));

        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonXpath));
            button.click();
        } catch (Exception e) {
            throw new AssertionError("The button '" + buttonName + "' is not clickable or visible", e);
        }
        return this;
    }

    public GooglePage verifyTheAddressAndGoToTheCorrectOne(String expectedUrl) {
        String actualUrl = webDriver.getCurrentUrl();

        if (actualUrl.equals(expectedUrl)) {
            System.out.println("Correct address ! No forwarding: " + actualUrl);
        } else {
            System.out.println("The wrong address: " + actualUrl);
            System.out.println("Redirects to: " + expectedUrl);

            webDriver.get(expectedUrl);
        }
        return this;
    }

    public GooglePage runTheSelectedTask() {
        selectValueFromTheSidebar("<select>");
        clickTryItYourselfButton();

        return this;
    }

    public GooglePage selectValueFromTheSidebar(String taskName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        By task = By.xpath(String.format(SELECT_TASK_FROM_SIDEBAR, taskName));

        try {
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(task));
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            System.out.println("Select '" + taskName + "' task from sidebar");
        } catch (Exception e) {
            throw new AssertionError("It is not possible to select '" + taskName + "' task");
        }
        return this;
    }

    public GooglePage clickTryItYourselfButton(){
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(TRY_YOURSELF_BUTTON));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(button)).click();
            System.out.println("The 'try it yourself' has been clicked");
        } catch (Exception e) {
            throw new AssertionError("It is not possible to click the 'Try it Yourself' button", e);
        }
        return this;
    }

    public GooglePage saveTheHeaderContentAndPrintItInTheConsole() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        try {
            List<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
            webDriver.switchTo().window(tabs.get(tabs.size() - 1));
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("iframeResult")));

            WebElement windowContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(HEADER_RESULT)));
            String text = windowContent.getAttribute("innerText");

            System.out.println("Result 'The select element' is visible below: ");
            System.out.println("-------------------------");
            System.out.println(text);
        } catch (Exception e) {
            throw new AssertionError("The text is not visible");
        }
        return this;
    }

    public GooglePage selectCarAndPrintTheValueInTheConsole() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        try {
            webDriver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("iframeResult")));

            WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(CARS_LIST)));
            Select dropdown = new Select(selectElement);
            dropdown.selectByVisibleText("Opel");
            System.out.println("The Opel brand has been selected");

            WebElement selectBrand = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OPEL_BRAND)));
            String fullTag = selectBrand.getAttribute("outerHTML");
            System.out.println("Value of the Opel: ");
            System.out.println("-------------------------");
            System.out.println(fullTag);  // Dopsać do metody możliwość wyboru uzyskania wartości różnych marek, nie koniecznie Opla.
        } catch (Exception e) {
            throw new AssertionError("It is not possible to select Opel brand");
        }
        return this;
    }
}
