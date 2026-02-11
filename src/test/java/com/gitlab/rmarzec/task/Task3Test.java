package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.framework.utils.pages.GooglePage;
import org.testng.annotations.Test;

public class Task3Test extends BaseTest {

    @Test
    public void Task3Test() {

        new HomePage(webDriver).goToTheWebsite("https://www.google.com/");
        new HomePage(webDriver).cookieAcceptance("Zaakceptuj wszystko");
        new GooglePage(webDriver)
                .searchText("W3Schools")
                .selectOption("Szczęśliwy traf")
                .verifyTheAddressAndGoToTheCorrectOne("https://www.w3schools.com/tags/tag_select.asp ");
        new HomePage(webDriver).cookieAcceptance("Potwierdź");
        new GooglePage(webDriver)
                .runTheSelectedTask()
                .saveTheHeaderContentAndPrintItInTheConsole()
                .selectCarAndPrintTheValueInTheConsole();
    }
}
