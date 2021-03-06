package pl.looksok.logic.test.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.logic.CalculationLogic;
import pl.looksok.logic.CalculationType;
import pl.looksok.logic.PersonData;
import pl.looksok.logic.exceptions.BadInputDataException;
import pl.looksok.logic.exceptions.DuplicatePersonNameException;
import pl.looksok.logic.exceptions.PaysNotCalculatedException;
import pl.looksok.logic.test.utils.Constants;
import pl.looksok.logic.test.utils.TestScenarioBuilder;

public class CcLogicEqualPaysTest extends TestCase {

	private CalculationLogic calc;
	private List<PersonData> inputPaysList;
	private boolean equalPayments = true;

	public CcLogicEqualPaysTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CalculationLogic();
		calc.setEqualPayments(equalPayments);
		calc.setCalculationType(CalculationType.POTLUCK_PARTY);
		inputPaysList = new ArrayList<PersonData>();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		calc = null;
		inputPaysList = null;
		super.tearDown();
	}
	
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testRefundOfZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(10.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayFewPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testReturnOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personBName).getToReturn());
	}
	
	public void testRefundOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void testRefundOfNonZeroPayFewPeopleFewPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(10.0, 5.0, 0.0);
		
		HashMap<String, PersonData> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personCName).getTotalRefundForThisPerson());
		
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personBName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personCName).getToReturn());
	}
	
	public void testThrowBadPayException(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(-10.0, 0.0);
			
			calc.calculate(inputPaysList);
			fail(Constants.SHOULD_THROW_EXCEPTION + BadInputDataException.class.getSimpleName());
		}catch(BadInputDataException e){}
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(15.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 0.0, 15.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(12.0, 3.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleThreePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(11.0, 3.0, 4.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 9.0, 9.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidNotEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(18.0, 6.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 8.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidNotEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(18.9, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 9.45, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testThrowExceptionIfNotCalculated(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + PaysNotCalculatedException.class.getSimpleName());
		}catch (PaysNotCalculatedException e){}
	}
	
	public void testThrowExceptionIfDuplicatePersonName(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeopleWithNames(Constants.personAName, 9.0, 
					Constants.personAName, 9.0, 
					Constants.personCName, 0.0);
			calc.calculate(inputPaysList);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + DuplicatePersonNameException.class.getSimpleName());
		}catch (DuplicatePersonNameException e){}
	}

}
