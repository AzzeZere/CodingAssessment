package testcases;

import java.io.IOException;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import utils.ExtentManager;
import utils.Keywords;

public class TestCase3 extends Keywords{

	ExtentReports extent;
	ExtentTest test;
	
	@BeforeTest
	public void setUp() {
		initDriver();
		extent = ExtentManager.GetExtent();
	}

	@Test
	public void thirdTest() throws IOException {
		test=extent.createTest("Test For - https://www.w3.org/standards/webdesign/htmlcss");
		test.log(Status.INFO, "Browser launched");
		String url="https://www.w3.org/standards/webdesign/htmlcss";
		
		//check response code for the page
				System.out.println("App response code - "+getAppResponseCode(url));
				Assert.assertTrue(getAppResponseCode(url)==200);
				test.log(Status.INFO, "Response Code from the Page validated successfully ");
				
				//navigate to application url
				driver.get(url);
				
				//check console logs
				LogEntries logs=driver.manage().logs().get("browser");
				for(LogEntry entry:logs) {
					System.out.println(entry.getMessage());
					if(entry.getMessage().contains("Failed"))
						Assert.assertTrue(false);
				}
				test.log(Status.INFO, "Console errors for the Page validated successfully");
				
				//check app links and their navigation
				System.out.println("Broken links - "+getBrokenLinks(url));
				Assert.assertTrue(getBrokenLinks(url)==0);
				test.log(Status.INFO, "No Broken links for the page exists");
	}
	
	@AfterMethod
	public void getResult(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE) {
					
					test.log(Status.FAIL, "Test Failed with error as -> "+result.getThrowable());
					
				//attaching the error screenshot to the report
					attachScreenshotToReport(test);
					
				}else {
					test.log(Status.PASS, "Test Passed successfully");
					attachScreenshotToReport(test);
				}
	}
	
	@AfterTest
	public void tearDown() {
		extent.flush();
		driver.quit();
	}
	
}
