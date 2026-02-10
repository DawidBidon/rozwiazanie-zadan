package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Task3Test {

    public String cookieAcceptanceButton = "//button[contains(., '%1$s')] | //*[contains(@aria-label, '%1$s')]";
    public String selectOption = "//input[@aria-label='%s']";
    public String selectTaskFromSidebar = "//*[contains(@class, 'sidebar')]//*[contains(text(), '%s')]";
    public By tryYourselfButton = By.xpath("//a[contains(text(), 'Try it Yourself')]");
    public String resultHeader = "//h1[contains(text(), 'The select element')]/parent::body";
    public String carsList = "//select[@id='cars']";
    public String opelBrand = "//select[@id='cars']/option[text()='Opel']";

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
        runTheSelectedTask();
        saveTheHeaderContentAndPrintItInTheConsole();
        selectCarAndPrintTheValueInTheConsole();
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
//        selectValueFromTheSidebar("<!-->");
        clickTryItYourselfButton();

    }

    public void selectValueFromTheSidebar(String taskName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        By task = By.xpath(String.format(selectTaskFromSidebar, taskName));

        try {
            WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(task));
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
            System.out.println("Select '" + taskName + "' task from sidebar");
        } catch (Exception e) {
            throw new AssertionError("It is not possible to select '" + taskName + "' task");
        }
    }

        public void clickTryItYourselfButton(){
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(tryYourselfButton));

            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
                System.out.println("The 'try it yourself' has been clicked");
            } catch (Exception e) {
                throw new AssertionError("It is not possible to click the 'Try it Yourself' button", e);
            }
        }

        public void saveTheHeaderContentAndPrintItInTheConsole() {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

            try {
                List<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
                webDriver.switchTo().window(tabs.get(tabs.size() - 1));
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("iframeResult")));

                WebElement windowContent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(resultHeader)));
                String text = windowContent.getAttribute("innerText");

                System.out.println("Result 'The select element' is visible below: ");
                System.out.println("-------------------------");
                System.out.println(text);
            } catch (Exception e) {
                throw new AssertionError("The text is not visible");
            }
        }

        public void selectCarAndPrintTheValueInTheConsole() {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

            try {
                webDriver.switchTo().defaultContent();
                wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("iframeResult")));

                WebElement selectElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(carsList)));
                Select dropdown = new Select(selectElement);
                dropdown.selectByVisibleText("Opel");
                System.out.println("The Opel brand has been selected");

                WebElement selectBrand = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(opelBrand)));
                String fullTag = selectBrand.getAttribute("outerHTML");
                System.out.println("Value of the Opel: ");
                System.out.println("-------------------------");
                System.out.println(fullTag);  // Dopsać do metody możliwość wyboru uzyskania wartości różnych marek, nie koniecznie Opla.

            } catch (Exception e) {
                throw new AssertionError("It is not possible to select Opel brand");
            }
        }
}