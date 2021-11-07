package utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import base.BaseClass;

public class Keywords extends BaseClass{

	
	
	public static int getAppResponseCode(String appurl) throws IOException {
		URL url = new URL(appurl);
	    HttpURLConnection huc = (HttpURLConnection)url.openConnection();
	    huc.setRequestMethod("GET");
	    huc.connect();
	    return huc.getResponseCode();
	}
	
	
	
	public static void attachScreenshotToReport(ExtentTest test) throws IOException {
		File srcFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		File dest=new File("src/../ErrImages/"+System.currentTimeMillis()+".png");
		String errFilePath=dest.getAbsolutePath();
		FileUtils.copyFile(srcFile, dest);
		
		test.addScreenCaptureFromPath(errFilePath);
	}
	
	public static int getBrokenLinks(String appurl) {
	
		String url = "";
		int brokenLinksCounter=0;
		HttpURLConnection huc = null;
		int respCode = 200;
		
		List<WebElement> links = driver.findElements(By.tagName("a"));

		Iterator<WebElement> it = links.iterator();

		while(it.hasNext()){

		url = it.next().getAttribute("href");

		//System.out.println(url);

		if(url == null || url.isEmpty()){
		//System.out.println("URL is either not configured for anchor tag or it is empty");
		continue;
		}

		if(!url.startsWith(appurl)){
		//System.out.println("URL belongs to another domain, skipping it.");
		continue;
		}
		
		try {
			huc = (HttpURLConnection)(new URL(url).openConnection());

			huc.setRequestMethod("HEAD");

			huc.connect();

			respCode = huc.getResponseCode();

			if(respCode >= 400){
			System.out.println(url+" is a broken link");
			brokenLinksCounter++;
			}
			else{
			//System.out.println(url+" is a valid link");
			}

			} catch (MalformedURLException e) {
			
			e.printStackTrace();
			} catch (IOException e) {
			
			e.printStackTrace();
			}
			}
		return brokenLinksCounter;
	}
}
