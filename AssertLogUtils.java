package com.vstl.generic;

import static com.vstl.config.ConfigurationManager.configuration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;
import com.vstl.data.dynamic.DynamicDataFactory;
import com.vstl.driver.DriverFactory;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

public class AssertLogUtils  {

	private SoftAssert softAssert=null;

	public AssertLogUtils() {
		softAssert = new SoftAssert();
	}

	/**
	 * @Method : logReporter
	 * @Description : Reporter method
	 * @param : Step - Step description, resultLog - result log pass/fail
	 *          (true/false), includeMobile - result for mobile(true/false)
	 * @author :Automation Tester
	 */
	@Step("{0}")
	public void logReporter(String step, boolean resultLog) {
		String strLog = step;
		this.addAssertTakeScreenShot(step, strLog, "", "", "", resultLog);
	}

	/**
	 * @Method : logReporter
	 * @Description : Reporter method
	 * @param : Step - Step description, inputValue - Input value, resultLog -
	 *          result log pass/fail (true/false), includeMobile - result for
	 *          mobile(true/false)
	 * @author : Automation Tester
	 */
	@Step("{0} - {1}")
	public void logReporter(String step, String inputValue, boolean resultLog) {
		String strLog = step + "|| Input Value : " + inputValue;
		this.addAssertTakeScreenShot(step, strLog, inputValue, "", "", resultLog);
	}

	/**
	 * @Method : logReporter
	 * @Description : Reporter method
	 * @param : Step - Step description, expectedValue - verification point expected
	 *          value, actualValue - verification point actual value, resultLog -
	 *          result log pass/fail (true/false), includeMobile - result for
	 *          mobile(true/false)
	 * @author :Automation Tester
	 */
	/*
	 * @Step("{0} - {1} - {2}") public void logReporter(String step, String
	 * expectedValue, String actualValue, boolean resultLog) { String strLog = step
	 * + " || Expected Result : " + expectedValue + " || Actual Result : " +
	 * actualValue; this.addAssertTakeScreenShot(step, strLog, "", expectedValue,
	 * actualValue, resultLog); }
	 */

	/**
	 * @Method : logReporter
	 * @Description : Reporter method
	 * @param : Step - Step description, expectedValue - verification point expected
	 *          value, actualValue - verification point actual value, resultLog -
	 *          result log pass/fail (true/false), includeMobile - result for
	 *          mobile(true/false)
	 * @author : VSTL-Developer
	 */
	@Step("Step Desc -> " + "{0} - {1} - {2}")
	public void logReporter(String step, String successMessage, String failureMessage, boolean resultLog) {
		String strSuccessLog = "";
		String strLog = step + " || Expected Result : " + successMessage + " || Actual Result : " + failureMessage;
		if (resultLog) {
			this.addStep("PASSED MESSAGE : " + successMessage);
			strSuccessLog = step + " || Expected Result : " + successMessage + " || Actual Result : " + successMessage;
			this.addAssertTakeScreenShot(step, strSuccessLog, "", successMessage, failureMessage, resultLog);
		} else {
			addStep("FAILURE MESSAGE : " + failureMessage);
			this.addAssertTakeScreenShot(step, strLog, "", successMessage, failureMessage, resultLog);
		}
	}

	@Step(" " + "{0}")
	public void addStep(String strMessage) {
	}
	
	@Step(" " + "{0}")
	public void verifyAllStepsAssertion(String strMessage) {
		String fileName = this.getDateInSpecifiedFormat("dd_MMM_yyyy_HH_mm_ss") + "_TCID_" + DriverFactory.getTestCaseID() + "_" + DriverFactory.getRunID()+"_"+DynamicDataFactory.getRandomNumberOfLength(6)+".png";
		String fileWithPath = System.getProperty("user.dir") + "\\target\\surefire-reports" + "\\ScreenShot\\" + "\\"
				+ DriverFactory.getTestCaseID() + "\\" + fileName;
		this.takeScreenShot(DriverFactory.getDriver(), fileWithPath);		
		softAssert.assertAll(strMessage);
	}
	
