package lt.kurti.defectregistry.soap.service.impl;

import static lt.kurti.defectregistry.soap.Constants.ERROR_CODE;
import static lt.kurti.defectregistry.soap.Constants.NOT_FOUND;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.USER_ID_NOT_ASSOCIATED;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import javax.transaction.Transactional;

import lt.kurti.defectregistry.domain.userservice.UserResponse;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.repository.UserIdentifierRepository;
import lt.kurti.defectregistry.soap.exceptions.ServiceFault;
import lt.kurti.defectregistry.soap.exceptions.ServiceFaultException;
import lt.kurti.defectregistry.soap.service.UserService;
import lt.kurti.defectregistry.soap.transformer.DTOTransformer;
import lt.kurti.defectregistry.soap.transformer.UserResponseTransformer;
import lt.kurti.defectregistry.web.rest.errors.ResourceNotFoundException;
import lt.kurti.defectregistry.wsdl.AddUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.User;
import lt.kurti.defectregistry.wsdl.UserIdentifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("soapUserService")
public class UserServiceImpl implements UserService {

	@Value("${userservice.url}")
	private String userServiceUrl;

	private final DefectRepository defectRepository;
	private final UserResponseTransformer userResponseTransformer;
	private final UserIdentifierRepository userIdentifierRepository;
	private final DTOTransformer dtoTransformer;
	private RestTemplate restTemplate;

	public UserServiceImpl(final DefectRepository defectRepository,
						   final DTOTransformer dtoTransformer,
						   @Qualifier("soapUserResponseTransformer") final UserResponseTransformer userResponseTransformer,
						   final UserIdentifierRepository userIdentifierRepository) {
		this.defectRepository = defectRepository;
		this.userResponseTransformer = userResponseTransformer;
		this.userIdentifierRepository = userIdentifierRepository;
		this.dtoTransformer = dtoTransformer;
		this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	}

	@Override
	@Transactional
	public User createUserForDefect(final AddUserToDefectRequest addUserToDefectRequest) {
		final Defect defect = getDefectById(addUserToDefectRequest.getDefectId());

		final UserResponse userResponse = submitPostRequestForUser(addUserToDefectRequest.getDefectId(), addUserToDefectRequest.getUser());

		persistUser(userResponse, defect);

		return userResponseTransformer.transformUserResponseToUser(userResponse);
	}

	@Override
	@Transactional
	public List<User> getUsersForDefect(final Long id) {
		final Defect defect = getDefectById(id);

		return getUsersAssociatedToDefect(defect);
	}

