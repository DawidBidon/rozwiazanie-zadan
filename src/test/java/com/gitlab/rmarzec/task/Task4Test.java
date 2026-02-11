package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.model.YTTile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Task4Test  extends BaseTest {

    public String SELECT_TAB = "//ytd-mini-guide-renderer//ytd-mini-guide-entry-renderer[descendant::*[contains(text(), '%s')]]";
    public By DOWNLOAD_CHANEL_NAME = By.xpath("(//a[contains(@class, 'yt-core-attributed-string__link') and contains(@href, '/@')])[1]");

    @Test
    public void Task4Test(){
        new HomePage(webDriver).goToTheWebsite("https://www.youtube.com/");
        new HomePage(webDriver).cookieAcceptance("Zaakceptuj wszystko");

        selectTab("Shorts");
        downloadChanelName();


        
//        //Lista kafelkow
//        List<YTTile> ytTileList = new ArrayList<YTTile>();
        
    }

    public void selectTab (String tabName) {
        String finalXpath = String.format(SELECT_TAB, tabName);
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.tagName("tp-yt-iron-overlay-backdrop")));

        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(finalXpath)));
        tab.click();
    }

    public void downloadChanelName() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        try {
            WebElement channel = wait.until(ExpectedConditions.presenceOfElementLocated(DOWNLOAD_CHANEL_NAME));
            String chanelName = channel.getText();

            System.out.println("--------------------------------");
            System.out.println("The channel has been downloaded: " + chanelName);
            System.out.println("--------------------------------");
        } catch (Exception e) {
            throw new AssertionError("It is not possible to download the channel");
        }

    }



}
