package dE.Page.Methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

import Utilities.Common;
import Utilities.GenericKeywords;



public class BaseClassPage {

	private WebDriver browser;
	private String url;	
	private String env;
	//private Page page;

	public BaseClassPage() {	
		String strbrowser = "";		
		String strJenkinsBrowser = System.getProperty("browser");
		if(strJenkinsBrowser != null) {
			strbrowser = strJenkinsBrowser;
		}else{
			strbrowser = Common.getConfigProperty("Browser");
		}
		browser=getDriver(strbrowser);
		this.browser = browser;
		String strJenkinsAppUrl = System.getProperty("url");
		if(strJenkinsAppUrl != null) {
			this.url = strJenkinsAppUrl;
		}else{
			this.url = Common.getConfigProperty("url");
		}
		GenericKeywords.driver = browser;
		GenericKeywords.extent.addSystemInfo("Browser", Common.getConfigProperty("Browser"));
		GenericKeywords.extent.addSystemInfo("URL", Common.getConfigProperty("url"));
	}

	//==================='
	public PlanCanadaHomePage openPlanCanadaApplication() {
		try {
			browser.get(url);
			browser.manage().window().maximize();
			//Common.testStepInfo("Opened Application Successfully and Maximize The Browser");			
		} catch (Exception e) {			
			//Common.testStepInfo("Unable to Open Application");
		}
		return new  PlanCanadaHomePage(browser);

	}

	//=================================================================

	/**
	 * method to make a thread sleep for customized time in milliseconds
	 * @param milliseconds
	 */
	protected void sleep(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void deleteAllCookies() {
		try{
			browser.manage().deleteAllCookies();
		}
		catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}


	/**
	 * Get the browser object specified in the property
	 * @param browserName
	 * @return
	 */
	protected WebDriver getDriver(String browserName) {
		WebDriver driver = null;
		if (browserName.equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.gecko.driver", 
					getRelativePath()+"/ext/BrowserSpecificDrivers/geckodriver.exe");								
			driver = new FirefoxDriver();

		}
		if(browserName.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", 
					getRelativePath()+"/ext/BrowserSpecificDrivers/chromedriver.exe");

			driver=new ChromeDriver();
		}
		if(browserName.equalsIgnoreCase("iexplore") || browserName.equalsIgnoreCase("ie")){
			System.setProperty("webdriver.ie.driver",
					getRelativePath()+"/ext/BrowserSpecificDrivers/IEDriverServer.exe");
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			//capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);

			capabilities.setCapability("ACCEPT_SSL_CERTS", true);
			driver=new InternetExplorerDriver(capabilities);

		}
		if(browserName.equalsIgnoreCase("safari")){			
			driver=new SafariDriver();
		}
//		if(browserName.equalsIgnoreCase("htmlunitdriver")){			
//			driver = new HtmlUnitDriver(true);
//		}
		if(browserName.equalsIgnoreCase("ChromeHeadless")){			
			System.setProperty("webdriver.chrome.driver", 
					getRelativePath()+"/ext/BrowserSpecificDrivers/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();			
			options.addArguments("--headless");  
			//options.addArguments("--disable-gpu");
			driver=new ChromeDriver(options);
		}


		return driver;
	}


	/**
	 * Get relative path of the framework
	 * @return
	 */
	public static String getRelativePath() {
		String relativePath = new File(System.getProperty("user.dir"))
		.getAbsolutePath();
		if (relativePath.endsWith("bin")) {
			relativePath = new File(System.getProperty("user.dir")).getParent();
		}
		return relativePath;
	}

	public void close() throws Exception {
		try{
			deleteAllCookies();
			browser.quit();
			if(GenericKeywords.testFailure){
				Assert.fail("Testcasefailed");
			}
		}catch(Exception Ex){
			System.out.println("Unable to Close Application");
		}		
	}

	private static final String TASKLIST = "tasklist";
	private static final String KILL = "taskkill /IM ";

	public static boolean isProcessRunging(String serviceName) throws Exception {

		Process p = Runtime.getRuntime().exec(TASKLIST);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.contains(serviceName)) {
				return true;
			}
		}

		return false;

	}

	public static void killProcess(String serviceName) throws Exception {

		Runtime.getRuntime().exec(KILL + serviceName);

	}
}
