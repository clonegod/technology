package sample.winio;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import clonegod.crawler.winio.jna.VirtualKeyBoard64;

public class WinIOWithSeleniumTest {
	
	WebDriver driver;  
	
	final String 深圳 = "https://nbp.szzfgjj.com/newui/login.jsp?transcode=pri";
	final String 武汉 = "https://whgjj.hkbchina.com/portal/pc/login.html";
	final String 天津 = "https://cx.zfgjj.cn/dzyw-grwt/index.do";
	
	String HOST = 深圳;
	
	@Before
	public void init() {
		// 指定selenium使用32位IE驱动
		System.setProperty("webdriver.ie.driver", "C:/Program Files (x86)/Internet Explorer/IEDriverServer.exe");
		
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		
		driver = new InternetExplorerDriver(ieCapabilities);
	}
	
	
	@Test
	public void test_Shenzhen() throws Exception {
		driver.get(HOST);
		
		waitForLoad(By.name("CardNo"), 5);
		
		// 输入账号
		WebElement telnoInput = driver.findElement(By.name("CardNo"));
		telnoInput.sendKeys("123456789abc");
		
		// 定位光标移到下一个输入框
		telnoInput.sendKeys(Keys.TAB);
		// 输入密码
		VirtualKeyBoard64.KeyPress("123abc");
		
		// 输入验证码
		WebElement verifyCodeInput = driver.findElement(By.name("identifyCode"));
		verifyCodeInput.sendKeys("1a2c");
		
		TimeUnit.SECONDS.sleep(10);
		
		driver.close();
	}
	
    public void waitForLoad(final By elementBy, int timeoutInSeconds) {  
        WebDriverWait wait = (new WebDriverWait(driver, timeoutInSeconds));  
        wait.until(new ExpectedCondition<Boolean>() {  
            public Boolean apply(WebDriver d) {  
                boolean loadcomplete = d.findElement(elementBy).isDisplayed();  
                return loadcomplete;  
            }  
        });  
    }  
	
}
