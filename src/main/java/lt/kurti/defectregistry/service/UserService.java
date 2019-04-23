package lt.kurti.defectregistry.service;

import lt.kurti.defectregistry.domain.userservice.User;

import java.util.List;

public interface UserService {

    User createUserForDefect(Long id, User user);

    List<User> getUsersForDefect(Long id);

    User getUserByIdForDefect(Long defectId, String userId);

    User updateUserForDefect(User user, Long defectId, String userId);

    User patchUserForDefect(User user, Long defectId, String userId);

    void deleteUserForDefect(Long defectId, String userId);
}
