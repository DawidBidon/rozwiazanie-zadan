package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.DriverFactory;
import com.gitlab.rmarzec.framework.utils.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class Task2Test extends BaseTest {

    public By selectLanguageButton = By.id("p-lang-btn-checkbox");
    public By languageSelectionWindow = By.xpath("//div[contains(@class, 'row uls-language-list uls-lcd')]");
    public By allLanguagesFromTheList = By.cssSelector("a[lang]");

    private Set<String> uniqueNames;
    private String url;

    @Test
    public void Task2Test() {
        new HomePage(webDriver).goToTheWebsite("https://pl.wikipedia.org/wiki/Wiki");

        listTheNamesOfTheLanguagesAndTheUrlForEnglish();
        checkIfTheNumberOfLanguagesAndTheUrlForEnglishIsCorrect();
    }

    public void listTheNamesOfTheLanguagesAndTheUrlForEnglish() {
        webDriver.findElement(selectLanguageButton).click();

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        try {
            WebElement listContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(languageSelectionWindow));
            System.out.println("The list of languages is visible");

            List<WebElement> allLanguages = listContainer.findElements(allLanguagesFromTheList);
            System.out.println("All found languages: " + allLanguages.size());

            uniqueNames = new java.util.LinkedHashSet<>();
            for (WebElement language : allLanguages) {
                String name = language.getText().trim();
                if (!name.isEmpty()) {
                    uniqueNames.add(name);
                }
            }

            System.out.println("Final unique languages count: " + uniqueNames.size());
            System.out.println("-------------------------");

            Set<String> printedNames = new java.util.HashSet<>();
            for (WebElement language : allLanguages) {
                String name = language.getText().trim();

                if (!name.isEmpty() && !printedNames.contains(name)) {
                    System.out.println("Język: " + name);
                    printedNames.add(name);

                    if (name.equalsIgnoreCase("English")) {
                        url = language.getAttribute("href");
                        System.out.println("   --> URL address for English: " + url);
                    }
                }
            }
        } catch (TimeoutException e) {
            throw new AssertionError("The list of languages is not visible", e);
        }
    }

    public void checkIfTheNumberOfLanguagesAndTheUrlForEnglishIsCorrect() {
        Assert.assertEquals(uniqueNames.size(), 149, "The number of unique languages is incorrect");
        Assert.assertEquals(url, "https://en.wikipedia.org/wiki/Wiki", "The Url addres for English languages is incorrect");

        System.out.println("");
        System.out.println("-------------------------");
        System.out.println("The test passed!!");
    }
}
