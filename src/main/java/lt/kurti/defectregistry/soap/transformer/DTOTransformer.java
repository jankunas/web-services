package lt.kurti.defectregistry.soap.transformer;

import java.util.Date;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import lt.kurti.defectregistry.wsdl.CreateDefectRequest;
import lt.kurti.defectregistry.wsdl.Defect;
import lt.kurti.defectregistry.wsdl.DefectPriority;
import lt.kurti.defectregistry.wsdl.DefectStatus;
import lt.kurti.defectregistry.wsdl.PatchDefectRequest;
import lt.kurti.defectregistry.wsdl.UpdateDefectRequest;
import lt.kurti.defectregistry.wsdl.UserIdentifier;
import org.springframework.stereotype.Service;

@Service
public class DTOTransformer {

	public Defect convertPatchDefectRequestToDefect(final CreateDefectRequest patchDefectRequest) {
		final Defect defect = new Defect();

		defect.setId(patchDefectRequest.getId());
		defect.setStatus(patchDefectRequest.getStatus());
		defect.setPriority(patchDefectRequest.getPriority());
		defect.setDescription(patchDefectRequest.getDescription());
		defect.setName(patchDefectRequest.getName());

		return defect;
	}

	public Defect convertUpdateDefectRequestToDefect(final UpdateDefectRequest updateDefectRequest) {
		final Defect defect = new Defect();

		defect.setId(updateDefectRequest.getId());
		defect.setStatus(updateDefectRequest.getStatus());
		defect.setPriority(updateDefectRequest.getPriority());
		defect.setDescription(updateDefectRequest.getDescription());
		defect.setName(updateDefectRequest.getName());

		return defect;
	}

	public Defect convertCreateDefectRequestToDefect(final CreateDefectRequest createDefectRequest) {
		final Defect defect = new Defect();

		defect.getUser().add(createDefectRequest.getUser());
		defect.setStatus(createDefectRequest.getStatus());
		defect.setPriority(createDefectRequest.getPriority());
		defect.setDescription(createDefectRequest.getDescription());
		defect.setName(createDefectRequest.getName());

		return defect;
	}

	public Defect convertToWsdlDefect(final lt.kurti.defectregistry.domain.Defect domainDefect) {
		final Defect defect = new Defect();

		defect.setDateCreated(convertDateToXMLDate(domainDefect.getDateCreated()));
		defect.setDateUpdated(convertDateToXMLDate(domainDefect.getDateUpdated()));
		defect.setDescription(domainDefect.getDescription());
		defect.setId(domainDefect.getId());
		defect.setName(domainDefect.getName());
		defect.setPriority(convertToXmlDefectPriority(domainDefect.getPriority()));
		defect.setStatus(convertToXmlDefectStatus(domainDefect.getStatus()));
		if (domainDefect.getUsers() != null && domainDefect.getUsers().size() != 0) {
			domainDefect.getUsers().forEach(domainUser -> {
				defect.getUser().add(convertToWsdlUserIdentifier(domainUser));
			});
		}

		return defect;
	}

	public lt.kurti.defectregistry.domain.Defect convertToDomainDefect(final Defect defect) {
		final lt.kurti.defectregistry.domain.Defect domainDefect = new lt.kurti.defectregistry.domain.Defect();

		if (defect.getDateCreated() != null) {
			domainDefect.setDateCreated(defect.getDateCreated().toGregorianCalendar().getTime());
		}
		if (defect.getDateUpdated() != null) {
			domainDefect.setDateUpdated(defect.getDateUpdated().toGregorianCalendar().getTime());
		}
		domainDefect.setDescription(defect.getDescription());
		domainDefect.setId(defect.getId());
		domainDefect.setName(defect.getName());
		domainDefect.setPriority(convertToDomainDefectPriority(defect.getPriority()));
		domainDefect.setStatus(convertToDomainDefectStatus(defect.getStatus()));

		if (defect.getUser().size() != 0 && defect.getUser().get(0) != null) {
			defect.getUser().forEach(user -> {
				domainDefect.setUser(convertToDomainUserIdentifier(user));
			});
		}

		return domainDefect;
	}

	public XMLGregorianCalendar convertDateToXMLDate(final Date date) {
		if (date != null) {
			String dateString = date.toInstant().toString();
			try {
				return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateString);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public lt.kurti.defectregistry.domain.enumeration.DefectStatus convertToDomainDefectStatus(final DefectStatus defectStatus) {
		switch (defectStatus) {
			case NEW:
				return lt.kurti.defectregistry.domain.enumeration.DefectStatus.NEW;
			case REJECTED:
				return lt.kurti.defectregistry.domain.enumeration.DefectStatus.REJECTED;
			case RESOLVED:
				return lt.kurti.defectregistry.domain.enumeration.DefectStatus.RESOLVED;
			case IN_PROGRESS:
				return lt.kurti.defectregistry.domain.enumeration.DefectStatus.IN_PROGRESS;
		}
		return null;
	}

	public DefectStatus convertToXmlDefectStatus(final lt.kurti.defectregistry.domain.enumeration.DefectStatus domainDefectStatus) {
		switch (domainDefectStatus) {
			case NEW:
				return DefectStatus.NEW;
			case REJECTED:
				return DefectStatus.REJECTED;
			case RESOLVED:
				return DefectStatus.RESOLVED;
			case IN_PROGRESS:
				return DefectStatus.IN_PROGRESS;
		}
		return null;
	}

	public lt.kurti.defectregistry.domain.enumeration.DefectPriority convertToDomainDefectPriority(final DefectPriority defectPriority) {
		switch (defectPriority) {
			case LOW:
				return lt.kurti.defectregistry.domain.enumeration.DefectPriority.LOW;
			case MEDIUM:
				return lt.kurti.defectregistry.domain.enumeration.DefectPriority.MEDIUM;
			case HIGH:
				return lt.kurti.defectregistry.domain.enumeration.DefectPriority.HIGH;
		}
		return null;
	}

	public DefectPriority convertToXmlDefectPriority(final lt.kurti.defectregistry.domain.enumeration.DefectPriority domainDefectPriority) {
		switch (domainDefectPriority) {
			case LOW:
				return DefectPriority.LOW;
			case MEDIUM:
				return DefectPriority.MEDIUM;
			case HIGH:
				return DefectPriority.HIGH;
		}
		return null;
	}

	public UserIdentifier convertToWsdlUserIdentifier(final lt.kurti.defectregistry.domain.UserIdentifier domainUserIdentifier) {
		final UserIdentifier userIdentifier = new UserIdentifier();

		userIdentifier.setEmail(domainUserIdentifier.getEmail());
		userIdentifier.setFirstName(domainUserIdentifier.getFirstName());
		userIdentifier.setLastName(domainUserIdentifier.getLastName());
		userIdentifier.setId(domainUserIdentifier.getId());

		return userIdentifier;
	}

	public lt.kurti.defectregistry.domain.UserIdentifier convertToDomainUserIdentifier(final UserIdentifier userIdentifier) {
		final lt.kurti.defectregistry.domain.UserIdentifier domainUserIdentifier = new lt.kurti.defectregistry.domain.UserIdentifier();

		domainUserIdentifier.setLastName(userIdentifier.getLastName());
		domainUserIdentifier.setFirstName(userIdentifier.getFirstName());
		domainUserIdentifier.setEmail(userIdentifier.getEmail());
		domainUserIdentifier.setId(userIdentifier.getId());

		return domainUserIdentifier;
	}
}
