package lt.kurti.defectregistry.transformer;

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

}
