package lt.kurti.defectregistry.web.rest.errors;

public class ErrorConstants {

	public static String ID_CANNOT_BE_PRESENT = "A new defect cannot have an id";
	public static String DEFECT_NOT_FOUND_BY_ID = "Defect with provided id was not found.";
	public static String INVALID_PRIORITY_NAME = "Defect's priority should be one of the following: LOW, MEDIUM, HIGH";
	public static String INVALID_STATUS_NAME = "Defect's status should be one of the following: NEW, IN_PROGRESS, RESOLVED, REJECTED";
	public static String CREATION_DATE_CANNOT_BE_PRESENT = "Creation date cannot be present in the request";
	public static String ID_HAS_TO_BE_PRESENT = "Defect id has to be present in the request body";
	public static String MISSING_FIELDS = "The following fields are required and cannot be empty: ";
}
