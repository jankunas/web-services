package lt.kurti.defectregistry.web.rest.errors;

import java.util.Date;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionAdvice {

	@ExceptionHandler({InvalidRequestException.class})
	public ResponseEntity<ErrorMessage> handleInvalidRequestException(final InvalidRequestException e) {
		return formError(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(final ResourceNotFoundException e) {
		return formError(HttpStatus.NOT_FOUND, e);
	}

	private ResponseEntity<ErrorMessage> formError(final HttpStatus status, final Exception e) {
		final ErrorMessage errorMessage = new ErrorMessage(new Date(), e.getMessage(), status.getReasonPhrase());
		return ResponseEntity.status(status).body(errorMessage);
	}
}
