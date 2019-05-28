package lt.kurti.defectregistry.soap.transformer;


import lt.kurti.defectregistry.domain.userservice.UserResponse;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.User;
import lt.kurti.defectregistry.wsdl.UserIdentifier;
import org.springframework.stereotype.Service;

@Service("soapUserResponseTransformer")
public class UserResponseTransformer {

	public User transformUserResponseToUser(final UserResponse userResponse) {
		final User user = new User();

		user.setEmail(userResponse.getData().getEmail());
		user.setFirstName(userResponse.getData().getFirstName());
		user.setLastName(userResponse.getData().getLastName());

		return user;
	}

	public User transformDefectRequestBodyToUser(final Defect defect) {
		if (defect.getUser().size() != 0 && defect.getUser().get(0) != null) {
			final User user = new User();

			final UserIdentifier userIdentifier = defect.getUser().get(0);

			user.setEmail(userIdentifier.getEmail());
			user.setFirstName(userIdentifier.getFirstName());
			user.setLastName(userIdentifier.getLastName());

			return user;
		}
		return null;
	}

}
