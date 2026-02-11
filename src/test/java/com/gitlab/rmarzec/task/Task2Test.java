package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.framework.utils.pages.WikipediaPage;
import org.testng.annotations.Test;

public class Task2Test extends BaseTest {

    @Test
    public void Task2Test() {

        new HomePage(webDriver).goToTheWebsite("https://pl.wikipedia.org/wiki/Wiki");
        new WikipediaPage(webDriver)
                .listTheNamesOfTheLanguagesAndTheUrlForEnglish()
                .checkIfTheNumberOfLanguagesAndTheUrlForEnglishIsCorrect();
    }
}
