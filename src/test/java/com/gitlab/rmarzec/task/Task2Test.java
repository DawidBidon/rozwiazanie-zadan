package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class Task2Test extends BaseTest {

    public By SELECT_LANGUAGE_BUTTON = By.id("p-lang-btn-checkbox");
    public By LANGUAGE_SELECTION_WINDOW = By.xpath("//div[contains(@class, 'row uls-language-list uls-lcd')]");
    public By ALL_LANGUAGES_FROM_THE_LIST = By.cssSelector("a[lang]");

    private Set<String> uniqueNames;
    private String url;

    @Test
    public void Task2Test() {
        new HomePage(webDriver).goToTheWebsite("https://pl.wikipedia.org/wiki/Wiki");

        listTheNamesOfTheLanguagesAndTheUrlForEnglish();
        checkIfTheNumberOfLanguagesAndTheUrlForEnglishIsCorrect();
    }

    public void listTheNamesOfTheLanguagesAndTheUrlForEnglish() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        boolean isOpened = false;
        int attempts = 0;

        while (attempts < 3 && !isOpened) {
            try {
                webDriver.findElement(SELECT_LANGUAGE_BUTTON).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(LANGUAGE_SELECTION_WINDOW));
                isOpened = true;
                System.out.println("The list of languages is visible (Attempt: " + (attempts + 1) + ")");
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed. Clicking again...");
            }
        }
        if (!isOpened) {
            throw new AssertionError("The list of languages did not appear after 3 attempts.");
        }

        WebElement listContainer = webDriver.findElement(LANGUAGE_SELECTION_WINDOW);
        List<WebElement> allLanguages = listContainer.findElements(ALL_LANGUAGES_FROM_THE_LIST);
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
    }

    public void checkIfTheNumberOfLanguagesAndTheUrlForEnglishIsCorrect() {
        Assert.assertEquals(uniqueNames.size(), 149, "The number of unique languages is incorrect");
        Assert.assertEquals(url, "https://en.wikipedia.org/wiki/Wiki", "The Url addres for English languages is incorrect");

        System.out.println("");
        System.out.println("-------------------------");
        System.out.println("The test passed!!");
    }
}
