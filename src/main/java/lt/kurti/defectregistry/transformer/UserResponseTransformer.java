package lt.kurti.defectregistry.transformer;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.UserIdentifier;
import lt.kurti.defectregistry.domain.userservice.User;
import lt.kurti.defectregistry.domain.userservice.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserResponseTransformer {

    public User transformUserResponseToUser(final UserResponse userResponse) {
        final User user = new User();

        user.setEmail(userResponse.getData().getEmail());
        user.setFirstName(userResponse.getData().getFirstName());
        user.setLastName(userResponse.getData().getLastName());

        return user;
    }

    public User transformDefectRequestBodyToUser(final Defect defect) {
        if (defect.getUsers() != null) {
            final User user = new User();

            final UserIdentifier userIdentifier = defect.getUsers().get(0);

            user.setEmail(userIdentifier.getEmail());
            user.setFirstName(userIdentifier.getFirstName());
            user.setLastName(userIdentifier.getLastName());

            return user;
        }
        return null;
    }

}
