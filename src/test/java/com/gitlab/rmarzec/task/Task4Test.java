package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.framework.utils.pages.YouTubePage;
import org.testng.annotations.Test;

public class Task4Test  extends BaseTest {

    @Test
    public void Task4Test() {

        new HomePage(webDriver).goToTheWebsite("https://www.youtube.com/");
        new HomePage(webDriver).cookieAcceptance("Zaakceptuj wszystko");
        new YouTubePage(webDriver)
                .selectTab("Shorts")
                .downloadChanelName()
                .selectTab("Główna")
                .searchByPhraseAndListTheResults("Live");
    }
}
