package lt.kurti.defectregistry.web.rest.errors;

public class ErrorConstants {

	public static String ID_CANNOT_BE_PRESENT = "A new defect cannot have an id";
	public static String DEFECT_NOT_FOUND_BY_ID = "Defect with provided id was not found.";
	public static String USER_ID_NOT_ASSOCIATED = "Provided user id is not associated with the defect.";
	public static String INVALID_PRIORITY_NAME = "Defect's priority should be one of the following: LOW, MEDIUM, HIGH";
	public static String INVALID_STATUS_NAME = "Defect's status should be one of the following: NEW, IN_PROGRESS, RESOLVED, REJECTED";
	public static String CREATION_DATE_CANNOT_BE_PRESENT = "Creation date cannot be present in the request";
	public static String MODIFICTIONATION_DATE_CANNOT_BE_PRESENT = "Modification date cannot be present in the request";
	public static String MISSING_FIELDS = "The following fields are required and cannot be empty: ";
	public static String INVALID_PUT_REQUESTS = "At least one field should be present - name, description, status, priority";
}
