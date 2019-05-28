package lt.kurti.defectregistry.soap.endpoint;


import static lt.kurti.defectregistry.soap.Constants.NAMESPACE_URI;
import static lt.kurti.defectregistry.soap.Constants.USER_FOR_DEFECT_DELETED_SUCCESSFULLY;

import java.util.List;

import lt.kurti.defectregistry.soap.service.UserService;
import lt.kurti.defectregistry.wsdl.AddUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.DeleteDefectResponse;
import lt.kurti.defectregistry.wsdl.DeleteUserForDefectRequest;
import lt.kurti.defectregistry.wsdl.GetUserForDefectRequest;
import lt.kurti.defectregistry.wsdl.GetUserResponse;
import lt.kurti.defectregistry.wsdl.GetUsersForDefectRequest;
import lt.kurti.defectregistry.wsdl.GetUsersResponse;
import lt.kurti.defectregistry.wsdl.PatchUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.PutUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class UserEndpoint {

	private final UserService userService;

	public UserEndpoint(@Qualifier("soapUserService") final UserService userService) {
		this.userService = userService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserToDefectRequest")
	@ResponsePayload
	public GetUserResponse addUserToDefect(@RequestPayload AddUserToDefectRequest addUserToDefectRequest) {
		final User result = userService.createUserForDefect(addUserToDefectRequest);

		final GetUserResponse getUserResponse = new GetUserResponse();
		getUserResponse.setUser(result);

		return getUserResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUsersForDefectRequest")
	@ResponsePayload
	public GetUsersResponse getUsersForDefect(@RequestPayload GetUsersForDefectRequest getUsersForDefectRequest) {
		final List<User> result = userService.getUsersForDefect(getUsersForDefectRequest.getDefectId());

		final GetUsersResponse getUsersResponse = new GetUsersResponse();
		result.forEach(user -> getUsersResponse.getUser().add(user));

		return getUsersResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserForDefectRequest")
	@ResponsePayload
	public GetUserResponse getUserForDefect(@RequestPayload GetUserForDefectRequest getUserForDefectRequest) {
		final User result = userService.getUserByIdForDefect(getUserForDefectRequest.getDefectId(), getUserForDefectRequest.getEmail());

		final GetUserResponse getUserResponse = new GetUserResponse();
		getUserResponse.setUser(result);

		return getUserResponse;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "putUserToDefectRequest")
	@ResponsePayload
	public GetUserResponse updateUserForDefect(@RequestPayload PutUserToDefectRequest putUserToDefectRequest) {
		final User result = userService.updateUserForDefect(putUserToDefectRequest.getUser(), putUserToDefectRequest.getDefectId(),
				putUserToDefectRequest.getEmail());

		final GetUserResponse getUserResponse = new GetUserResponse();
		getUserResponse.setUser(result);

		return getUserResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "patchUserToDefectRequest")
	@ResponsePayload
	public GetUserResponse patchUserForDefect(@RequestPayload PatchUserToDefectRequest patchUserToDefectRequest) {
		final User result = userService.patchUserForDefect(patchUserToDefectRequest.getUser(), patchUserToDefectRequest.getDefectId(),
				patchUserToDefectRequest.getEmail());

		final GetUserResponse getUserResponse = new GetUserResponse();
		getUserResponse.setUser(result);

		return getUserResponse;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserForDefectRequest")
	@ResponsePayload
	public DeleteDefectResponse deleteUserForDefect(@RequestPayload DeleteUserForDefectRequest deleteUserForDefectRequest) {
		userService.deleteUserForDefect(deleteUserForDefectRequest.getDefectId(), deleteUserForDefectRequest.getEmail());

		final DeleteDefectResponse deleteDefectResponse = new DeleteDefectResponse();
		deleteDefectResponse.setMessage(USER_FOR_DEFECT_DELETED_SUCCESSFULLY);

		return deleteDefectResponse;
	}
}
