package lt.kurti.defectregistry.soap.service;

import java.util.List;

import lt.kurti.defectregistry.wsdl.AddUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.User;

public interface UserService {

	User createUserForDefect(AddUserToDefectRequest addUserToDefectRequest);

	List<User> getUsersForDefect(Long id);

	User getUserByIdForDefect(Long defectId, String userId);

	User updateUserForDefect(User user, Long defectId, String userId);

	User patchUserForDefect(User user, Long defectId, String userId);

	void deleteUserForDefect(Long defectId, String userId);
}
