package lt.kurti.defectregistry.soap.service.impl;

import static lt.kurti.defectregistry.soap.Constants.ERROR_CODE;
import static lt.kurti.defectregistry.soap.Constants.NOT_FOUND;
import static lt.kurti.defectregistry.web.rest.errors.ErrorConstants.DEFECT_NOT_FOUND_BY_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.xml.datatype.XMLGregorianCalendar;

import lt.kurti.defectregistry.repository.DefectRepository;
import lt.kurti.defectregistry.soap.exceptions.ServiceFault;
import lt.kurti.defectregistry.soap.exceptions.ServiceFaultException;
import lt.kurti.defectregistry.soap.service.DefectService;
import lt.kurti.defectregistry.soap.service.UserService;
import lt.kurti.defectregistry.soap.transformer.DTOTransformer;
import lt.kurti.defectregistry.soap.transformer.UserResponseTransformer;
import lt.kurti.defectregistry.soap.validation.DefectValidator;
import lt.kurti.defectregistry.wsdl.AddUserToDefectRequest;
import lt.kurti.defectregistry.wsdl.CreateDefectRequest;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.PatchDefectRequest;
import lt.kurti.defectregistry.wsdl.UpdateDefectRequest;
import lt.kurti.defectregistry.wsdl.User;
import lt.kurti.defectregistry.wsdl.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service("soapDefectService")
public class DefectServiceImpl implements DefectService {

	private final DefectRepository defectRepository;
	private final DefectValidator defectValidator;
	private final UserResponseTransformer userResponseTransformer;
	private final UserService userService;
	private final DTOTransformer dtoTransformer;
	private RestTemplate restTemplate;

	@Autowired
	public DefectServiceImpl(final DefectRepository defectRepository,
							 final DTOTransformer dtoTransformer,
							 @Qualifier("soapDefectValidator") final DefectValidator defectValidator,
							 @Qualifier("soapUserResponseTransformer") final UserResponseTransformer userResponseTransformer,
							 @Qualifier("soapUserService") final UserService userService) {
		this.defectRepository = defectRepository;
		this.defectValidator = defectValidator;
		this.userResponseTransformer = userResponseTransformer;
		this.userService = userService;
		this.dtoTransformer = dtoTransformer;
		this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	}

	@Override
	@Transactional
	public Defect createDefect(final CreateDefectRequest createDefectRequest) {
		final Defect defect = dtoTransformer.convertCreateDefectRequestToDefect(createDefectRequest);

		defectValidator.validateRequest(defect);

		final Defect persistedDefect = dtoTransformer.convertToWsdlDefect(defectRepository.save(dtoTransformer.convertToDomainDefect(defect)));

		final User user = userResponseTransformer.transformDefectRequestBodyToUser(defect);

		if (user != null) {
			final AddUserToDefectRequest addUserToDefectRequest = new AddUserToDefectRequest();
			addUserToDefectRequest.setDefectId(persistedDefect.getId());
			addUserToDefectRequest.setUser(user);
			userService.createUserForDefect(addUserToDefectRequest);
		}

		return persistedDefect;
	}

	@Override
	@Transactional
	public Defect updateDefect(final UpdateDefectRequest updateDefectRequest) {
		final Defect defect = dtoTransformer.convertUpdateDefectRequestToDefect(updateDefectRequest);

		defectValidator.validateUpdateRequest(defect);

		final XMLGregorianCalendar existingDefectCreationDate = defectRepository.findById(defect.getId())
				.map(lt.kurti.defectregistry.domain.Defect::getDateCreated)
				.map(dtoTransformer::convertDateToXMLDate)
				.orElseThrow(() -> new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, DEFECT_NOT_FOUND_BY_ID)));

		defect.setId(defect.getId());
		final Defect updatedDefect = dtoTransformer.convertToWsdlDefect(defectRepository.save(dtoTransformer.convertToDomainDefect(defect)));
		updatedDefect.setDateCreated(existingDefectCreationDate);

		return updatedDefect;
	}

	@Override
	@Transactional
	public Defect patchDefect(final PatchDefectRequest patchDefectRequest) {
		final Defect defect = dtoTransformer.convertPatchDefectRequestToDefect(patchDefectRequest);

		defectValidator.validatePatchRequest(defect);

		final Defect existingDefect = defectRepository.findById(defect.getId())
				.map(dtoTransformer::convertToWsdlDefect)
				.orElseThrow(() -> new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, DEFECT_NOT_FOUND_BY_ID)));

		defect.setId(defect.getId());

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

		final Defect updatedDefect = dtoTransformer.convertToWsdlDefect(defectRepository.save(dtoTransformer.convertToDomainDefect(existingDefect)));
		updatedDefect.setDateCreated(existingDefect.getDateCreated());

		return updatedDefect;
	}

	@Override
	@Transactional
	public List<Defect> getDefects() {
		final List<Defect> defects = new ArrayList<>();
		final List<lt.kurti.defectregistry.domain.Defect> domainDefects = defectRepository.findAll();
		domainDefects.forEach(domainDefect -> defects.add(dtoTransformer.convertToWsdlDefect(domainDefect)));
		return defects;
	}

	@Override
	@Transactional
	public Defect getDefectById(final Long id, final String embedded) {
		if (embedded != null && embedded.equals("users")) {
			final Optional<Defect> defect = Optional.of(dtoTransformer.convertToWsdlDefect(defectRepository.findById(id).get()));
			if (defect.isPresent()) {
				final List<UserIdentifier> userIdentifiers = defect.get().getUser();
				final List<User> userList = new ArrayList<>();
				for (UserIdentifier userIdentifier : userIdentifiers) {
					final String email = userIdentifier.getEmail();
					userList.add(userService.getUserByIdForDefect(defect.get().getId(), email));
				}

				defect.get().getUser().clear();

				for (User user : userList) {
					final UserIdentifier userIdentifier = new UserIdentifier();
					userIdentifier.setEmail(user.getEmail());
					userIdentifier.setFirstName(user.getFirstName());
					userIdentifier.setLastName(user.getLastName());
					defect.get().getUser().add(userIdentifier);
				}

				return defect.get();
			}
		}

		return dtoTransformer.convertToWsdlDefect(defectRepository.findById(id).get());
	}

	@Override
	@Transactional
	public Defect getDefectById(final Long id) {
		final Optional<lt.kurti.defectregistry.domain.Defect> domainDefect = defectRepository.findById(id);
		if (domainDefect.isPresent()) {
			return dtoTransformer.convertToWsdlDefect(domainDefect.get());
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteDefectById(final Long id) {
		if (defectRepository.findById(id).isPresent()) {
			defectRepository.deleteById(id);
		} else {
			throw new ServiceFaultException(ERROR_CODE, new ServiceFault(NOT_FOUND, DEFECT_NOT_FOUND_BY_ID));
		}
	}

}
