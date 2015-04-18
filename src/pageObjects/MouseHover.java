package pageObjects;

import main.Clicks;
import main.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import testng.CustomException;

public class MouseHover extends TestBase {
	
	public MouseHover(WebDriver driver){
		super(driver); 
	}

	public MouseHover mouseHover() throws Exception, CustomException{
		
		Actions action = new Actions(driver);

		//driver.get("http://huvip.hbogo.eu/content/game-of-thrones-1-winter-is-coming-2012115219");
		
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresentAndDisplay(By.xpath(ContentDetail))) break; } catch (Exception e) {
    		Log.info(e.getCause());
    	} Thread.sleep(1000);
    }			
    	
		Thread.sleep(5000);
    
    	WebElement selectedplaygomb = driver.findElement(By.xpath(ContentDetail));
        
    	if (selectedplaygomb.isDisplayed()){
    		
    		//mouse hover action
    		action.moveToElement(selectedplaygomb).build().perform();
    		Log.info("Mouse hove action succeeded!");
    		
    		WebElement playbuttonmenu;
            playbuttonmenu = driver.findElement(By.id("play_dropdown"));
            action.moveToElement(playbuttonmenu);
           
            Clicks.clickEpisode();
                      
        }
    	
    	else if (!selectedplaygomb.isDisplayed()){
    		throw new CustomException();
    	}
    	
    	
    	//assert if the playback got under way within 20 seconds
    	TestBase.isPlayBackRunning(2, 20000, true);
	
    return new MouseHover(driver); 	
	
	}
}


