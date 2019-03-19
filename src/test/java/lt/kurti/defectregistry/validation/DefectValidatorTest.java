package lt.kurti.defectregistry.validation;

import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.ID_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.MISSING_FIELDS;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.enumeration.DefectPriority;
import lt.kurti.defectregistry.domain.enumeration.DefectStatus;
import lt.kurti.defectregistry.web.rest.errors.InvalidRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefectValidatorTest {

	private static final String TEST_DESCRIPTION = "Test decription";
	private static final String TEST_NAME = "Test name";
	private static final Long DEFECT_ID = 1L;
	private static final String PRIORITY_FIELD = "priority";
	private static final String STATUS_FIELD = "status";

	private final DefectValidator defectValidator = new DefectValidator();

	private Defect defect = new Defect();

	@Before
	public void init() {
		defect.setDescription(TEST_DESCRIPTION);
		defect.setName(TEST_NAME);
		defect.setPriority(DefectPriority.HIGH);
		defect.setStatus(DefectStatus.NEW);
	}

	@Test
	public void testValidatePostRequestSuccess() {
		defectValidator.validateRequest(defect);
	}

	@Test
	public void testValidatePostRequestIdIsPresent() {
		defect.setId(DEFECT_ID);
		try {
			defectValidator.validateRequest(defect);
		} catch (InvalidRequestException e) {
			Assert.assertEquals(ID_CANNOT_BE_PRESENT, e.getMessage());
		}
	}

	@Test
	public void testValidatePostRequestPriorityNotPresent() {
		defect.setPriority(null);
		try {
			defectValidator.validateRequest(defect);
		} catch (InvalidRequestException e) {
			Assert.assertEquals(MISSING_FIELDS + PRIORITY_FIELD, e.getMessage());
		}
	}

	@Test
	public void testValidateUpdateRequestStatusNotPresent() {
		defect.setStatus(null);
		try {
			defectValidator.validateRequest(defect);
		} catch (InvalidRequestException e) {
			Assert.assertEquals(MISSING_FIELDS + STATUS_FIELD, e.getMessage());
		}
	}

}
