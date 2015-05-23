package main.java.qa.framework.pageObjects;


import main.java.qa.framework.main.BaseUrls;
import main.java.qa.framework.main.TestBase;
import main.java.qa.framework.utils.PropertyUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class HboSignUp extends TestBase {
	
	public HboSignUp(WebDriver driver){
		super(driver); 
	}
	 
	  private static String voucher = PropertyUtils.getProperty("voucher");

	
	public HboSignUp selectOperator(String operator) throws Exception{
		
			driver.get(BaseUrls.PLAYER.get() + "/group/offers");
			Log.info(BaseUrls.PLAYER.get() + "/group/offers");

		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.id("headerButtonLogin"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
			driver.findElement(By.id("headerButtonLogin")).click();
			
			
		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.cssSelector("div.settings_description_white > #headerButtonLogin"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
		    driver.findElement(By.cssSelector("div.settings_description_white > #headerButtonLogin")).click();
		    
		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.id("OperatorId"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
		    driver.findElement(By.id("OperatorId")).click();
		    
		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.id(operator))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
		    driver.findElement(By.id(operator)).click();
		    
		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.name("SpecificData"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
		    driver.findElement(By.name("SpecificData")).clear();
		    driver.findElement(By.name("SpecificData")).sendKeys(voucher);
		    Log.info("Voucher is" + voucher);
		    
		    for (int second = 0;; second++) {
		    	if (second >= 60) fail("timeout");
		    	try { if (isElementPresent(By.cssSelector("button.button_submit.next"))) break; } catch (Exception e) {}
		    	Thread.sleep(1000);
		    }
		    driver.findElement(By.cssSelector("button.button_submit.next")).click();
		    		  		
		
		return new HboSignUp(driver); 
	}
	
}
