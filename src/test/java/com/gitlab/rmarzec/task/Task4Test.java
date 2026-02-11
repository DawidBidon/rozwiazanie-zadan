package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.model.YTTile;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Task4Test  extends BaseTest {

    private static final String SELECT_TAB = "//ytd-mini-guide-renderer//ytd-mini-guide-entry-renderer[descendant::*[contains(text(), '%s')]]";
    private static final By DOWNLOAD_CHANEL_NAME = By.xpath("(//a[contains(@class, 'yt-core-attributed-string__link') and contains(@href, '/@')])[1]");
    private static final By SEARCH_INPUT = By.cssSelector("input.yt-searchbox-input[name='search_query']");
    private static final By VIDEO_ROWS = By.xpath("//ytd-item-section-renderer//ytd-video-renderer");
    private static final By SEARCH_RESULTS_CONTAINER = By.cssSelector("ytd-section-list-renderer #contents");
    private static final By VIDEO_TITLE = By.id("video-title");
    private static final By CHANNEL_NAME = By.xpath(".//ytd-channel-name//a");
    private static final By LIVE_BADGE = By.cssSelector("badge-shape[aria-label='NA ŻYWO']");
    private static final By VIDEO_DURATION_LABEL = By.cssSelector("span.ytd-thumbnail-overlay-time-status-renderer");

    @Test
    public void Task4Test() {
        new HomePage(webDriver).goToTheWebsite("https://www.youtube.com/");
        new HomePage(webDriver).cookieAcceptance("Zaakceptuj wszystko");

        selectTab("Shorts");
        downloadChanelName();
        selectTab("Główna");
        searchByPhraseAndListTheResults("Live");


    }

    public void selectTab(String tabName) {
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

    public void searchByPhraseAndListTheResults(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
        searchInput.clear();
        searchInput.sendKeys(text);
        searchInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(SEARCH_RESULTS_CONTAINER));

        List<WebElement> videoElements = webDriver.findElements(VIDEO_ROWS);
        while (videoElements.size() < 12) {
            int countBeforeScroll = videoElements.size();
            js.executeScript("window.scrollBy(0, 2000)");
            try {
                wait.until(d -> webDriver.findElements(VIDEO_ROWS).size() > countBeforeScroll);
            } catch (TimeoutException e) {
                break;
            }
            videoElements = webDriver.findElements(VIDEO_ROWS);
        }

        List<YTTile> ytTileList = new ArrayList<>();
        for (int i = 0; i < Math.min(12, videoElements.size()); i++) {
            WebElement video = videoElements.get(i);
            YTTile tile = new YTTile();

            String title = video.findElement(VIDEO_TITLE).getText();
            tile.setTitle(title);
            String channel = video.findElement(CHANNEL_NAME).getText();
            tile.setChannel(channel);

            try {
                List<WebElement> liveBadges = video.findElements(LIVE_BADGE);
                if (!liveBadges.isEmpty()) {
                    tile.setLength("live");
                } else {
                    WebElement timeElement = video.findElement(VIDEO_DURATION_LABEL);
                    String duration = timeElement.getText().trim();
                    if (duration.isEmpty()) {
                        duration = timeElement.getAttribute("aria-label");
                    }
                    tile.setLength(duration);
                }
            } catch (Exception e) {
                tile.setLength("live");
            }
            ytTileList.add(tile);
        }

        System.out.println("\n=== The list of downloaded movies ===");
        for (int i = 0; i < ytTileList.size(); i++) {
            YTTile item = ytTileList.get(i);
            System.out.println((i + 1) + ". Title: " + item.getTitle());
            System.out.println("   Chanel: " + item.getChannel());
            System.out.println("   Duration: " + item.getLength());
            System.out.println("-----------------------------------");
        }

        System.out.println("\n=== Videos not broadcast live ===");
        for (YTTile item : ytTileList) {
            String length = item.getLength().toUpperCase();
            boolean isActuallyLive = length.equals("LIVE") || length.contains("ŻYWO") || length.contains("NA ŻYWO");

            if (!isActuallyLive) {
                System.out.println("Title: " + item.getTitle());
                System.out.println("Duration: " + item.getLength());
                System.out.println("-----------------------------------");
            }
        }
    }
}
