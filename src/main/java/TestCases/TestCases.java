package TestCases;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

//import PageMethods.SearchHotel;
//import PageMethods.SelectHotel;
import Utilities.Common;
import Utilities.GenericKeywords;
import Utilities.TestLinkConnection;
import Utilities.CommonPageMethods;
import dE.Page.Methods.BaseClassPage;
import dE.Page.Methods.PlanCanadaHomePage;


@Listeners({ Utilities.TestListener.class })
public class TestCases extends Common {

	public static int count = 1;
	private BaseClassPage planCanadaApplication;
	private PlanCanadaHomePage planCanadaHomePage;
	
	



	@BeforeClass
	public void start(){	
		GenericKeywords.extent.loadConfig(new File("./Config/extent-config.xml"));		
	}


	public void testStart(String testCaseDescription) {
		GenericKeywords.testFailure = false;
		GenericKeywords.currentStep = 0;
		reportStart(GenericKeywords.testCaseName,testCaseDescription);
		planCanadaApplication=new BaseClassPage();
		planCanadaHomePage=planCanadaApplication.openPlanCanadaApplication();

	}

	public void testEnd() {
		try {
			planCanadaApplication.close();			
		} catch (Exception e) {
			System.out.println("Expception : " + e.getMessage());
		}finally{
			GenericKeywords.extent.endTest(GenericKeywords.parent);
			GenericKeywords.extent.flush();	
			if(getConfigProperty("UpdateTestLinkStatus").toString().trim().toUpperCase().equals("YES")){
				if(GenericKeywords.testFailure){
					TestLinkConnection.updateExecutionStatus(GenericKeywords.testCaseName, "", "FAIL");					
				}else{
					TestLinkConnection.updateExecutionStatus(GenericKeywords.testCaseName, "passed", "PASS");
				}
			}
		}
	}


	@Test(alwaysRun = true)
	public void GOH_Single_Product_Mail_Card() {
		GenericKeywords.testCaseName = new Exception().getStackTrace()[0].getMethodName();
		testStart("GOH Single Product Mail Card");


			for (String testDataSet : GenericKeywords.testCaseDataSets) {
			GenericKeywords.testCaseDataRow = returnRowNumber(testDataSet);
			testStepInfoStart(testDataSet);
			Common.testStepPassed("Opened Application Successfully and Maximize The Browser");
			planCanadaHomePage.isValidPage();
			planCanadaHomePage.clickOnShopGiftsOfHope();
			planCanadaHomePage.clickOnPaginationAndSelectProduct();
			planCanadaHomePage.clickOnShopCart();
			planCanadaHomePage.clickOnContinue();
			planCanadaHomePage.selectCardDeliveryMethod();
			planCanadaHomePage.gohMailCardMessage();
			planCanadaHomePage.saveAndContinue();
			planCanadaHomePage.clickOnContinue();
			//planCanadaHomePage.clickOnContinueInActionCalss();
			planCanadaHomePage.enterContactInformationInCartPage();
			planCanadaHomePage.clickOnContinue();
			planCanadaHomePage.enterPaymentInformation();
			planCanadaHomePage.clickOnContinue();
			
			testStepInfoEnd(testDataSet);
		}
		testEnd();

	}


	


	

}
