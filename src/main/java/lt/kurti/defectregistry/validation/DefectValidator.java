package lt.kurti.defectregistry.validation;

import static lt.kurti.defectregistry.domain.enumeration.DefectPriority.HIGH;
import static lt.kurti.defectregistry.domain.enumeration.DefectPriority.LOW;
import static lt.kurti.defectregistry.domain.enumeration.DefectPriority.MEDIUM;
import static lt.kurti.defectregistry.domain.enumeration.DefectStatus.IN_PROGRESS;
import static lt.kurti.defectregistry.domain.enumeration.DefectStatus.NEW;
import static lt.kurti.defectregistry.domain.enumeration.DefectStatus.REJECTED;
import static lt.kurti.defectregistry.domain.enumeration.DefectStatus.RESOLVED;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.CREATION_DATE_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.ID_CANNOT_BE_PRESENT;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_PRIORITY_NAME;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_PUT_REQUESTS;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.INVALID_STATUS_NAME;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.MISSING_FIELDS;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.MODIFICTIONATION_DATE_CANNOT_BE_PRESENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.enumeration.DefectPriority;
import lt.kurti.defectregistry.domain.enumeration.DefectStatus;
import lt.kurti.defectregistry.web.rest.errors.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefectValidator {

	private static final String NAME_FIELD = "name";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String PRIORITY_FIELD = "priority";
	private static final String STATUS_FIELD = "status";
	private static final String COMMA_DELIMITER = ", ";

	private List<DefectPriority> defectPriorityList = Arrays.asList(LOW, MEDIUM, HIGH);
	private List<DefectStatus> defectStatusList = Arrays.asList(NEW, IN_PROGRESS, RESOLVED, REJECTED);

	public void validatePatchRequest(final Defect defect) {
		if (defect.getId() != null) {
			throw new InvalidRequestException(ID_CANNOT_BE_PRESENT);
		}
		if (!StringUtils.isEmpty(defect.getDateCreated())) {
			throw new InvalidRequestException(CREATION_DATE_CANNOT_BE_PRESENT);
		}
		if (!StringUtils.isEmpty(defect.getDateUpdated())) {
			throw new InvalidRequestException(MODIFICTIONATION_DATE_CANNOT_BE_PRESENT);
		}
		if (StringUtils.isEmpty(defect.getName()) && StringUtils.isEmpty(defect.getStatus()) && StringUtils.isEmpty(defect.getPriority()) && StringUtils.isEmpty(defect.getDescription())) {
			throw new InvalidRequestException(INVALID_PUT_REQUESTS);
		}
	}

	public void validateRequest(final Defect defect) {
		if (defect.getId() != null) {
			throw new InvalidRequestException(ID_CANNOT_BE_PRESENT);
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
			throw new InvalidRequestException(MISSING_FIELDS + invalidFieldNames);
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
