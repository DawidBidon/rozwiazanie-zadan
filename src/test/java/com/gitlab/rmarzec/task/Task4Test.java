package com.gitlab.rmarzec.task;

import com.gitlab.rmarzec.framework.utils.BaseTest;
import com.gitlab.rmarzec.framework.utils.HomePage;
import com.gitlab.rmarzec.model.YTTile;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class Task4Test  extends BaseTest {

    @Test
    public void Task4Test(){
        new HomePage(webDriver).goToTheWebsite("https://www.google.com/");
        new HomePage(webDriver).cookieAcceptance("Zaakceptuj wszystko");
        
        //Lista kafelkow
        List<YTTile> ytTileList = new ArrayList<YTTile>();
        
    }
}
