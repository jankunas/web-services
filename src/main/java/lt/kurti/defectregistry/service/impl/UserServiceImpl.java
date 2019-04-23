package lt.kurti.defectregistry.service.impl;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.UserIdentifier;
import lt.kurti.defectregistry.domain.userservice.User;
import lt.kurti.defectregistry.domain.userservice.UserResponse;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.repository.UserIdentifierRepository;
import lt.kurti.defectregistry.service.UserService;
import lt.kurti.defectregistry.transformer.UserResponseTransformer;
import lt.kurti.defectregistry.web.rest.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.USER_ID_NOT_ASSOCIATED;

@Service
public class UserServiceImpl implements UserService {

    @Value("${userservice.url}")
    private String userServiceUrl;

    private final DefectRepository defectRepository;
    private final UserResponseTransformer userResponseTransformer;
    private final UserIdentifierRepository userIdentifierRepository;
    private RestTemplate restTemplate;

    public UserServiceImpl(final DefectRepository defectRepository, final UserResponseTransformer userResponseTransformer,
                           final UserIdentifierRepository userIdentifierRepository) {
        this.defectRepository = defectRepository;
        this.userResponseTransformer = userResponseTransformer;
        this.userIdentifierRepository = userIdentifierRepository;
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    public User createUserForDefect(final Long id, final User user) {
        final Defect defect = getDefectById(id);

        final UserResponse userResponse = submitPostRequestForUser(id, user);

        persistUser(userResponse, defect);

        return userResponseTransformer.transformUserResponseToUser(userResponse);
    }

    @Override
    public List<User> getUsersForDefect(final Long id) {
        final Defect defect = getDefectById(id);

        return getUsersAssociatedToDefect(defect);
    }

    @Override
    public User getUserByIdForDefect(final Long defectId, final String userId) {
        final Defect defect = getDefectById(defectId);

        final Optional<String> email = getEmailByIdentifier(defect, userId);

        if (email.isPresent()) {
            return makeGetRequestForUser(email.get());
        }
        throw new ResourceNotFoundException(USER_ID_NOT_ASSOCIATED);
    }

    @Override
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
        userIdentifier.setDefect(defect);
        userIdentifier.setEmail(userResponse.getData().getEmail());
        userIdentifier.setId(getIdByEmail(defect, email).get());
        userIdentifierRepository.save(userIdentifier);
    }

    private Optional<Long> getIdByEmail(final Defect defect, final String email) {
        return defect.getUsers().stream()
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
    public User patchUserForDefect(final User user, final Long defectId, final String userId) {
        final Defect defect = getDefectById(defectId);

        final Optional<String> email = getEmailByIdentifier(defect, userId);

        if (email.isPresent()) {
            final UserResponse userResponse = makePatchRequestForUser(email.get(), user);
            if (!userResponse.getData().getEmail().equals(userId)) {
                updateUser(userId, defect, userResponse);
            }
            return userResponseTransformer.transformUserResponseToUser(userResponse);
        }
        throw new ResourceNotFoundException(USER_ID_NOT_ASSOCIATED);
    }

    @Override
    public void deleteUserForDefect(final Long defectId, final String userId) {
        final Defect defect = getDefectById(defectId);

        final Optional<String> email = getEmailByIdentifier(defect, userId);

        if (email.isPresent()) {
            restTemplate.delete(userServiceUrl + "/" + email);
            userIdentifierRepository.deleteById(getIdByEmail(defect, userId).get());
        } else {
            throw new ResourceNotFoundException(USER_ID_NOT_ASSOCIATED);
        }
    }

    private Optional<String> getEmailByIdentifier(final Defect defect, final String userId) {
        return defect.getUsers().stream()
                .map(UserIdentifier::getEmail)
                .filter(email -> email.equals(userId))
                .findFirst();
    }

    private List<User> getUsersAssociatedToDefect(final Defect defect) {
        return defect.getUsers().stream()
                .map(UserIdentifier::getEmail)
                .map(this::makeGetRequestForUser)
                .collect(Collectors.toList());
    }

    private User makeGetRequestForUser(final String email) {
        final UserResponse userResponse = restTemplate.getForObject(userServiceUrl + "/" + email, UserResponse.class);
        return userResponseTransformer.transformUserResponseToUser(userResponse);
    }

    private Defect getDefectById(final Long id) {
        final Optional<Defect> defect = defectRepository.findById(id);
        if (!defect.isPresent()) {
            throw new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID);
        }
        return defect.get();
    }

    private void persistUser(final UserResponse userResponse, final Defect defect) {
        final UserIdentifier userIdentifier = new UserIdentifier();
        userIdentifier.setEmail(userResponse.getData().getEmail());
        userIdentifier.setDefect(defect);
        userIdentifierRepository.save(userIdentifier);
    }

    private UserResponse submitPostRequestForUser(final Long id, final User user) {
        final HttpEntity<User> request = new HttpEntity<>(user);
        return restTemplate.postForObject(userServiceUrl, request, UserResponse.class);
    }


}
