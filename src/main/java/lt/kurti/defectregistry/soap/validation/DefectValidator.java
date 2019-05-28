package lt.kurti.defectregistry.soap.validation;



import static lt.kurti.defectregistry.soap.Constants.ERROR_CODE;
import static lt.kurti.defectregistry.soap.Constants.INVALID_REQUEST;
import static lt.kurti.defectregistry.soap.Constants.NOT_FOUND;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.CREATION_DATE_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.ID_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.ID_MUST_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_FIELDS;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_PRIORITY_NAME;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_PUT_REQUESTS;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_STATUS_NAME;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.MISSING_FIELDS;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.MODIFICTIONATION_DATE_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.wsdl.DefectPriority.HIGH;
import static lt.kurti.defectregistry.wsdl.DefectPriority.LOW;
import static lt.kurti.defectregistry.wsdl.DefectPriority.MEDIUM;
import static lt.kurti.defectregistry.wsdl.DefectStatus.IN_PROGRESS;
import static lt.kurti.defectregistry.wsdl.DefectStatus.NEW;
import static lt.kurti.defectregistry.wsdl.DefectStatus.REJECTED;
import static lt.kurti.defectregistry.wsdl.DefectStatus.RESOLVED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lt.kurti.defectregistry.soap.exceptions.ServiceFault;
import lt.kurti.defectregistry.soap.exceptions.ServiceFaultException;
import lt.kurti.defectregistry.web.rest.errors.InvalidRequestException;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.DefectPriority;
import lt.kurti.defectregistry.wsdl.DefectStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("soapDefectValidator")
public class DefectValidator {

	private static final String NAME_FIELD = "name";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String PRIORITY_FIELD = "priority";
	private static final String STATUS_FIELD = "status";
	private static final String COMMA_DELIMITER = ", ";

	private List<DefectPriority> defectPriorityList = Arrays.asList(LOW, MEDIUM, HIGH);
	private List<DefectStatus> defectStatusList = Arrays.asList(NEW, IN_PROGRESS, RESOLVED, REJECTED);

	public void validatePatchRequest(final Defect defect) {
		if (defect.getId() == 0) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(INVALID_REQUEST, ID_MUST_BE_PRESENT));
		}
		if (!StringUtils.isEmpty(defect.getDateCreated())) {
			throw new InvalidRequestException(CREATION_DATE_CANNOT_BE_PRESENT);
		}
		if (!StringUtils.isEmpty(defect.getDateUpdated())) {
			throw new InvalidRequestException(MODIFICTIONATION_DATE_CANNOT_BE_PRESENT);
		}

		if (StringUtils.isEmpty(defect.getName()) && StringUtils.isEmpty(defect.getStatus()) && StringUtils.isEmpty(defect.getPriority())
				&& StringUtils.isEmpty(defect.getDescription())) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(INVALID_REQUEST, INVALID_PUT_REQUESTS));
		}
	}

	public void validateRequest(final Defect defect) {
		if (defect.getId() != null) {
			throw new InvalidRequestException(ID_CANNOT_BE_PRESENT);
		}

		validateDefectBody(defect);
	}

	public void validateUpdateRequest(final Defect defect) {
		if (defect.getId() == null) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(INVALID_REQUEST, ID_MUST_BE_PRESENT));
		}
		validateDefectBody(defect);
	}

	private void validateDefectBody(final Defect defect) {
		validateEmptyFields(defect);
		validateInvalidFields(defect);
	}

	private void validateInvalidFields(final Defect defect) {
		defectPriorityList.stream()
				.filter(priority -> priority.equals(defect.getPriority()))
				.findAny()
				.orElseThrow(() -> new InvalidRequestException(INVALID_PRIORITY_NAME));

		defectStatusList.stream()
				.filter(status -> status.equals(defect.getStatus()))
				.findAny()
				.orElseThrow(() -> new InvalidRequestException(INVALID_STATUS_NAME));
	}

	private void validateEmptyFields(final Defect defect) {
		if (!StringUtils.isEmpty(defect.getDateCreated())) {
			throw new InvalidRequestException(CREATION_DATE_CANNOT_BE_PRESENT);
		}
		if (!StringUtils.isEmpty(defect.getDateUpdated())) {
			throw new InvalidRequestException(MODIFICTIONATION_DATE_CANNOT_BE_PRESENT);
		}

		final String invalidFieldNames = collectInvalidFieldNames(defect);

		if (!StringUtils.isEmpty(invalidFieldNames)) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(INVALID_REQUEST, INVALID_FIELDS + invalidFieldNames));
		}
	}

	private String collectInvalidFieldNames(final Defect defect) {
		final List<String> fields = new ArrayList<>();

		if (StringUtils.isEmpty(defect.getName())) {
			fields.add(NAME_FIELD);
		}
		if (StringUtils.isEmpty(defect.getDescription())) {
			fields.add(DESCRIPTION_FIELD);
		}
		if (StringUtils.isEmpty(defect.getPriority())) {
			fields.add(PRIORITY_FIELD);
		}
		if (StringUtils.isEmpty(defect.getStatus())) {
			fields.add(STATUS_FIELD);
		}

		return String.join(COMMA_DELIMITER, fields);
	}

}