	/**
	 * @Method : addAssertTakeScreenShot
	 * @Description :
	 * @param :
	 * @author : Automation Tester
	 */
	public void addAssertTakeScreenShot(String step, String strLog, String inputValue, String expectedValue,
			String actualValue, boolean resultLog) {
		System.out.println("Step Description--> " + strLog);
		// final Logger logger = Logger.getLogger(Utilities.class);
		String fileName = this.getDateInSpecifiedFormat("dd_MMM_yyyy_HH_mm_ss") + "_TCID_"
				+ DriverFactory.getTestCaseID() + "_"+ DriverFactory.getRunID()+"_"+DynamicDataFactory.getRandomNumberOfLength(6)+".png";
		String fileWithPath = System.getProperty("user.dir") + "\\target\\surefire-reports" + "\\ScreenShot\\" + "\\"
				+ DriverFactory.getTestCaseID() + "\\" + fileName;
		boolean assertType = configuration().getAssertType();
		if (assertType) {
			if (resultLog) {
				Reporter.log("Step Description--> " + strLog);
				DriverFactory.getLogger().info("Step Description--> " + strLog);
				softAssert.assertTrue(true,strLog);
				DriverFactory.getObjFunctionFactory().waitFor(1);
				if(configuration().getEveryStepScreenshot()){
					this.takeScreenShot(DriverFactory.getDriver(), fileWithPath);
				}
			} else {
				Reporter.log("Step Description--> " + strLog);
				DriverFactory.getLogger().error("Step Description--> " + strLog);
				DriverFactory.getObjFunctionFactory().waitFor(1);
				this.takeScreenShot(DriverFactory.getDriver(), fileWithPath);
				softAssert.assertTrue(false,strLog);
			}
		} else {
			if (resultLog) {
				Reporter.log("Step Description--> " + strLog);
				DriverFactory.getLogger().info("Step Description--> " + strLog);
				Assert.assertTrue(true);
				DriverFactory.getObjFunctionFactory().waitFor(1);
				if(configuration().getEveryStepScreenshot()){
					this.takeScreenShot(DriverFactory.getDriver(), fileWithPath);
				}
			} else {
				Reporter.log("Step Description--> " + strLog);
				DriverFactory.getLogger().error("Step Description--> " + strLog);
				DriverFactory.getObjFunctionFactory().waitFor(1);
				this.takeScreenShot(DriverFactory.getDriver(), fileWithPath);
				Assert.assertEquals("ActualResult : " + actualValue, "ExpectedResult : " + expectedValue,
						"TEST FAILED ");
			}
		}
	}
	
	/**
	 * @Method : takeScreenShot
	 * @Description : Take Screen shot for given web driver.
	 * @author :Automation Tester .
	 * 
	 */
	public boolean takeScreenShot(WebDriver webDriver, String fileWithPath) {
		TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
		// Call getScreenshotAs method to create image file
		File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
		// Move image file to new destination
		File destFile = new File(fileWithPath);
		// Copy file at destination
		try {
			FileUtils.moveFile(srcFile, destFile);
			this.fileToByte(destFile);
			return true;
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return false;

		}
	}

//	public boolean takeScreenShot(WebDriver webDriver, String fileWithPath) {
//	    TakesScreenshot scrShot = ((TakesScreenshot) webDriver);
//	    // Call getScreenshotAs method to create image file
//	    File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
//	    // Move image file to new destination
//	    File destFile = new File(fileWithPath);
//	    // Convert File objects to Path objects
//	    Path srcPath = srcFile.toPath();
//	    Path destPath = destFile.toPath();
//	    // Copy file at destination
//	    try {
//	        Files.move(srcPath, destPath);
//	        this.fileToByte(destFile);
//	        return true;
//	    } catch (IOException iOException) {
//	        iOException.printStackTrace();
//	        return false;
//	    }
//	}
	
	/**
	 * @Method : fileToByte
	 * @Description : Converts image file to byte array for allure.
	 * @author :Automation Tester
	 * @throws : IOException
	 */
	@Attachment(value = "Screenshot", type = "image/png")
	private byte[] fileToByte(File file) throws IOException {
		if (file != null)
			return Files.readAllBytes(Paths.get(file.getPath()));
		else
			return new byte[0];
	}

	/*
	 * @Method : getDateInSpecifiedFormat
	 * 
	 * @Description : This method takes parameter of your required DateFormat Type
	 * Like: dd-mm-YYYY DD.MM.YYYY and in return it will give you today's date in
	 * specified date format
	 * 
	 * @param : dateFormat like : dd-MM-YYYY
	 * 
	 * @author : Framework Developer
	 * 
	 */
	public String getDateInSpecifiedFormat(String dateFormat) {
		String current_date = "";
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		current_date = formatter.format(today);
		// System.out.println("getDateInSpecifiedFormat "+dateFormat + " -
		// "+current_date);
		return current_date;
	}

	/**
	 * @Method : waitFor
	 * @Description : Waits for the specified amount of [timeInMilliseconds].
	 * @param :
	 *            timeUnitSeconds - wait time seconds
	 */
	public  boolean waitFor(int timeUnitSeconds) {
		try {
			Thread.sleep(TimeUnit.MILLISECONDS.convert(timeUnitSeconds, TimeUnit.SECONDS));
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("I got Exception: "+exception);
			return false;
		}
	}
	

}