	@Override
	@Transactional
	public User getUserByIdForDefect(final Long defectId, final String userId) {
		final Defect defect = getDefectById(defectId);

		final Optional<String> email = getEmailByIdentifier(defect, userId);

		if (email.isPresent()) {
			return makeGetRequestForUser(email.get());
		}
		throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, USER_ID_NOT_ASSOCIATED));
	}

	@Override
	@Transactional
	public User updateUserForDefect(final User user, final Long defectId, final String userId) {
		final Defect defect = getDefectById(defectId);

		final Optional<String> email = getEmailByIdentifier(defect, userId);

		if (email.isPresent()) {
			final UserResponse userResponse = makePutRequestForUser(email.get(), user);
			if (!userResponse.getData().getEmail().equals(userId)) {
				updateUser(userId, defect, userResponse);
			}
			return userResponseTransformer.transformUserResponseToUser(userResponse);
		}
		throw new ResourceNotFoundException(USER_ID_NOT_ASSOCIATED);
	}

	private UserResponse makePatchRequestForUser(final String emailId, final User user) {
		final HttpEntity<User> request = new HttpEntity<>(user);
		return restTemplate.exchange(userServiceUrl + "/" + emailId, HttpMethod.PATCH, request,
				UserResponse.class).getBody();
	}

	private void updateUser(final String email, final Defect defect, final UserResponse userResponse) {
		final UserIdentifier userIdentifier = new UserIdentifier();
		userIdentifier.setEmail(userResponse.getData().getEmail());
		userIdentifier.setId(getIdByEmail(defect, email).get());

		final lt.kurti.defectregistry.domain.Defect domainDefect = dtoTransformer.convertToDomainDefect(defect);
		final lt.kurti.defectregistry.domain.UserIdentifier domainUserIdentifier = dtoTransformer.convertToDomainUserIdentifier(userIdentifier);

		domainUserIdentifier.setDefect(domainDefect);

		userIdentifierRepository.save(domainUserIdentifier);
	}

	private Optional<Long> getIdByEmail(final Defect defect, final String email) {
		return defect.getUser().stream()
				.filter(user -> user.getEmail().equals(email))
				.map(UserIdentifier::getId)
				.findFirst();
	}

	private UserResponse makePutRequestForUser(final String emailId, final User user) {
		final HttpEntity<User> request = new HttpEntity<>(user);
		return restTemplate.exchange(userServiceUrl + "/" + emailId, HttpMethod.PUT, request,
				UserResponse.class).getBody();
	}

	@Override
	@Transactional
	public User patchUserForDefect(final User user, final Long defectId, final String userId) {
		final Defect defect = getDefectById(defectId);

		final Optional<String> email = getEmailByIdentifier(defect, userId);

		if (email.isPresent()) {
			user.setEmail(email.get());
			final UserResponse userResponse = makePatchRequestForUser(email.get(), user);
			if (!userResponse.getData().getEmail().equals(userId)) {
				updateUser(userId, defect, userResponse);
			}
			return userResponseTransformer.transformUserResponseToUser(userResponse);
		}
		throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, USER_ID_NOT_ASSOCIATED));
	}

	@Override
	@Transactional
	public void deleteUserForDefect(final Long defectId, final String userId) {
		final Defect defect = getDefectById(defectId);

		final Optional<String> email = getEmailByIdentifier(defect, userId);

		if (email.isPresent()) {
			restTemplate.delete(userServiceUrl + "/" + email);
			userIdentifierRepository.deleteById(getIdByEmail(defect, userId).get());
		} else {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, USER_ID_NOT_ASSOCIATED));
		}
	}

	private Optional<String> getEmailByIdentifier(final Defect defect, final String userId) {
		return defect.getUser().stream()
				.map(UserIdentifier::getEmail)
				.filter(email -> email.equals(userId))
				.findFirst();
	}

	private List<User> getUsersAssociatedToDefect(final Defect defect) {
		return defect.getUser().stream()
				.map(UserIdentifier::getEmail)
				.map(this::makeGetRequestForUser)
				.collect(Collectors.toList());
	}

	private User makeGetRequestForUser(final String email) {
		final UserResponse userResponse = restTemplate.getForObject(userServiceUrl + "/" + email, UserResponse.class);
		return userResponseTransformer.transformUserResponseToUser(userResponse);
	}

	private Defect getDefectById(final Long id) {
		final Optional<lt.kurti.defectregistry.domain.Defect> domainDefect = defectRepository.findById(id);
		if (!domainDefect.isPresent()) {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, DEFECT_NOT_FOUND_BY_ID));
		}

		return dtoTransformer.convertToWsdlDefect(domainDefect.get());
	}

	private void persistUser(final UserResponse userResponse, final Defect defect) {
		final UserIdentifier userIdentifier = new UserIdentifier();
		userIdentifier.setEmail(userResponse.getData().getEmail());
		final lt.kurti.defectregistry.domain.UserIdentifier domainUserIdentifier = dtoTransformer.convertToDomainUserIdentifier(userIdentifier);
		domainUserIdentifier.setDefect(dtoTransformer.convertToDomainDefect(defect));

		userIdentifierRepository.save(domainUserIdentifier);
	}

	private UserResponse submitPostRequestForUser(final Long id, final User user) {
		final HttpEntity<User> request = new HttpEntity<>(user);
		return restTemplate.postForObject(userServiceUrl, request, UserResponse.class);
	}

}
