package lt.kurti.defectregistry.service.impl;

import lt.kurti.defectregistry.domain.Defect;
import lt.kurti.defectregistry.domain.UserIdentifier;
import lt.kurti.defectregistry.domain.userservice.User;
import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.service.DefectService;
import lt.kurti.defectregistry.service.UserService;
import lt.kurti.defectregistry.transformer.UserResponseTransformer;
import lt.kurti.defectregistry.validation.DefectValidator;
import lt.kurti.defectregistry.web.rest.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;

@Service
public class DefectServiceImpl implements DefectService {

    private final DefectRepository defectRepository;
    private final DefectValidator defectValidator;
    private final UserResponseTransformer userResponseTransformer;
    private final UserService userService;
    private RestTemplate restTemplate;

    @Autowired
    public DefectServiceImpl(final DefectRepository defectRepository, final DefectValidator defectValidator,
                             final UserResponseTransformer userResponseTransformer, final UserService userService) {
        this.defectRepository = defectRepository;
        this.defectValidator = defectValidator;
        this.userResponseTransformer = userResponseTransformer;
        this.userService = userService;
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    @Override
    public Defect createDefect(final Defect defect) {
        defectValidator.validateRequest(defect);

        final Defect persistedDefect = defectRepository.save(defect);

        final User user = userResponseTransformer.transformDefectRequestBodyToUser(defect);

        if (user != null) {
            userService.createUserForDefect(persistedDefect.getId(), user);
        }

        return persistedDefect;
    }

    @Override
    public Defect updateDefect(final Defect defect, final Long id) {
        defectValidator.validateRequest(defect);

        final Date existingDefectCreationDate = defectRepository.findById(id)
                .map(Defect::getDateCreated)
                .orElseThrow(() -> new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID));

        defect.setId(id);
        final Defect updatedDefect = defectRepository.save(defect);
        updatedDefect.setDateCreated(existingDefectCreationDate);

        return updatedDefect;
    }

    @Override
    public Defect patchDefect(final Defect defect, final Long id) {
        final Defect existingDefect = defectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID));

        defectValidator.validatePatchRequest(defect);
        defect.setId(id);

        if (!StringUtils.isEmpty(defect.getName())) {
            existingDefect.setName(defect.getName());
        }
        if (!StringUtils.isEmpty(defect.getDescription())) {
            existingDefect.setDescription(defect.getDescription());
        }
        if (!StringUtils.isEmpty(defect.getPriority())) {
            existingDefect.setPriority(defect.getPriority());
        }
        if (!StringUtils.isEmpty(defect.getStatus())) {
            existingDefect.setStatus(defect.getStatus());
        }


        final Defect updatedDefect = defectRepository.save(existingDefect);
        updatedDefect.setDateCreated(existingDefect.getDateCreated());

        return updatedDefect;
    }

    @Override
    public List<Defect> getDefects() {
        return defectRepository.findAll();
    }

    @Override
    public Optional<Defect> getDefectById(final Long id, final String embedded) {
        if (embedded != null && embedded.equals("users")) {
            final Optional<Defect> defect = defectRepository.findById(id);
            if (defect.isPresent()) {
                final List<UserIdentifier> userIdentifiers = defect.get().getUsers();
                final List<User> userList = new ArrayList<>();
                for (UserIdentifier userIdentifier : userIdentifiers) {
                    final String email = userIdentifier.getEmail();
                    userList.add(userService.getUserByIdForDefect(defect.get().getId(), email));
                }

                defect.get().getUsers().clear();

                for (User user : userList) {
                    final UserIdentifier userIdentifier = new UserIdentifier();
                    userIdentifier.setEmail(user.getEmail());
                    userIdentifier.setFirstName(user.getFirstName());
                    userIdentifier.setLastName(user.getLastName());
                    defect.get().setUser(userIdentifier);
                }

                return defect;
            }
        }

        return defectRepository.findById(id);
    }

    @Override
    public void deleteDefectById(final Long id) {
        if (defectRepository.findById(id).isPresent()) {
            defectRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(DEFECT_NOT_FOUND_BY_ID);
        }
    }
}
